package com.liukai.datastructure.ch_37_greedy;

import lombok.Data;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

/**
 * 37-2 贪心算法——霍夫曼编码
 *
 * <p>描述：霍夫曼编码是非常有效的编码方式。
 *
 * <p>原理：它通过分析文本中不同的字符、出现的频率，利用优先级队列小顶堆，以从底向上的方式按照一定规则构建出一个树结构， *
 * 根据树来对不同长度的字符进行编码，一次来通过不等长的编码方式，进一步增加压缩比例。
 *
 * <p>规定：各个字符的编码之间，不会出现某个编码是另一个编码的前缀的情况。
 *
 * <p>霍夫曼树构建步骤： 1. 把每个字符都看作一个节点，并且辅带着把频率放到优先级队列中（小顶堆）；
 *
 * <p>2. 从队列中取出两个频率最小的节点 A、B，然后新建一个节点 C，把频率设置为这两个节点的频率之和，并且这个新节点作为节点 A、B的父节点；
 *
 * <p>3. 最后把节点 C 放入到优先级队列中，重复这个过程，直到队列中没有数据；
 *
 * <p>4. 为树中的每一条边加上一个权值，指向左边的子节点的边，标记为 0；指向右节点的边，标记为 1；
 *
 * <p>5. 从根节点到叶子节点的路径就是叶节点对应字符的霍夫曼编码。
 *
 * <p>贪心思想：把频率出现较多的字符，用稍微短一些编码；出现频率比较少的字符，用稍微长一些的编码。
 */
public class HuffmanCode {

  private final char[] text;

  /**
   * 字符对应的编码表，用于编码
   */
  Map<Character, String> charCodeMap = new HashMap<>();

  /**
   * 字符对应的编码表，用于解码
   */
  Map<String, Character> codeCharMap = new HashMap<>();

  public HuffmanCode(char[] text) {
    this.text = text;
    buildHuffmanTree();
  }

  public static void main(String[] args) {

    String str = "abcdeaaaabbbhhceecda";
    System.out.println("压缩之前的编码为：" + str);

    HuffmanCode huffmanCode = new HuffmanCode(str.toCharArray());
    System.out.println("构建的字符编码表为：" + huffmanCode.charCodeMap);
    System.out.println("构建的编码字符表为：" + huffmanCode.codeCharMap);

    String compress = huffmanCode.compress();
    // 压缩
    System.out.println("压缩之后的编码为：" + compress);

    // 解缩
    String decompression = huffmanCode.decompression(compress);
    System.out.println("解压之后的编码为：" + decompression);

    /*
    压缩之前的编码为：abcdeaaaabbbhhceecda
    构建的字符编码表为：{a=011, b=001, c=0101, d=0000, e=0100, h=0001}
    构建的编码字符表为：{011=a, 001=b, 0101=c, 0000=d, 0001=h, 0100=e}
    压缩之后的编码为：0110010101000001000110110110110010010010001000101010100010001010000011
    解压之后的编码为：abcdeaaaabbbhhceecda
     */

  }

  /**
   * 压缩字符串
   *
   * @return 返回压缩的编码
   */
  public String decompression(String compressStr) {
    StringBuilder sb = new StringBuilder();
    StringBuilder tmp = new StringBuilder();
    for (char c : compressStr.toCharArray()) {
      tmp.append(c);
      Character character = codeCharMap.get(tmp.toString());
      if (character != null) {
        sb.append(character);
        tmp = new StringBuilder();
      }
    }
    return sb.toString();
  }

  /**
   * 压缩字符串
   *
   * @return 返回压缩的编码
   */
  public String compress() {
    StringBuilder sb = new StringBuilder();
    for (char c : text) {
      sb.append(charCodeMap.getOrDefault(c, ""));
    }
    return sb.toString();
  }

  /**
   * 构建霍夫曼编码表
   */
  public void buildHuffmanTree() {

    // 通过散列表统计字符出现的频率信息
    Map<Character, Node> charFrequencyMap = new HashMap<>();
    for (char c : text) {
      charFrequencyMap.compute(c, (k, v) -> {
        if (v == null) {
          Node node = new Node(k);
          node.frequency++;
          return node;
        } else {
          v.frequency++;
          return v;
        }
      });
    }

    // 将文本中的字符放入优先级队列中，小顶堆
    PriorityQueue<Node> smallHead = new PriorityQueue<>(
      Comparator.comparingInt(Node::getFrequency));
    smallHead.addAll(charFrequencyMap.values());

    Node root = null;
    // 构建哈夫曼树
    while (!smallHead.isEmpty() && smallHead.size() > 1) {
      Node a = smallHead.poll();
      Node b = smallHead.poll();
      // 构建一个新的节点，将两个频率小的节点的频率之和设置到新节点上
      Node c = new Node();
      c.frequency = a.frequency + b.frequency;
      // 将 c 节点设置为 a、b 的父节点
      c.left = a;
      c.right = b;
      // 将右节点的边权重设置为 1
      c.right.sideWeight = 1;

      if (!smallHead.isEmpty()) {
        // 将 c 节点放入优先级队列
        smallHead.add(c);
      }

      root = c;
    }

    // 只有一个节点的情况
    if (smallHead.size() == 1) {
      Node poll = smallHead.poll();
      Node node = new Node(poll.data);
      node.sideWeight = 1;
      root = new Node();
      root.right = node;
    }

    if (root == null) {
      System.out.println("无效的字符文本");
      return;
    }

    // 深度优先。前序递归遍历
    preOrder(root, "", charCodeMap);

    // 将字符编码表转换成编码字符表
    codeCharMap = charCodeMap.entrySet().stream()
      .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
  }

  private void preOrder(Node node, String prefixCode, Map<Character, String> charCodeMap) {
    // 设置
    if (node.left == null && node.right == null) {
      charCodeMap.put(node.data, prefixCode + node.sideWeight);
      return;
    }
    String newPrefixCode = prefixCode + node.sideWeight;
    if (node.left != null) {
      preOrder(node.left, newPrefixCode, charCodeMap);
    }

    if (node.right != null) {
      preOrder(node.right, newPrefixCode, charCodeMap);
    }
  }

  @Data
  static class Node {

    /**
     * 数据
     */
    char data;

    /**
     * 出现的频率
     */
    int frequency;

    /**
     * 边的权重
     *
     * <p>用于记录根节点到叶子节点的霍夫曼编码，左子节点的边权重为0，右子节点的边权重是 1
     */
    int sideWeight;

    /**
     * 左节点
     */
    Node left;

    /**
     * 右子节点
     */
    Node right;

    public Node() {
    }

    public Node(char data) {
      this.data = data;
    }

  }

}
