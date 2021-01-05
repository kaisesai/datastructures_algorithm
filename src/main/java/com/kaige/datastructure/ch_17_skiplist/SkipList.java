package com.kaige.datastructure.ch_17_skiplist;

import java.util.Random;

/**
 * 17-1 跳表
 * <p>
 * 跳表是一种通过单链表构建出多级索来利用二分查找的思想实现的一种数据结构。
 * <p>
 * 特点：
 * 1. 时间复杂度为 O(logn)
 * 2. 空间复杂度为 O(n)
 * <p>
 * 参考：<a href= "https://juejin.im/post/57fa935b0e3dd90057c50fbc">用跳表实现redis 的有序集合</a>
 */
public class SkipList {
  
  /**
   * 最大支持的索引层级
   */
  private static final int MAX_LEVEL = 16;
  
  /**
   * 带头结点
   */
  private final Node head = new Node(MAX_LEVEL);
  
  private final Random random = new Random();
  
  /**
   * 当前索引层级数量
   */
  private int levelCount = 1;
  
  public static void main(String[] args) {
    SkipList list = new SkipList();
    list.insert(1);
    list.insert(2);
    list.insert(3);
    list.insert(4);
    list.insert(5);
    list.insert(6);
    list.insert(7);
    list.insert(8);
    list.printAll_beautiful();
    list.printAll();

    /*
    第15层索引值:null，下一个结点------->
    第14层索引值:null，下一个结点------->
    第13层索引值:null，下一个结点------->
    第12层索引值:null，下一个结点------->
    第11层索引值:null，下一个结点------->
    第10层索引值:null，下一个结点------->
    第9层索引值:null，下一个结点------->
    第8层索引值:5，下一个结点------->
    第7层索引值:5，下一个结点------->
    第6层索引值:5，下一个结点------->
    第5层索引值:8，下一个结点------->第5层索引值:5，下一个结点------->
    第4层索引值:5，下一个结点------->第4层索引值:3，下一个结点------->第4层索引值:8，下一个结点------->
    第3层索引值:3，下一个结点------->第3层索引值:2，下一个结点------->第3层索引值:5，下一个结点------->第3层索引值:6，下一个结点------->第3层索引值:7，下一个结点------->第3层索引值:8，下一个结点------->
    第2层索引值:2，下一个结点------->第2层索引值:3，下一个结点------->第2层索引值:4，下一个结点------->第2层索引值:5，下一个结点------->第2层索引值:6，下一个结点------->第2层索引值:7，下一个结点------->第2层索引值:8，下一个结点------->
    第1层索引值:2，下一个结点------->第1层索引值:3，下一个结点------->第1层索引值:4，下一个结点------->第1层索引值:5，下一个结点------->第1层索引值:6，下一个结点------->第1层索引值:7，下一个结点------->第1层索引值:8，下一个结点------->
    第0层索引值:1，下一个结点------->第0层索引值:2，下一个结点------->第0层索引值:3，下一个结点------->第0层索引值:4，下一个结点------->第0层索引值:5，下一个结点------->第0层索引值:6，下一个结点------->第0层索引值:7，下一个结点------->第0层索引值:8，下一个结点------->
    { data: 1; levels: 1 } { data: 2; levels: 4 } { data: 3; levels: 5 } { data: 4; levels: 3 } { data: 5; levels: 9 } { data: 6; levels: 4 } { data: 7; levels: 4 } { data: 8; levels: 6 }

     */

  }

  /**
   * 查找给定的元素所在
   *
   * @param value 元素
   * @return 找到则返回目标结点，否则返回 null
   */
  public Node find(int value) {
    Node p = head;
    // 从头结点开始，在最高层开始查找，找到前一个结点，再从下一层继续查找
    for (int i = levelCount - 1; i >= 0; i--) {
      // 判断 p 结点的第 i 层索引指向的下一个结点是否不为空，并且值小于目标值
      // 如果是，则获取该节点继续往下层查找，直到走完全部层
      while (p.forwards[i] != null && p.forwards[i].data < value) {
        p = p.forwards[i];
      }
    }
    // 此时已经从索引层最高下降到最底层，即第 0 层
    if (p.forwards[0] != null && p.forwards[0].data == value) {
      return p.forwards[0];
    } else {
      return null;
    }
  }

  /**
   * 插入元素
   *
   * @param value
   */
  public void insert(int value) {
    // 生成随机层数
    int level = head.forwards[0] == null ? 1 : randomLevel();
    // 每次只增加一层
    if (level > levelCount) {
      levelCount++;
    }

    Node newNode = new Node(level);
    newNode.data = value;

    // 记录要更新的层次，表示新节点要更新到哪几层
    Node[] update = new Node[level];
    for (int i = 0; i < level; i++) {
      update[i] = head;
    }

    Node p = head;
    // 查找插入结点的位置
    // 从头开始在最高层到底层往下查找元素插入的位置
    for (int i = levelCount - 1; i >= 0; i--) {
      while (p.forwards[i] != null && p.forwards[i].data < value) {
        p = p.forwards[i];
      }
      // 记录当前索引层的插入位置得元素
      // levelCount 可能会大于 level
      if (i < level) {
        update[i] = p;
      }
    }
    // 更新插入结点的位置信息，与新节点的
    // 执行插入
    for (int i = 0; i < level; i++) {
      newNode.forwards[i] = update[i].forwards[i];
      update[i].forwards[i] = newNode;
    }

  }

  public void insert2(int value) {
    int level = head.forwards[0] == null ? 1 : randomLevel();
    if (level > levelCount) {
      levelCount++;
    }
    Node newNode = new Node(level);
    newNode.data = value;

    Node p = head;
    for (int i = levelCount - 1; i > 0; i--) {
      while (p.forwards[i] != null && p.forwards[i].data < value) {
        p = p.forwards[i];
      }
      if (i < level) {
        // 直接修改索引层的结点，省去保存更新结点而创建临时数组创建的开销
        newNode.forwards[i] = p.forwards[i];
        p.forwards[i] = newNode;
      }

    }

  }

  public void delete(int value) {
    Node[] update = new Node[levelCount];
    Node p = head;

    for (int i = levelCount - 1; i >= 0; i--) {
      while (p.forwards[i] != null && p.forwards[i].data < value) {
        p = p.forwards[i];
      }
      update[i] = p;
    }

    if (p.forwards[0] != null && p.forwards[0].data == value) {
      for (int i = 0; i < update.length; i++) {
        if (update[i].forwards[i] != null && update[i].forwards[i].data == value) {
          update[i].forwards[i] = update[i].forwards[i].forwards[i];
        }

      }
    }

  }

  /**
   * 随机生成索引层数，如果是奇数层数 +1，防止伪随机
   *
   * @return 索引层
   */
  private int randomLevel() {
    int level = 1;
    for (int i = 1; i < MAX_LEVEL; i++) {
      // 百分之 50 的几率出现奇数
      if (random.nextInt() % 2 == 1) {
        level++;
      }
    }
    return level;
  }

  public void printAll() {
    Node p = head;
    while (p.forwards[0] != null) {
      System.out.print(p.forwards[0] + " ");
      p = p.forwards[0];
    }
    System.out.println();
  }
  
  /**
   * 打印所有数据
   */
  private void printAll_beautiful() {
    Node p = head;
    Node[] c = p.forwards;
    Node[] d = c;
    int maxLevel = c.length;
    for (int i = maxLevel - 1; i >= 0; i--) {
      do {
        System.out.print("第" + i + "层索引值:" + (d[i] != null ? d[i].data : null) + "，下一个结点------->");
      }
      while (d[i] != null && (d = d[i].forwards)[i] != null);
      System.out.println();
      d = c;
    }
  }
  
  private static class Node {
    
    /**
     * 表示当前结点在所有索引层置指向的下一个结点，索引从小到大表示层从小到大。
     * 比如 forwards[3]，表示当前结点在第 3 层索引层中指向的下一个结点。
     */
    private final Node[] forwards;
    
    private int data = -1;
    
    /**
     * 构建结点
     *
     * @param level 索引层级
     */
    Node(int level) {
      forwards = new Node[level];
    }
    
    @Override
    public String toString() {
      return "{ data: " + data + "; levels: " + forwards.length + " }";
    }

  }

}
