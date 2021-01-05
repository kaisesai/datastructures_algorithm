package com.kaige.datastructure.ch_08_stack;

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
    count = 0;
    data = (T[]) new Object[capacity];
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
    if (isFull()) {
      resizeStack(2 * capacity);
    }
    // 将数组尾部作为栈顶
    data[count++] = e;
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
    return data[--count];
  }

  /**
   * 栈容量是否为空
   *
   * @return 栈容量是否为空
   */
  public boolean isEmpty() {
    return count == 0;
  }

  /**
   * 栈是否已满
   *
   * @return 栈容量是否已满
   */
  public boolean isFull() {
    return data.length == capacity;
  }

  /**
   * 调整栈的大小
   *
   * @param size 栈大小
   */
  private void resizeStack(int size) {
    if (size <= count) {
      return;
    }
    // 数据搬移
    T[] newData = (T[]) new Object[size];
    for (int i = 0; i < count; i++) {
      newData[i] = data[i];
    }
    capacity = size;
    data = newData;
  }

  public void printAll() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < count; i++) {
      sb.append(data[i]).append(" ");
    }
    System.out.println(sb);
  }

}
