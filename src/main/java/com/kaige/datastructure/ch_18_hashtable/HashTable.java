package com.kaige.datastructure.ch_18_hashtable;

import java.util.Objects;

/**
 * 18-1 哈希表
 */
public class HashTable<K, V> {

  /**
   * 散列表默认长度
   */
  private static final int DEFUALT_INITAL_CAPACITY = 8;

  /**
   * 装载因子
   */
  private static final float LOAD_FACTOR = 0.75f;

  /**
   * 实际元素数量
   */
  private int size = 0;

  /**
   * 散列表索引数量
   */
  private int use = 0;

  /**
   * 初始化散列表数组
   */
  private Entry<K, V>[] table;

  public HashTable() {
    table = new Entry[DEFUALT_INITAL_CAPACITY];
  }

  /**
   * 新增
   *
   * @param key
   * @param value
   */
  public void put(K key, V value) {
    // 计算 hash 函数
    int index = hash(key);
    // 位置未被引用，创建哨兵节点
    if (table[index] == null) {
      table[index] = new Entry<>(null, null, null);
    }

    Entry<K, V> tmp = table[index];
    // 新增结点
    if (tmp.next == null) {
      tmp.next = new Entry<>(key, value, null);
      size++;
      use++;
      // 动态扩容
      if (use >= table.length * LOAD_FACTOR) {
        resize();
      }
    } else {
      // 解决散列冲突，使用链表法
      do {
        tmp = tmp.next;
        // key 相同则替换
        if (Objects.equals(tmp.key, key)) {
          tmp.value = value;
          return;
        }
      }
      while (tmp.next != null);
      // 新插入的元素放入链表头部
      Entry<K, V> temp = table[index].next;
      table[index].next = new Entry<>(key, value, temp);
      size++;
    }

  }

  /**
   * 动态扩容
   */
  private void resize() {
    Entry<K, V>[] oldTable = table;
    table = new Entry[table.length * 2];

    use = 0;
    for (int i = 0; i < oldTable.length; i++) {
      // 将原先散列表中的数据复制到新的散列表中
      if (oldTable[i] == null || oldTable[i].next == null) {
        continue;
      }
      // 遍历桶中所有元素，并且重新计算 hash 值
      Entry<K, V> e = oldTable[i];
      while (e.next != null) {
        e = e.next;
        int index = hash(e.key);
        if (table[index] == null) {
          // 创建哨兵结点
          table[index] = new Entry<>(null, null, null);
          use++;
        }
        table[index].next = new Entry<>(e.key, e.value, table[index].next);
      }
    }
  }

  /**
   * 删除
   *
   * @param key
   */
  public void remove(K key) {
    int index = hash(key);
    Entry<K, V> e = table[index];
    if (e == null || e.next == null) {
      return;
    }

    Entry<K, V> pre;
    Entry<K, V> headNode = table[index];
    do {
      pre = e;
      e = e.next;
      if (Objects.equals(e.key, key)) {
        pre.next = e.next;
        size--;
        if (headNode.next == null) {
          // 桶中没有实际的元素
          use--;
        }

        return;
      }
    }
    while (headNode.next != null);
  }

  /**
   * 获取
   *
   * @param key
   * @return
   */
  public V get(K key) {
    int index = hash(key);
    if (table[index] == null || table[index].next == null) {
      return null;
    }
    Entry<K, V> e = table[index];
    while (e.next != null) {
      e = e.next;
      if (Objects.equals(e.key, key)) {
        return e.value;
      }
    }
    return null;
  }

  /**
   * 散列函数
   *
   * @param key
   * @return
   */
  private int hash(K key) {
    int h;
    return (key == null) ? 0 : ((h = key.hashCode()) ^ (h >>> 16)) % table.length;
  }

  static class Entry<K, V> {

    K key;

    V value;

    Entry<K, V> next;

    public Entry(K key, V value, Entry<K, V> next) {
      this.key = key;
      this.value = value;
      this.next = next;
    }

  }

}
