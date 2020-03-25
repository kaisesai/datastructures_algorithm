package com.liukai.datastructure.ch_09_queue;

/**
 * 9-2-1 基于链表实现的队列——链式队列
 * <p>
 * 头尾指针不使用哨兵节点的实现方式
 */
public class QueueBasedOnLInkedList<T> {

  /**
   * 头指针
   */
  private Node<T> head;

  /**
   * 尾指针
   */
  private Node<T> tail;

  /**
   * 队列容量
   */
  private int capacity;

  /**
   * 队列元素个数
   */
  private int count;

  public QueueBasedOnLInkedList(int capacity) {
    this.capacity = capacity;
  }

  /**
   * 出队操作
   *
   * @return 队头元素
   */
  public T dequeue() {
    // 头指针为空说明队列没有元素
    if (head == null) {
      return null;
    }
    T item = this.head.data;
    this.head = this.head.next;
    this.count--;
    return item;
  }

  /**
   * 入队操作
   *
   * @param item 元素
   * @return 入队是否成功
   */
  public boolean enqueue(T item) {
    // 头指针和尾指针都指向了默认值，当第一次入队操作时，修改 tail 指针也就修改了 head 指针
    Node<T> node = new Node<>(item, null);
    if (tail == null) {
      // 数据首次入队，说明头结点和尾结点都为空，需要初始化
      head = tail = node;
    } else {
      // 否则直接修改尾结点
      tail.next = node;
      tail = node;
    }
    this.count++;
    return true;
  }

  /**
   * 队列是否为空
   *
   * @return 是否为空
   */
  public boolean isEmpty() {
    return this.count == 0;
  }

  /**
   * 队列是否已满
   *
   * @return 是否已满
   */
  public boolean isFull() {
    return this.count == this.capacity;
  }

  public void printAll() {
    if (isEmpty()) {
      System.out.println("队列为空");
      return;
    }
    StringBuilder sb = new StringBuilder();
    Node<T> p = head;
    while (p != null) {
      sb.append(p.data).append(" ");
      p = p.next;
    }
    System.out.println(sb);
  }

  static class Node<T> {

    private T data;

    private Node<T> next;

    public Node(T data, Node<T> next) {
      this.data = data;
      this.next = next;
    }

  }

}
