package com.liukai.datastructure.ch_05_array;

import java.util.Objects;

/**
 * 5-2 泛型数组
 * <p>
 * 支持扩容
 */
public class GenericArray<T> {

  private T[] data;

  private int size;

  /**
   * 指定数组容量大小
   *
   * @param capacity 数组的容量
   */
  public GenericArray(int capacity) {
    data = (T[]) new Object[capacity];
    this.size = 0;
  }

  /**
   * 无参构造器
   * <p>
   * 默认数组容量大小为 10
   * </p>
   */
  public GenericArray() {
    this(10);
  }

  public static void main(String[] args) {
    GenericArray<Integer> array = new GenericArray<>();
    array.addFirst(1);
    array.addFirst(2);
    array.addFirst(3);
    array.addLast(4);
    System.out.println(array);
    System.out.println("数组是否为空：" + array.isEmpty());
    array.add(4, 10);
    System.out.println(array);

    array.removeFirst();
    System.out.println("removeFirst 后: " + array);
    array.removeLast();
    System.out.println("removeLast 后: " + array);

    array.remove(2);
    System.out.println("remove 后: " + array);

  }

  /**
   * 获取数组容量
   *
   * @return 数组容量
   */
  public int getCapacity() {
    return data.length;
  }

  /**
   * 获取当前元素个数
   *
   * @return 当前元素个数
   */
  public int count() {
    return this.size;
  }

  /**
   * 判断数组是否为空
   *
   * @return 数组是否为空
   */
  public boolean isEmpty() {
    return this.size == 0;
  }

  /**
   * 修改 index 位置的元素
   *
   * @param index
   * @param e
   */
  public void set(int index, T e) {
    checkIndex(index);
    data[index] = e;
  }

  /**
   * 获取 index 位置的元素
   *
   * @param index 元素索引
   * @return 元素
   */
  public T get(int index) {
    checkIndex(index);
    return data[index];
  }

  /**
   * 检查数据元素是否包含元素 e
   *
   * @param e 元素
   * @return 数组是否包含元素 e
   */
  public boolean contains(T e) {
    for (int i = 0; i < this.size; i++) {
      if (Objects.equals(e, data[i])) {
        return true;
      }
    }
    return false;
  }

  /**
   * 查找元素对应的数组索引
   *
   * @param e 元素
   * @return 返回对应元素的数组下标，未找到返回 -1
   */
  public int find(T e) {
    for (int i = 0; i < this.size; i++) {
      if (Objects.equals(data[i], e)) {
        return i;
      }
    }
    return -1;
  }

  /**
   * 添加指定位置的元素
   *
   * @param index
   * @param e
   */
  public void add(int index, T e) {
    //检查索引合法性
    checkIndexForAdd(index);
    // 数组是否已满，则将数组扩容为原来的两倍
    if (this.size == this.data.length) {
      resize(2 * data.length);
    }

    for (int i = this.size; i > index; i--) {
      data[i] = data[i - 1];
    }
    data[index] = e;
    this.size++;
  }

  /**
   * 插入元素到头部
   *
   * @param e 元素
   */
  public void addFirst(T e) {
    add(0, e);
  }

  /**
   * 插入元素到尾部
   *
   * @param e 元素
   */
  public void addLast(T e) {
    add(this.size, e);
  }

  /**
   * 调整数组容量，时间复杂度是 O(n)
   *
   * @param capacity 数组容量
   */
  public void resize(int capacity) {
    // 调整数组的大小
    Object[] newData = new Object[capacity];
    for (int i = 0; i < this.size; i++) {
      newData[i] = data[i];
    }
    data = (T[]) newData;
  }

  /**
   * 删除 index 上的元素
   *
   * @param index 删除元素的索引
   * @return 删除的元素
   */
  public T remove(int index) {
    // 检查索引合法性
    checkIndex(index);

    // 获取删除的元素
    T e = data[index];
    // 将 index~n-1 的元素向前移动
    for (int i = index; i < this.size - 1; i++) {
      data[i] = data[i + 1];
    }

    this.size--;

    // 缩容，数组实际数量为容量的 1/4，并且数组的容量大于 2 时，进行缩容
    if (this.size == data.length / 4 && data.length / 2 != 0) {
      resize(data.length / 2);
    }

    return e;
  }

  /**
   * 删除头部元素
   *
   * @return 头部元素
   */
  public T removeFirst() {
    return remove(0);
  }

  /**
   * 删除最后一个元素
   *
   * @return 删除的元素
   */
  public T removeLast() {
    return remove(this.size - 1);
  }

  /**
   * 删除元素
   *
   * @param e 删除的元素
   * @return 元素的索引
   */
  public void removeElement(T e) {
    int index = this.find(e);
    if (index != -1) {
      remove(index);
    }
  }

  @Override
  public String toString() {

    StringBuilder sb = new StringBuilder(
      "当前数组的总容量为：" + this.data.length + "，元素数量为：" + this.size + ", 元素为：");
    for (int i = 0; i < this.size; i++) {
      sb.append(data[i] + ", ");
    }
    return sb.toString();
  }

  /**
   * 检查索引的合法性
   *
   * @param index 数组的索引
   */
  public void checkIndex(int index) {
    if (index < 0 || index >= this.size) {
      throw new IllegalArgumentException("非法的位置，范围为： 0<=index<Array.getCount()");
    }
  }

  public void checkIndexForAdd(int index) {
    if (index < 0 || index > this.size) {
      throw new IllegalArgumentException("插入失败，索引的范围应该是为： 0 <= index <= " + this.size);
    }
  }

}
