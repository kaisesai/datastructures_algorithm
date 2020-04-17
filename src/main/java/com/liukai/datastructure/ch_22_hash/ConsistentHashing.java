package com.liukai.datastructure.ch_22_hash;

import lombok.Builder;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * 22-1 一致性哈希算法
 * <p>
 * 原理：将所有可能的哈希值组成一个圆环，对机器的 IP 哈希取模映射到哈希环中，
 * 再将访问的请求数据哈希取模，按照顺时针方向从 0 开始查找哈希环上的机器结点
 * <p>
 * 参考：<a href="https://juejin.im/post/5cfdf4e5f265da1bd260e04c">一致性hash算法及其java实现</a>
 * <p>
 * 参考：<a href="https://blog.csdn.net/u011305680/article/details/79721030">一致性hash算法及java实现</a>
 */
public class ConsistentHashing {

  /**
   * 每个实际节点对应的虚拟结点数量
   */
  private static final int VIRTUAL_NODE_SIZE_FOR_PEER_REAL_NODE = 10;

  /**
   * 真实的节点
   */
  private static final ConcurrentLinkedQueue<MachineNode> REAL_NODES
    = new ConcurrentLinkedQueue<>();

  /**
   * 虚拟结点
   * <p>
   * 这里使用跳表，提高查询效率。根据哈希环值升序排序
   */
  private static final ConcurrentSkipListMap<Long, MachineNode> VIRTUAL_NODES
    = new ConcurrentSkipListMap<>(Comparator.comparingLong(t -> t));

  public static void main(String[] args) {
    List<String> ipList = Arrays.asList("192.168.1.1", "192.168.1.2", "192.168.1.3", "192.168.1.4");
    // 注册 IP
    ipList.forEach(ConsistentHashing::registerMachineHashNode);

    System.out.println("真实结点：" + REAL_NODES);
    System.out.println("虚拟结点总数：" + VIRTUAL_NODES.size());

    VIRTUAL_NODES.values().forEach(v -> System.out.println("虚拟节点：" + v.virtualIp));

    System.out.println();

    String value = "小红";
    MachineNode mh = matcherMachineHashNode(value);
    System.out.println(value + " 映射到的机器 IP： " + mh);
    value = "小黄";
    mh = matcherMachineHashNode(value);
    System.out.println(value + " 映射到的机器 IP： " + mh);
    value = "小蓝";
    mh = matcherMachineHashNode(value);
    System.out.println(value + " 映射到的机器 IP： " + mh);
  }

  /**
   * 根据 value 查找对应的机器
   *
   * @param value
   * @return
   */
  public static MachineNode matcherMachineHashNode(String value) {
    // 计算哈希环值
    long hashRingCode = hashRingCode(value);

    // 寻找匹配的机器
    if (VIRTUAL_NODES.isEmpty()) {
      return null;
    }
    // 这里也可以使用 TreeMap 的 tailMap 方法，返回 key 大于等于给定值的 map
    ConcurrentNavigableMap<Long, MachineNode> tailMap = VIRTUAL_NODES.tailMap(hashRingCode);
    if (tailMap.isEmpty()) {
      // 如果没有找到机器节点，则取第一个机器节点
      return VIRTUAL_NODES.firstEntry().getValue();
    } else {
      // 第一个 key 就是顺时针找到的机器节点
      return tailMap.firstEntry().getValue();
    }

    // 根据 value 的哈希环值，从有序 map 中的第一个最小的ip 哈希环值进行查找，如果没有找到就去第一个
    // Long ipHashRingCode = machineHashNodeMap.keySet().stream().filter(t -> hashRingCode <= t)
    //   .findFirst().orElse(machineHashNodeMap.firstKey());
    // return machineHashNodeMap.get(ipHashRingCode);
  }

  /**
   * 注册机器节点
   * <p>
   * 让机器与哈希环建立映射关系
   *
   * @param ip 机器 IP 地址
   */
  public static void registerMachineHashNode(String ip) {
    long hashRingCode = hashRingCode(ip);
    // 添加真实节点
    REAL_NODES
      .add(MachineNode.builder().ip(ip).hashRingCode(hashRingCode).isVirtual(false).build());

    // 虚拟节点 IP
    String virtualIp;
    // 虚拟节点哈希环值
    long virtualNodeHashRingCode;
    // 添加虚拟结点来解决哈希环偏斜问题
    for (int i = 0; i < VIRTUAL_NODE_SIZE_FOR_PEER_REAL_NODE; i++) {
      virtualIp = buildVirtualIp(ip, i);
      virtualNodeHashRingCode = hashRingCode(virtualIp);
      VIRTUAL_NODES.put(virtualNodeHashRingCode,
                        MachineNode.builder().ip(ip).hashRingCode(hashRingCode).isVirtual(true)
                          .sequence(i).virtualIp(virtualIp).build());
    }
  }

  /**
   * 使用FNV1_32_HASH算法计算服务器的Hash值,这里不使用重写hashCode的方法，最终效果没区别
   *
   * @param str
   * @return
   */
  private static int hashRingCode(String str) {
    final int p = 16777619;
    int hash = (int) 2166136261L;
    for (int i = 0; i < str.length(); i++) {
      hash = (hash ^ str.charAt(i)) * p;
    }
    hash += hash << 13;
    hash ^= hash >> 7;
    hash += hash << 3;
    hash ^= hash >> 17;
    hash += hash << 5;

    // 如果算出来的值为负数则取其绝对值
    if (hash < 0) {
      hash = Math.abs(hash);
    }
    return hash;
  }

  /**
   * 构建虚拟节点
   *
   * @param ip
   * @param sequence
   * @return
   */
  private static String buildVirtualIp(String ip, int sequence) {
    return ip + MachineNode.VIRTUAL_IP_SUFFIX + sequence;
  }

  /**
   * 机器的哈希结点
   */
  @Builder
  static class MachineNode {

    public static final String VIRTUAL_IP_SUFFIX = "_##_";

    /**
     * 序列
     */
    int sequence;

    /**
     * 机器 IP 地址
     */
    String ip;

    /**
     * 虚拟 IP 地址
     */
    String virtualIp;

    /**
     * 哈希环值
     */
    long hashRingCode;

    /**
     * 是否虚拟结点
     */
    boolean isVirtual;

    @Override
    public String toString() {
      return "MachineNode{" + "sequence=" + sequence + ", ip='" + ip + '\'' + ", virtualIp='"
        + virtualIp + '\'' + ", hashRingCode=" + hashRingCode + ", isVirtual=" + isVirtual + '}';
    }

  }

}
