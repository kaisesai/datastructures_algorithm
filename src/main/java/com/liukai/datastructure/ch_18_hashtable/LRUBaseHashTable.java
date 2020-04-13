package com.liukai.datastructure.ch_18_hashtable;

import java.util.HashMap;

/**
 * 18-2 基于哈希表的 LRU 缓存淘汰算法
 * <p>
 * 特点：
 * 1. 查找时间复杂度为 O(1)
 * 2. 淘汰最近最少使用的元素
 * 3. 基于哈希表与链表实现
 */
public class LRUBaseHashTable<K, V> {

  /**
   * 默认的初始化容量
   */
  public static final int DEFAULT_INITIAL_CAPACITY = 10;

  /**
   * 头结点
   */
  private final DNode<K, V> head;

  /**
   * 尾结点
   */
  private final DNode<K, V> tail;

  /**
   * 哈希表，记录键
   */
  private final HashMap<K, DNode<K, V>> table;

  /**
   * 链表容量
   */
  private final int capacity;

  /**
   * 链表长度
   */
  private int length = 0;

  public LRUBaseHashTable(int capacity) {
    this.capacity = capacity;
    table = new HashMap<>(capacity);

    // 头尾指针都是哨兵结点
    head = new DNode<>();
    tail = new DNode<>();

    head.next = tail;
    tail.prev = head;

  }

  public static void main(String[] args) {
    LRUBaseHashTable<String, Object> cache = new LRUBaseHashTable<>(10);

    cache.add("a", 1);
    cache.add("b", 2);
    cache.add("c", 3);
    cache.add("d", 4);
    cache.add("e", 5);
    cache.add("f", 6);
    cache.add("g", 7);

    cache.printAll();

    cache.add("h", 8);
    cache.add("i", 9);
    cache.add("g", 10);
    cache.printAll();
    cache.add("k", 11);
    cache.printAll();

    cache.get("c");
    cache.printAll();

  }

  // 添加元素
  public void add(K key, V value) {
    DNode<K, V> node = table.get(key);
    if (node == null) {
      DNode<K, V> newNode = new DNode<>(key, value);
      // 将元素插入到哈希表
      table.put(key, newNode);
      // 将元素插入到链表头部
      addNodeToHead(newNode);
      length++;

      if (length >= capacity) {
        // 缓存容量已经满了
        // 删除尾结点元素
        DNode<K, V> tailNode = popTail();
        table.remove(tailNode.key);
        length--;
      }

    } else {
      node.value = value;
      // 将元素移动到头部
      moveNodeToHead(node);
    }

  }

  /**
   * 获取结点数据
   *
   * @param key
   * @return
   */
  public V get(K key) {
    DNode<K, V> node = table.get(key);
    if (node == null) {
      return null;
    }
    // 移动结点到头部
    moveNodeToHead(node);
    return node.value;
  }

  /**
   * 移出结点数据
   *
   * @param key
   */
  public void remove(K key) {
    DNode<K, V> node = table.get(key);
    if (node == null) {
      return;
    }
    removeNode(node);
    table.remove(key);
    length--;
  }

  /**
   * 移动结点到头部
   *
   * @param node
   */
  private void moveNodeToHead(DNode<K, V> node) {
    removeNode(node);
    addNodeToHead(node);
  }

  /**
   * 弹出尾部数据结点
   *
   * @return
   */
  private DNode<K, V> popTail() {
    // 尾结点是哨兵结点，需要获取它的前一个结点
    DNode<K, V> node = tail.prev;
    removeNode(node);
    return node;
  }

  /**
   * 移除结点
   * <p>
   * 修改结点的前驱指针结点
   * 修改结点的后继指针结点
   *
   * @param node
   */
  private void removeNode(DNode<K, V> node) {
    node.prev.next = node.next;
    node.next.prev = node.prev;
  }

  /**
   * 添加元素到链表头部
   *
   * @param newNode
   */
  private void addNodeToHead(DNode<K, V> newNode) {
    // 设置头结点
    newNode.next = head.next;
    newNode.prev = head;

    head.next.prev = newNode;
    head.next = newNode;
  }

  public void printAll() {
    DNode<K, V> n = head.next;
    StringBuilder sb = new StringBuilder();
    while (n != null && n.next != null) {
      sb.append(n.toString()).append(", ");
      n = n.next;
    }
    System.out.println(sb);
  }

  // 查找元素

  // 删除元素

  static class DNode<K, V> {

    K key;

    V value;

    DNode<K, V> prev;

    DNode<K, V> next;

    public DNode() {
    }

    public DNode(K key, V value) {
      this.key = key;
      this.value = value;
    }

    @Override
    public String toString() {
      return "{" + key + "=" + value + "}";
    }

  }

}
