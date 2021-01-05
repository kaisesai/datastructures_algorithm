package com.kaige.alog.test.mianshi;

import lombok.Data;
import org.junit.Test;

/**
 * 链表的练习
 * <p>
 * 熟练掌握 5 个精选的常见的链表操作：
 * 1. 单链表反转
 * 2. 链表中环的检测
 * 3. 两个有序的链表合并（利用技巧三：哨兵结点）
 * 4. 删除链表倒数第 n 个结点（利用技巧三：哨兵结点）
 * 5. 求链表的中间结点
 * 6. 判断字符串是否回文字符串
 */
public class LinkedListTest_06 {
  
  private static void printNode(Node node) {
    if (node == null) {
      System.out.println("不打印空的链表");
      return;
    }
    Node p = node;
    
    StringBuilder sb = new StringBuilder();
    while (p != null) {
      sb.append(p.data);
      if (p.next != null) {
        sb.append(" -> ");
      }
      p = p.next;
    }
    System.out.println("打印列表：" + sb.toString());
    
  }
  
  @Test
  public void testLinkedList() {
    // Node n5 = new Node(9, null);
    Node n4 = new Node(7, null);
    Node n3 = new Node(5, n4);
    Node n2 = new Node(3, n3);
    Node n1 = new Node(1, n2);
    
    // LinkedListTest_06.printNode(n1);
    // 1. 单链表的反转
    // Node n6 = test_01_Reverse(n1);
    // LinkedListTest_06.printNode(n6);
    
    // 2. 检测链表中环
    // n5.next = n3;
    // boolean checkRing = test_02_checkRing(n1);
    // System.out.println("链表中是否有环：" + checkRing);
    
    // Node nn5 = new Node(8, null);
    // Node nn4 = new Node(6, nn5);
    // Node nn3 = new Node(4, nn4);
    // Node nn2 = new Node(2, nn3);
    // Node nn1 = new Node(0, nn2);
    
    // LinkedListTest_06.printNode(n1);
    // LinkedListTest_06.printNode(nn1);
    // 3. 合并两个有序链表
    // Node nn = mergeSortedLinkedList(n1, nn1);
    // LinkedListTest_06.printNode(nn);
    
    // 4. 删除链表倒数第 k 个元素
    // LinkedListTest_06.printNode(n1);
    // Node node = deleteLastKNode(n1, 5);
    // LinkedListTest_06.printNode(node);
    
    // 5. 求链表的中间结点
    LinkedListTest_06.printNode(n1);
    Node node = findMidNode(n1);
    LinkedListTest_06.printNode(node);
  }
  
  // 5. 求链表的中间结点
  private Node findMidNode(Node node) {
    // 定义两个指针，快指针和慢指针
    Node quick = node;
    Node small = node;
    
    while (quick != null && quick.next != null) {
      small = small.next;
      quick = quick.next.next;
    }
    
    return small;
  }
  
  // 4. 删除链表倒数第 k 个指针
  private Node deleteLastKNode(Node node, int k) {
    // 删除倒数第 k 个元素，就是删除正数 n -k 个元素。即 1，2，3，4，5 个元素，删除倒数第 2 个元素，那就是说，要删除
    // 正数第 4 个元素，即索引是 3 的元素，5-2 --> n - k
    
    if (k == 0) {
      return node;
    }
    
    // 获取链表的个数
    int n = 0;
    Node p = node;
    while (p != null) {
      n++;
      p = p.next;
    }
    
    if (n == 0) {
      return null;
    }
    
    // 计算正数元素索引，n - k
    int dn = n - k;
    if (dn < 0) {
      return null;
    }
    
    if (dn == 0) {
      return node.next;
    }
    
    p = node;
    // 获取第 n - k - 1 的索引，用于删除元素
    for (int i = 1; i < dn; i++) {
      p = p.next;
    }
    
    if (p == null) {
      return null;
    }
    
    // 删除下一个结点
    p.next = p.next.next;
    
    return node;
  }
  // 链表的基本操作，新、删、查、遍历
  
  // 3. 两个有序链表的合并
  private Node mergeSortedLinkedList(Node node1, Node node2) {
    
    Node p = node1;
    Node q = node2;
    
    // 1,3,5,7,9
    // 0,2,4,6,8
    // 0,1,2,3,4,5,6,7,8,9
    
    // 哨兵节点
    Node n = new Node(-1, null);
    Node c = n;
    
    while (p != null && q != null) {
      if (p.data < q.data) {
        c.next = p;
        p = p.next;
      } else {
        c.next = q;
        q = q.next;
      }
      c = c.next;
    }
    
    // p 结点没有遍历完毕
    if (p != null) {
      c.next = p;
    }
    
    // q 结点没有遍历完毕
    if (q != null) {
      c.next = q;
    }
    
    return n.next;
  }
  
  // 2. 链表中环的检测
  private boolean test_02_checkRing(Node node) {
    // 定义三个变量
    if (node == null || node.next == null) {
      return false;
    }
    // 定义快慢指针，慢指针遍历一遍后，快指针不为 null 就说明是链表中有环存在
    Node quick = node;
    Node slow = node;
    while (quick.next != null) {
      slow = slow.next;
      quick = quick.next.next;
      // 判断快慢指针是否相撞
      if (quick == slow) {
        return true;
      }
    }
    return false;
  }
  
  // 1. 单列表的反转
  private Node test_01_Reverse(Node node) {
    // 定义三个变量
    if (node == null || node.next == null) {
      return node;
    }
    // 当前结点
    Node p = node;
    // 上一个结点
    Node prev = null;
    // 下一个结点
    Node next;
    
    // null->1->2->3->4->5 ---> null<-1<-2<-3<-4<-5
    while (p != null) {
      // 临时保存当前结点的下一个结点
      next = p.next;
      // 将当前结点的下一个结点指向上一个结点
      p.next = prev;
      // 将上一个结点索引指向当前结点
      prev = p;
      // 将当前结点指向临时保存的下一个结点
      p = next;
    }
    
    // 技巧，定义是三个指针变量，当前结点 p、上一个结点 prev、下个结点 next
    // 1. 先临时保存当前结点的下一个结点到 next 指针
    // 2. 将当前结点 p 指针下一个结点的索引变为它的前一个结点 prev 指针值
    // 3. 将前一个结点 prev 指针索引变为当前结点 p 的指针
    // 4. 将当前结点 p 指针指向下一个结点 next 的值
    // 5. 返回上一个结点的指针 prev
    // 注意，while 循环条件为 p != null
    
    return prev;
  }
  
  // 结点
  @Data
  public static class Node {
    
    // 数据
    private int data;
    
    // 下一个结点
    private Node next;
    
    Node(int data, Node next) {
      this.data = data;
      this.next = next;
    }
    
    @Override
    public String toString() {
      return "Node{" + "data=" + data + ", next=" + next + '}';
    }
    
  }
  
}
