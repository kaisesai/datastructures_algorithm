package com.kaige.datastructure.ch_08_stack;

/**
 * 8-2 基于链表实现的栈——链表栈
 */
public class StackBasedOnLinkeList<T> {

  /**
   * 栈顶
   */
  private Node<T> top;

  /**
   * 栈中的元素数量
   */
  private int count;

  public static void main(String[] args) {
    StackBasedOnLinkeList<Integer> stack = new StackBasedOnLinkeList<>();
    stack.push(0);
    stack.push(1);
    stack.push(2);
    stack.push(3);
    stack.printAll();
    Integer pop = stack.pop();
    System.out.println("pop：" + pop);
    stack.printAll();
    stack.push(100);
    stack.printAll();

    pop = stack.pop();
    System.out.println("pop：" + pop);
    stack.printAll();
    pop = stack.pop();
    System.out.println("pop：" + pop);
    stack.printAll();
    pop = stack.pop();
    System.out.println("pop：" + pop);
    stack.printAll();
  }
  
  /**
   * 入栈操作
   * <p>
   * 将元素插入栈顶
   *
   * @param e 元素
   */
  void push(T e) {
    // 将元素插入到栈顶插入元素
    Node<T> newNode = new Node<>(e, null);
    if (top == null) {
      top = newNode;
    } else {
      newNode.next = top;
      top = newNode;
    }
    count++;
  }
  
  /**
   * 出栈操作
   * <p>
   * 移出栈顶元素并返回
   *
   * @return 栈顶元素
   */
  T pop() {
    // 移出栈顶（头部）元素
    if (top == null) {
      return null;
    }
    Node<T> deletedNode = top;
    top = top.next;
    count--;
    return deletedNode.data;
  }

  /**
   * 栈是否为空
   *
   * @return 是否为空
   */
  public boolean isEmpty() {
    return count == 0;
  }

  public void printAll() {
    if (top == null) {
      return;
    }
    StringBuilder sb = new StringBuilder("栈中元素数量：" + count + "，元素为：");
    Node<T> p = top;
    while (p != null) {
      sb.append(p.data).append(" ");
      p = p.next;
    }
    System.out.println(sb);
  }
  
  static class Node<T> {
    
    private final T data;
    
    private Node<T> next;
    
    Node(T data, Node<T> next) {
      this.data = data;
      this.next = next;
    }
    
  }

}
