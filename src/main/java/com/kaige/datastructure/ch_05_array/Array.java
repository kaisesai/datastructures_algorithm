package com.kaige.datastructure.ch_05_array;

/**
 * 5-1 数组
 * <p>
 * 1）、数组的插入、删除，按照下边随机访问操作；
 * 2）、数组中的数据类型是 int
 */
public class Array {
  
  // 定义整型数据 data 保存数据
  private final int[] data;
  
  // 定义数组的长度
  private final int n;
  
  // 定义数组的实际个数
  private int count;
  
  /**
   * 构造方法，定义数组大小
   *
   * @param capacity 数组容量
   */
  private Array(int capacity) {
    data = new int[capacity];
    n = capacity;
    count = 0;
  }

  public static void main(String[] args) {
    Array array = new Array(10);
    array.insert(0, 0);
    array.insert(1, 1);
    array.printAll();
    array.insert(2, 2);
    array.insert(3, 3);
    array.printAll();

    array.delete(3);
    array.printAll();
    array.delete(0);
    array.printAll();

    System.out.println(array.find(1));

  }
  
  /**
   * 根据索引查找数组中的元素并返回
   *
   * @param index 索引
   * @return 返回指定索引的数据值，没有则返回 -1
   */
  private int find(int index) {
    if (index < 0 || index >= count) {
      return -1;
    }
    return data[index];
  }

  /**
   * 插入元素：头部插入，尾部插入
   *
   * @param index 索引
   * @param value 值
   * @return 插入是否成功
   */
  public boolean insert(int index, int value) {
    // 数组已满
    if (count == n) {
      System.out.println("没有可插入的位置");
      return false;
    }

    // 位置不合法
    if (index < 0 || index > count) {
      System.out.println("不合法的位置");
      return false;
    }

    // 位置合法
    // 将 index~count-1 的元素向后移动一位
    for (int i = count - 1; i >= index; i--) {
      data[i + 1] = data[i];
    }

    // 设置 index 的值
    data[index] = value;
    count++;

    return true;

  }
  
  /**
   * 删除指定位置的元素
   *
   * @param index 索引
   * @return 删除是否成功
   */
  private boolean delete(int index) {
    // 位置不合法
    if (index < 0 || index >= count) {
      return false;
    }
    // 从删除位置开始，将 index~count-1的元素向前移动一位数
    for (int i = index; i < count; i++) {
      data[i] = data[i + 1];
    }
    count--;
    return true;
  }

  /**
   * 打印出数组的所有元素
   */
  public void printAll() {
    for (int i = 0; i < count; i++) {
      System.out.print(data[i] + "\t");
    }
    System.out.println();
  }

}
