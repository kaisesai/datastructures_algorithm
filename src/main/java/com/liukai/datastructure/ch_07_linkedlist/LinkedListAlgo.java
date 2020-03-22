package com.liukai.datastructure.ch_07_linkedlist;

/**
 * 7-1 链表训练
 * <p>
 * 1）单链表反转
 * 2）链表中环的检测
 * 3）两个有序链表的合并
 * 4）删除链表倒数第 n 个结点
 * 5）求链表的中间结点
 */
public class LinkedListAlgo {

  /**
   * 1. 单链表的反转
   *
   * @param node 结点
   * @return 反转之后的链表头结点
   */
  public static Node reverse(Node node) {
    if (node == null) {
      return null;
    }
    // 当前结点
    Node cur = node;
    // 前一个结点
    Node prev = null;
    // 下一个结点
    Node next = null;
    while (cur != null) {
      // 记录下一个结点
      next = cur.next;
      // 修改当前结点的 next 结点指向
      cur.next = prev;
      // 修改前一个结点的指向
      prev = cur;
      // 修改当前结点为下一个结点
      cur = next;
    }
    return prev;
  }

  /**
   * 2. 检测一个链表中环
   * <p>
   * 中环即链表尾部结点指向链表中的某个结点，尾结点与指向的结点形成一个内环。
   *
   * @param list 链表
   * @return 是否为环形
   */
  public static boolean check(Node list) {
    if (list == null) {
      return false;
    }
    // 定义一个快指针，前进两步
    Node fast = list;
    // 定义一个慢指针，前进一步
    Node slow = list;
    while (fast.next != null) {
      fast = fast.next.next;
      slow = slow.next;
      // 快慢指针相遇时，说明链表有中环
      if (fast.data == slow.data) {
        return true;
      }
    }
    return false;

  }

  /**
   * 3. 合并两个有序链表（力扣 21 题）
   *
   * @param l1 第一个链表
   * @param l2 第二个链表
   * @return 合并之后的链表头结点
   */
  public static Node mergeTwoLists(Node l1, Node l2) {
    // 技巧三：利用哨兵结点简化难度
    Node solder = new Node(-1, null);
    Node p = solder;

    while (l1 != null && l2 != null) {
      // 比较两个结点的最小结点
      if (l1.data < l2.data) {
        // 说明 l1 结点的元素小
        p.next = l1;
        l1 = l1.next;
      } else {
        // 否则 l2 结点元素小
        p.next = l2;
        l2 = l2.next;
      }
      p = p.next;
    }

    if (l1 != null) {
      // l1 还有剩余结点
      p.next = l1;
    }
    if (l2 != null) {
      // l2 还有剩余结点
      p.next = l2;
    }
    return solder.next;
  }

  /**
   * 删除链表倒数第 n 个结点
   * <p>
   * 力扣 19 题
   *
   * @param head 链表头结点
   * @param n    倒数第 n 个元素
   * @return 删除指定结点之后的链表的头结点
   */
  public static Node removeNthFromEnd(Node head, int n) {
    // 技巧三：利用哨兵结点简化编程难度
    Node solder = new Node(0, null);
    solder.next = head;

    // 统计链表的长度
    int length = 0;
    Node first = head;
    while (first != null) {
      length++;
      first = first.next;
    }

    // 查找删除结点的前一个结点，即链表的第 l-n 个结点
    length -= n;
    first = solder;
    while (first != null && length > 0) {
      length--;
      first = first.next;
    }
    if (first != null && first.next != null) {
      first.next = first.next.next;
    }

    return solder.next;
  }

  /**
   * 4. 删除链表倒数第 k 个节点
   *
   * @param list 链表
   * @param k    第 k 个元素，k 从 1 开始
   * @return 返回删除的结点
   */
  public static Node deleteLastKth(Node list, int k) {
    if (list == null) {
      return null;
    }

    // 先找出链表的总结点数
    Node p = list;
    int i = 0;
    while (p != null) {
      p = p.next;
      i++;
    }

    // 倒数第 k 个节点，即索引为 n-k 的结点，索引从 0开始，n 为链表的长度
    // 找到第 n-k -1 个节点
    int targetIndex = i - k;
    if (targetIndex < 0) {
      // 无效的 k
      return null;
    }

    Node target = null;
    if (targetIndex == 0) {
      // 说明是头结点元素
      target = list;
      list = list.next;
    } else {
      // 不是头结点元素，需要获取前一个结点
      Node prev = list;
      int prevIndex = 0;
      while (prev != null) {
        if (prevIndex == (targetIndex - 1)) {
          break;
        }
        prev = prev.next;
        prevIndex++;
      }
      if (prev.next != null) {
        target = prev.next;
        prev.next = prev.next.next;
      }
    }

    if (target != null) {
      // 删除 target 结点的下一个结点元素
      target.next = null;
    }

    return target;

  }

  /**
   * 5. 求链表的中间结点
   *
   * @param list 链表头结点
   * @return 链表的中间结点
   */
  public static Node findMiddleNode(Node list) {
    if (list == null) {
      return null;
    }
    Node fast = list;
    Node slow = list;
    while (fast != null && fast.next != null) {
      fast = fast.next.next;
      slow = slow.next;
    }

    // 链表元素个数为奇数个，slow 是链表的中间结点
    // 链表元素为偶数个，slow 为 链表元素总数/2 -1 索引位置
    return slow;
  }

  public static void main(String[] args) {
    Node node = new Node(0, null);
    Node node1 = new Node(1, null);
    Node node2 = new Node(2, null);
    Node node3 = new Node(3, null);

    node.next = node1;
    node1.next = node2;
    node2.next = node3;

    // 1. 反转链表
    // Node reverse = reverse(node);
    // System.out.println("反转链表 ↓↓↓↓↓↓↓↓↓↓");
    // printNode(reverse);
    // 2. 检查是否有中环
    printNode(node);
    // node3.next = node1;
    // System.out.println(check(node));
    // 3. 合并两个有序结点
    // Node node00 = new Node(0, null);
    // Node node01 = new Node(2, null);
    // Node node02 = new Node(4, null);
    // Node node03 = new Node(6, null);
    // Node node04 = new Node(100, null);
    // node00.next = node01;
    // node01.next = node02;
    // node02.next = node03;
    // node03.next = node04;
    // printNode(node00);
    // Node mergedNode = mergeTwoLists(node, node00);
    // printNode(mergedNode);

    // 4. 找出结点的中间结点
    // Node middleNode = findMiddleNode(node);
    // printNode(middleNode);

    // 5. 删除链表中倒数第 k 个元素
    // Node deletedNode = deleteLastKth(node, 1);
    // 5.2 删除链表中第 n 个结点，并返回头链表结点
    Node deletedNode = removeNthFromEnd(node, 1);
    printNode(deletedNode);

  }

  public static void printNode(Node p) {
    if (p == null) {
      System.out.println("null");
      return;
    }
    StringBuilder sb = new StringBuilder(String.valueOf(p.data));
    Node q = p.next;
    while (q != null) {
      sb.append("->").append(q.data);
      q = q.next;
    }
    System.out.println(sb);
  }

  static class Node {

    private int data;

    private Node next;

    public Node(int data, Node next) {
      this.data = data;
      this.next = next;
    }

  }

}
