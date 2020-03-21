package com.liukai.datastructure.ch_06_linkedlist;

import lombok.Data;

import java.util.Objects;

/**
 * 6-2 基于单链表实现的 LRU（最近最少使用策略） 算法
 * <p>
 * LRU 算法思路：
 * 1. 链表中头部的元素是最新访问的，越接近链表尾部的数据访问时间越旧
 * 2. 当有新的元素被访问时，遍历链表，如果链表中存在该元素，则将原来的所在位置的结点删除，并插入到链表头部。
 * 3. 当没有元素被访问时，又分两种情况：
 * - 链表已满，则删除链表尾部结点，将新元素插入到链表头部
 * - 链表未满，将元素插入到链表头部
 */
public class LRUBaseLinkedList<T> {

  /**
   * 链表的默认容量
   */
  private static final int DEFAULT_CAPACITY = 10;

  /**
   * 链表长度
   */
  private int length;

  /**
   * 链表容量
   */
  private int capacity;

  /**
   * 头结点
   */
  private SNode<T> headNode = new SNode<>();

  /**
   * 构建默认容量的的缓存
   * <p>
   * 默认容量为：LRUBaseLinkedList#DEFAULT_CAPACITY
   */
  public LRUBaseLinkedList() {
    this.capacity = DEFAULT_CAPACITY;
    this.length = 0;
  }

  /**
   * 构建指定容量的缓存
   *
   * @param capacity 链表容量
   */
  public LRUBaseLinkedList(int capacity) {
    this.capacity = capacity;
    this.length = 0;
  }

  public static void main(String[] args) {
    LRUBaseLinkedList<String> cache = new LRUBaseLinkedList<>();
    cache.add("a");
    cache.add("b");
    cache.add("c");
    cache.printAll();

    cache.add("a");
    cache.printAll();

  }

  public void add(T data) {
    // 查找执行元素的前一个元素
    SNode<T> prevNode = findPrevNode(data);

    if (prevNode == null) {
      // 元素不在链表中
      if (this.length == this.capacity) {
        // 容量已满，则先删除链表最后一个结点，再插入元素到链表头部
        this.deleteElementAtEnd();
      }
    } else {
      // 元素找到了，则先删除元素结点，再在头部插入元素
      this.deleteElemOptim(prevNode);
    }
    // 将元素插入到头结点
    intsertElemAtBegin(data);

  }

  /**
   * 删除 preveNode 结点的下一个结点
   *
   * @param prevNode 结点
   */
  private void deleteElemOptim(SNode prevNode) {
    prevNode.next = prevNode.next.next;
    this.length--;
  }

  /**
   * 链表头部插入元素
   *
   * @param data 元素
   */
  private void intsertElemAtBegin(T data) {
    // 在链表头部插入元素，需要修改 head 的 next
    SNode<T> node = headNode.next;
    SNode<T> p = new SNode<>(data, node);
    this.headNode.next = p;
    this.length++;
  }

  /**
   * 获取查找元素的前一个结点
   *
   * @param data 元素
   * @return 元素的前一个结点
   */
  private SNode<T> findPrevNode(T data) {
    SNode<T> p = headNode;

    // head->a
    while (p.next != null && !Objects.equals(p.next.element, data)) {
      p = p.next;
    }

    // 没有找到
    if (p.next == null) {
      return null;
    }
    return p;
  }

  /**
   * 删除尾部结点
   */
  private void deleteElementAtEnd() {

    // 遍历到结点尾部
    SNode<T> p = headNode;
    while (p.next != null && p.next.next != null) {
      p = p.next;
    }
    // head->a->b->c
    if (p.next == null) {
      return;
    }

    p.next = null;
    this.length--;
  }

  public void printAll() {
    StringBuilder sb = new StringBuilder("缓存中的元素为：");
    SNode<T> p = headNode.next;
    while (p != null) {
      sb.append(p.element.toString()).append(" ");
      p = p.next;
    }
    System.out.println(sb);
  }

  /**
   * 链表结点
   *
   * @param <T> 泛型
   */
  @Data
  static class SNode<T> {

    private T element;

    private SNode<T> next;

    public SNode(T element, SNode<T> next) {
      this.element = element;
      this.next = next;
    }

    public SNode() {
    }

  }

}
