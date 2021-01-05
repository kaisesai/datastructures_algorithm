package com.kaige.datastructure.ch_07_linkedlist;

public class TestAli {
  
  //1.将head链表以m为组反转链表(不足m则不反转)：
  //例子：假设m=3. 链表 1 -> 2 -> 3 -> 4 -> 5 -> 6 -> 7 ->8
  //output: 3 -> 2 -> 1 ->6

/*
	思路：
    1. 根据给定的 m 先反转指定的 m 个数
    2. 在遍历过程中反转数，反转结束，同时需要继续前进 m 个数，找到对应的数
*/
  
  public static void main(String[] args) {
    Node n1 = new Node(1);
    Node n2 = new Node(2);
    Node n3 = new Node(3);
    Node n4 = new Node(4);
    Node n5 = new Node(5);
    Node n6 = new Node(6);
    Node n7 = new Node(7);
    Node n8 = new Node(8);
    
    n1.next = n2;
    n2.next = n3;
    n3.next = n4;
    n4.next = n5;
    n5.next = n6;
    n6.next = n7;
    n7.next = n8;
    
    f(n1, 3);
    
  }
  
  private static void f(Node head, int m) {
    if (m == 0) {
      return;
    }
    if (head.next == null) {
      return;
    }
    // 遍历一遍，记录链表的个数
    int num = 0;
    Node p = head;
    while (p != null) {
      num++;
      p = p.next;
    }
    // 不满足 m 个数
    if (num < m) {
      return;
    }
    
    // 遍历的当前节点
    Node cur = head;
    // 上个节点
    Node pre = null;
    // 下一个节点
    Node next = null;
    
    Node t;
    for (int i = 0; i < num; i++) {
      // 需要判断链表长度
      // 更新 p 节点的数据
      // 1 -> 2 -> 3 -> ...
      // null <- 1 <- 2 <-3
      if (i < m) {
        next = cur.next;
        // 修改的节点
        cur.next = pre;
        // 修改前一个节点
        pre = cur;
        cur = next;
      } else {
        
        // 如果当前遍历的值到了 2m 个数，则把 2*m 的节点数据放过来
        if (i == 2 * m - 1) {
          t = cur.next;
          t.next = null;
          // cur.next = pre;
          break;
        }
        
        cur = cur.next;
      }
      
    }
    
    // 将尾结点插入新的结果节点
    
    Node result = pre;
    while (result != null) {
      System.out.print(result.value + "->");
      result = result.next;
    }
    if (cur != null) {
      System.out.print(cur.value);
    }
  }
  
  public static class Node {
    
    int value;
    
    Node next;
    
    public Node(int value) {
      this.value = value;
    }
    
    public Node(int value, Node next) {
      this.value = value;
      this.next = next;
    }
    
  }
  
}
