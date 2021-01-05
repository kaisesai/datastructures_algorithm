package com.kaige.datastructure.ch_09_queue;

/**
 * 9-2-2 基于链表实现的队列——链式队列
 * <p>
 * 头尾指针使用哨兵节点的实现方式
 * <p>
 * head 指针指向一个哨兵结点
 */
public class QueueBasedOnLInkedList2<T> {
  
  /**
   * 队列容量
   */
  private final int capacity;
  
  /**
   * 头指针
   */
  private Node<T> head;
  
  /**
   * 尾指针
   */
  private Node<T> tail;
  
  /**
   * 队列元素个数
   */
  private int count;

  public QueueBasedOnLInkedList2(int capacity) {
    this.capacity = capacity;
    // 初始化头尾指针
    tail = head = new Node<>(null, null);
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
    // 刚开始，队列中没有元素时，头尾指针指向的都是一个哨兵结点，修改了 tail.next，也就修改了 head.next 指针
    tail = tail.next = node;
    count++;
    return true;
  }

  /**
   * 出队操作
   *
   * @return 队头元素
   */
  public T dequeue() {
    if (isEmpty()) {
      return null;
    }
    Node<T> h = head;// 记录哨兵结点
    Node<T> first = h.next;// 记录真正的第一个结点
    // help GC，这时候 h 已经没有用了，切断 h 对象与其他对象的联系，加速 JVM 垃圾收集，其实使用 h.next= null 也可以完成相同的效果
    h.next = h;

    head = first;// 将第一个结点赋值给 head 指针
    T x = first.data;
    first.data = null;// 将 head 结点编程哨兵结点
    count--;
    return x;
  }

  /**
   * 队列是否为空
   *
   * @return 是否为空
   */
  public boolean isEmpty() {
    return count == 0;
  }

  /**
   * 队列是否已满
   *
   * @return 是否已满
   */
  public boolean isFull() {
    return count == capacity;
  }

  public void printAll() {
    if (isEmpty()) {
      System.out.println("队列为空");
      return;
    }
    StringBuilder sb = new StringBuilder();
    Node<T> p = head.next;
    while (p != null) {
      sb.append(p.data).append(" ");
      p = p.next;
    }
    System.out.println(sb);
  }
  
  static class Node<T> {
    
    private T data;
    
    private Node<T> next;
    
    Node(T data, Node<T> next) {
      this.data = data;
      this.next = next;
    }
    
  }

}
