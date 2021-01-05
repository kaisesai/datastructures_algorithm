package com.kaige.datastructure.ch_09_queue;

/**
 * 9-1 基于数组实现的队列——顺序队列
 */
public class ArrayQueue<T> {

  /**
   * 默认的容量
   */
  private static final int DEFAULT_CAPACITY = 10;

  /**
   * 元素数据
   */
  T[] items;

  /**
   * 队列中元素的数量
   */
  int count;

  /**
   * 头指针
   */
  int head;

  /**
   * 尾指针
   */
  int tail;

  /**
   * 队列容量
   */
  int capacity;

  /**
   * 构建指定容量的队列
   *
   * @param capacity
   */
  private ArrayQueue(int capacity) {
    this.capacity = capacity;
    items = (T[]) new Object[capacity];
    count = 0;
    head = 0;
    tail = 0;
  }

  /**
   * 构建默认容量的队列
   */
  ArrayQueue() {
    this(DEFAULT_CAPACITY);
  }

  public static void main(String[] args) {
    ArrayQueue<String> queue = new ArrayQueue<>();
    queue.printAll();
    queue.enqueue("a");
    queue.enqueue("b");
    queue.enqueue("c");
    queue.printAll();
    queue.dequeue();
    queue.printAll();
    queue.dequeue();
    queue.printAll();
    queue.dequeue();
    queue.printAll();

  }

  /**
   * 入队操作
   *
   * @param item 元素
   * @return 入队是否成功
   */
  public boolean enqueue(T item) {
    // 队列已满不允许操作
    if (isFull()) {
      System.out.println("队列已满，无法执行入队操作！");
      return false;
    }
    items[tail] = item;
    // 这个操作相比判断是否等于 head 值更加简单
    tail = ++tail % capacity;
    // if (++tail == this.capcacity) {
    //   tail = 0;
    // }
    count++;
    return true;
  }

  /**
   * 出队操作
   *
   * @return 返回队头的元素
   */
  public T dequeue() {
    if (isEmpty()) {
      System.out.println("队列为空，无法执行出队操作");
      return null;
    }
    T item = items[head];
    head = ++head % capacity;
    count--;
    return item;
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
  boolean isFull() {
    return count == capacity;
  }

  public void printAll() {
    if (isEmpty()) {
      System.out.println("队列为空");
      return;
    }
  
    StringBuilder sb = new StringBuilder();
    int k = count;

    for (int i = head; ; ) {
      sb.append(items[i]).append(" ");
      i = ++i % capacity;
      if (--k == 0) {
        break;
      }
    }
    System.out.println(sb);
  }

}
