package com.liukai.datastructure.ch_06_linkedlist;

import java.util.HashMap;
import java.util.Map;

/**
 * 6-3 基于数组的 LRU（最近最少使用策略） 缓存
 * <p>
 * 使用数组存储数据，借助 HashMap 记录元素索引值
 */
public class LRUBasedArray<T> {

  /**
   * 默认缓存容量8
   */
  private static final int DEFAULT_CAPACITY = 1 << 3;

  /**
   * 缓存容量
   */
  private int capacity;

  /**
   * 缓存中元素的数量
   */
  private int count;

  /**
   * 数据
   */
  private T[] value;

  /**
   * 用于记录元素的索引值
   */
  private Map<T, Integer> holder;

  public LRUBasedArray(int capacity) {
    this.capacity = capacity;
    this.value = (T[]) new Object[capacity];
    this.count = 0;
    this.holder = new HashMap<>(capacity);// 指定 map 的容量大小，防止其自动扩容
  }

  public LRUBasedArray() {
    this(DEFAULT_CAPACITY);
  }

  public static void main(String[] args) {

    LRUBasedArray<String> cache = new LRUBasedArray<>();
    cache.offer("a");
    cache.offer("b");
    cache.offer("c");
    cache.offer("d");
    System.out.println(cache);
    cache.offer("a");
    System.out.println(cache);
    cache.offer("2321");
    cache.offer("2aaa321");
    System.out.println(cache);
    cache.offer(null);

  }

  /**
   * 模拟访问某个值
   *
   * @param object 值
   */
  public void offer(T object) {
    if (object == null) {
      throw new IllegalArgumentException("不支持 null 数据");
    }

    Integer index = this.holder.get(object);
    if (index == null) {
      // 数据不存在，则插入数据
      if (isFull()) {
        // 缓存已满，删除最后一个元素，再插入数据到数组的头部
        // 删除最后一个元素，并且右移
        this.removeAndCache(object);

      } else {
        // 缓存未满，插入数据到数组的头部
        this.cache(object, this.count);
      }

    } else {
      // 数据存在，则更新数据
      update(index);
    }

  }

  /**
   * 判断元素是否存在缓存中
   *
   * @param object 元素
   * @return 是否存在
   */
  private boolean isContains(T object) {
    return holder.get(object) == null;
  }

  /**
   * 缓存已满的情况下，先踢出最后一个元素，再将新元素插入到缓存头部
   *
   * @param object 新元素
   */
  private void removeAndCache(T object) {
    // 先删除最后一个元素数据
    T lastValue = this.value[this.count--];
    this.holder.remove(lastValue);
    // 右移所有元素，并将新元素缓存到头部
    this.cache(object, this.count);

  }

  /**
   * 将数据缓存到数组头部，并且将指定索引左侧的数据向右移动一位
   *
   * @param target
   * @param index
   */
  private void cache(T target, int index) {
    rightShift(index);
    this.value[0] = target;
    this.holder.put(target, 0);
    this.count++;
  }

  private boolean isFull() {
    return this.capacity == this.count;
  }

  /**
   * 若缓存中有值，则更新元素
   *
   * @param index 索引
   */
  private void update(Integer index) {
    // 先找到元素
    T target = this.value[index];
    // 将数组中目标元素索引位置左侧的数据右移一位
    rightShift(index);
    // 将元素插入到数组头部
    this.value[0] = target;
    // 设置元素索引值
    this.holder.put(target, 0);
  }

  /**
   * 将指定索引位置左侧的数据前右移动一位
   *
   * @param index 索引
   */
  private void rightShift(Integer index) {
    for (int i = index; i > 0; i--) {
      this.value[i] = this.value[i - 1];
      // 更新 holder 的元素索引值
      this.holder.put(this.value[i], i);
    }
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < count; i++) {
      sb.append(value[i]);
      sb.append(" ");
    }
    return sb.toString();
  }

}
