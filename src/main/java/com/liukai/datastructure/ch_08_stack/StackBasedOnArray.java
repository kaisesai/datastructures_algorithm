package com.liukai.datastructure.ch_08_stack;

/**
 * 8-1 基于数组实现的栈——顺序栈
 * <p>
 * 特性：
 * 1. 先进后出，后进先出
 * 2. 支持动态扩容
 */
public class StackBasedOnArray<T> {

  private static final int DEFAULT_CAPACITY = 10;

  private T[] data;// 数组

  private int count;// 栈中元素的个数

  private int capacity;// 栈的大小

  /**
   * 构造指定大小的栈
   *
   * @param capacity 栈的长度
   */
  public StackBasedOnArray(int capacity) {
    this.capacity = capacity;
    this.count = 0;
    this.data = (T[]) new Object[capacity];
  }

  /**
   * 构造默认栈
   */
  public StackBasedOnArray() {
    this(DEFAULT_CAPACITY);
  }

  public static void main(String[] args) {
    StackBasedOnArray<Integer> stack = new StackBasedOnArray<>();
    stack.push(1);
    Integer pop = stack.pop();
    System.out.println("pop: " + pop);
    stack.printAll();
    stack.push(2);
    stack.push(3);
    stack.printAll();
    pop = stack.pop();
    System.out.println("pop: " + pop);
    pop = stack.pop();
    System.out.println("pop: " + pop);
    stack.printAll();
  }

  /**
   * 入栈操作
   * <p>
   * 数据后进先出，位于栈顶
   *
   * @param e 元素
   */
  public void push(T e) {
    // 是否需要扩容
    if (this.isFull()) {
      this.resizeStack(2 * this.capacity);
    }
    // 将数组尾部作为栈顶
    this.data[this.count++] = e;
  }

  /**
   * 出栈操作
   * <p>
   * 从栈顶弹出一个元素
   *
   * @return 返回栈顶元素
   */
  public T pop() {
    if (isEmpty()) {
      return null;
    }
    return this.data[--count];
  }

  /**
   * 栈容量是否为空
   *
   * @return 栈容量是否为空
   */
  public boolean isEmpty() {
    return this.count == 0;
  }

  /**
   * 栈是否已满
   *
   * @return 栈容量是否已满
   */
  public boolean isFull() {
    return this.data.length == this.capacity;
  }

  /**
   * 调整栈的大小
   *
   * @param size 栈大小
   */
  private void resizeStack(int size) {
    if (size <= this.count) {
      return;
    }
    // 数据搬移
    T[] newData = (T[]) new Object[size];
    for (int i = 0; i < this.count; i++) {
      newData[i] = data[i];
    }
    this.capacity = size;
    this.data = newData;
  }

  public void printAll() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < this.count; i++) {
      sb.append(this.data[i]).append(" ");
    }
    System.out.println(sb);
  }

}
