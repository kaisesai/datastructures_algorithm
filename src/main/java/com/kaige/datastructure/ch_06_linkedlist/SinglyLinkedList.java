package com.kaige.datastructure.ch_06_linkedlist;

/**
 * 6-1 单链表
 * 1）单链表的插入、删除与查找操作
 * 2）链表中存储的是 int 数据类型
 */
public class SinglyLinkedList {

  private Node head = null;

  public static void main(String[] args) {
    SinglyLinkedList linkedList = new SinglyLinkedList();
    // linkedList.insertTail(0);
    // linkedList.insertTail(0);
    // linkedList.insertTail(1);
    // linkedList.insertTail(2);
    // linkedList.insertTail(3);
    // linkedList.insertTail(4);
    // linkedList.insertTail(5);
    // linkedList.printAll();
    //
    // linkedList.insertToHead(6);
    // linkedList.printAll();
    // Node node = new Node(19, null);
    // linkedList.insertToHead(node);
    // linkedList.printAll();
    // linkedList.insertAfter(node, 20);
    // linkedList.printAll();
    //
    // Node byIndex = linkedList.findByIndex(2);
    // System.out.println(byIndex.data);
    //
    // linkedList.deleteByNode(node);
    // linkedList.printAll();
    //
    // linkedList.deleteByValue(3);
    // linkedList.printAll();
    //
    // linkedList.insertToHead(0);
    // linkedList.printAll();
    // linkedList.deleteRepeatedByValue(0);
    // linkedList.printAll();

    Node p = new Node(0, null);
    linkedList.insertToHead(p);
    linkedList.insertTail(1);
    linkedList.insertTail(2);
    linkedList.insertTail(3);
    linkedList.insertTail(2);
    linkedList.insertTail(1);
    linkedList.insertTail(1);
    // 判断是否回文
    // System.out.println(linkedList.palindrome());
    linkedList.printAll();

    // 带头结点的链表反转
    // Node node = linkedList.inverseLinkedList_head(p);
    // linkedList.printNode(node);

  }

  public void printNode(Node p) {
    StringBuilder sb = new StringBuilder("结点元素为：");
    Node q = p;
    while (q != null) {
      sb.append(q.data).append(" ");
      q = q.next;
    }
    System.out.println(sb);
  }

  /**
   * 判断链表字符串是否回文
   *
   * @return
   */
  public boolean palindrome() {
    if (head == null) {
      return false;
    }

    if (head.next == null) {
      System.out.println("只有一个元素");
      return true;
    }

    System.out.println("开始执行找中间结点");

    Node p = head;// 慢指针，每次前进一步
    Node q = head;// 块指针，每次前进两步

    while (q.next != null && q.next.next != null) {
      p = p.next;
      q = q.next.next;
    }

    System.out.println("中间结点 " + p.data);
    System.out.println("开始执行回文判断");

    Node leftLink = null;// 中间结点左侧链表
    Node rightLink = null;// 中间结点右侧链表

    if (q.next == null) {
      // p 一定是中间位置，链表数量为奇数
      rightLink = p.next;
      leftLink = inverseLinkedList(p).next;
    } else {
      // p q 均为中间结点
      // 链表数量为偶数
      rightLink = p.next;
      leftLink = inverseLinkedList(p);
    }

    return testResult(leftLink, rightLink);
  }
  
  /**
   * 判断两个链表值是否相同
   *
   * @param leftLink  第一个链表
   * @param rightLink 第二个链表
   * @return
   */
  private boolean testResult(Node leftLink, Node rightLink) {
    
    Node l = leftLink;
    Node r = rightLink;
    
    boolean flag = true;
    
    while (l != null && r != null) {
      if (l.data == r.data) {
        l = l.next;
        r = r.next;
      } else {
        flag = false;
        break;
      }
    }
    return flag;

  }

  /**
   * 无头结点的链表反转
   * <p>
   * 根据给定的尾结点来，反转链表，并返回一个头结点
   *
   * @param p 结点
   * @return 返回反转链表的结点
   */
  private Node inverseLinkedList(Node p) {
    // 将链表反转 lev，p 结点为 v，结果为 vel

    Node prev = null;// 前一个结点，用来反转链表
    Node r = head;
    Node next = null;
    while (r != p) {
      next = r.next;// 获取下一个结点
      r.next = prev;// 修改当前节点的 next 为前一个结点，即执行反转链表
      prev = r;// 记录当前节点为上个节点
      r = next;// 当前节点指向下一个结点
    }
    r.next = prev;
    // 返回左半部分的中点之前的那个结点
    // 从此处开始同步向两边比较
    // 注意此时，已经把原实结点的 head 的 nex 指针改为了 null 了。
    return r;
  }

  public Node inverseLinkedList_head(Node p) {
    // head 为新建的一个结点
    Node Head = new Node(9999, null);
    // p 为原来链表的头结点，现在 Head 指向头结点
    Head.next = p;

    // 带头结点的链表反转，实际上等于从第二个结点开始重头插法建立链表
    Node cur = p.next;
    p.next = null;
    Node next = null;

    // head -> a -> b -> c =====> head -> c -> b -> a
    while (cur != null) {
      next = cur.next;// 保存当前节点的下一个结点
      cur.next = Head.next;// 修改当前节点的下一个结点为上一个结点
      Head.next = cur;// 修改 Head 的下一个结点为当前结点
      cur = next;// 更新当前结点
    }

    return Head;

  }

  /**
   * 查找链表中指定的元素结点
   *
   * @param value 值
   * @return 元素结点，找不到就返回 null
   */
  public Node findByValue(int value) {
    Node p = head;
    while (p != null && p.data == value) {
      p = p.next;
    }
    return p;
  }

  /**
   * 根据索引查找元素
   *
   * @param index 索引
   * @return 元素，查不到就返回 null
   */
  public Node findByIndex(int index) {
    int count = 0;
    Node p = head;
    while (p != null && count != index) {
      count++;
      p = p.next;
    }

    return p;
  }

  /**
   * 无头结点
   * 表头插入
   * 这种操作将输入的顺序相反，逆序
   *
   * @param value 插入元素到头部
   */
  public void insertToHead(int value) {
    insertToHead(new Node(value, null));
  }
  
  /**
   * @param node
   */
  private void insertToHead(Node node) {
    if (head == null) {
      head = node;
    } else {
      // 将目前 head 的内存地址赋值给 node 的 next 变量
      node.next = head;
      // 将 node 的内存地址赋值给变量 head；
      head = node;
    }
  }
  
  /**
   * 插入元素到链尾
   *
   * @param value 元素值
   */
  private void insertTail(int value) {
    Node node = new Node(value, null);
    if (head == null) {
      head = node;
    } else {
      // 遍历查找链尾，并且找最后一个元素
      Node p = head;
      while (p.next != null) {
        p = p.next;
      }
      p.next = node;
    }
  }

  /**
   * 指定结点元素之后插入值为 value 的结点
   *
   * @param p     指定结点
   * @param value 值
   */
  public void insertAfter(Node p, int value) {
    insertAfter(p, new Node(value, null));
  }
  
  /**
   * 在目标结点之后插入新结点
   *
   * @param p       目标结点
   * @param newNode 新结点
   */
  private void insertAfter(Node p, Node newNode) {
    if (p == null) {
      return;
    }
    // 找到结点 p
    newNode.next = p.next;
    p.next = newNode;
  }

  /**
   * 在指定结点前插入元素
   *
   * @param p     指定结点
   * @param value 元素值
   */
  public void insertBefore(Node p, int value) {
    insertBefore(p, new Node(value, null));
  }
  
  /**
   * 在指定结点之前插入新结点
   *
   * @param p       指定结点
   * @param newNode 新结点
   */
  private void insertBefore(Node p, Node newNode) {
    if (p == null) {
      return;
    }
    // 需要找到结点 p 之前的结点
    
    // 只有一个头结点的情况
    if (p == head) {
      insertToHead(newNode);
      return;
    }

    Node q = head;
    while (q != null && q.next != p) {
      q = q.next;
    }

    // 找到 p 结点的前一个结点
    if (q != null) {
      newNode.next = p;
      q.next = newNode;
    }

  }

  /**
   * 删除指定结点
   *
   * @param p 指定结点
   * @return 是否删除成功
   */
  public boolean deleteByNode(Node p) {
    // 链表中没有元素时，返回 false
    if (head == null || p == null) {
      return false;
    }

    // 只是头结点
    if (head == p) {
      head = head.next;
      return true;
    }

    // 查找 p 结点的前一个节点
    Node q = head;
    while (q != null && q.next != p) {
      q = q.next;
    }
    if (q == null) {
      return false;
    }
    q.next = q.next.next;
    return true;
  }

  /**
   * 删除元素值为 value 值的结点
   *
   * @param value 元素值
   * @return 是否删除成功
   */
  public boolean deleteByValue(int value) {
    // 空结点的情况
    if (head == null) {
      return false;
    }
    // 只有头结点的情况
    if (head.data == value) {
      head = null;
      return true;
    }

    Node p = head;
    while (p.next != null && p.next.data != value) {
      p = p.next;
    }

    // 说明找到目标值的前一个元素，已经处于链表尾部
    if (p.next == null) {
      return false;
    }
    p.next = p.next.next;
    return true;
  }

  /**
   * 删除链表中所有值为 value 的结点
   *
   * @param value 元素值
   * @return 是否删除成功
   */
  public boolean deleteRepeatedByValue(int value) {

    if (head == null) {
      return false;
    }

    boolean result = false;
    Node p = head;
    Node q = null;
    while (p != null) {
      if (p.data == value) {

        if (q == null) {
          // 删除的结点时头结点
          head = head.next;
          q = null;
          p = p.next;
        } else {
          q.next = p.next;
          // q = p.next;
          p = p.next;
        }
        result = true;
        continue;
      }
      q = p;
      p = p.next;
    }
    // TODO: 2020/3/20 测试
    return result;
  }

  public void printAll() {
    Node p = head;
    StringBuilder sb = new StringBuilder("单链表的元素为：");
    while (p != null) {
      sb.append(p.data).append(" ");
      p = p.next;
    }
    System.out.println(sb);
  }
  
  static class Node {
    
    private final int data;
    
    private Node next;
    
    Node(int data, Node next) {
      this.data = data;
      this.next = next;
    }
    
    public int getData() {
      return data;
    }
    
  }

}
