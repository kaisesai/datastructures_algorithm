package com.kaige.datastructure.ch_28_heap;

import java.util.Arrays;

/**
 * 28-1 堆
 * <p>
 * 堆是一种特殊的树，需要满足以下两点要求:
 * 1. 堆是一个棵完全二叉树；
 * 2. 堆中的每个节点的值都大于等于（或者小于等于）它的左右子节点的值。
 * <p>
 * 大顶堆：堆中每个节点值都大于等于子节点值
 * 小顶堆：堆中每个节点值都小于等于子节点值
 *
 * <p>
 * 堆的存储方式：因为是完全二叉树，所以使用数组进行存储。
 * <p>
 * 堆的重要的两个操作：
 * 1. 插入元素：将元素插入堆的末尾，然后自下往上的堆化
 * 2. 删除堆顶元素：将数组末尾的元素替换到堆顶，
 * <p>
 * 堆排序：
 * 特点：
 * 1. 原地排序
 * 2. 非稳定的排序算法
 * 3. 时间复杂度 O(nlogn)
 * <p>
 * 堆排序的步骤：
 * 1. 建堆：在数组中从后往前处理数据，对堆中非叶子节点进行自上往下的堆化操作
 * 2. 排序：将数组末尾的元素与堆顶的元素进行交换，然后对堆顶的元素进行自上往下的堆化操作，然后将堆数据量减一，这样迭代的操作，最后数组中的元素就是有序排列的了。
 */
public class Heap {

  /**
   * 数组，保存节点元素，数据从下标为 1 开始存储数据
   */
  private final int[] a;

  /**
   * 堆的最大容量
   */
  private final int capacity;

  /**
   * 堆中元素的个数
   */
  private int count;
  
  /**
   * 创建一个大顶堆
   *
   * @param capacity 堆最大容量
   */
  private Heap(int capacity) {
    this.capacity = capacity;
    a = new int[capacity + 1];
    count = 0;
  }

  public static void main(String[] args) {
    // 堆
    Heap heap = new Heap(10);
    heap.insert(5);
    heap.insert(9);
    heap.insert(10);
    heap.insert(1);
    heap.insert(3);
    heap.printHeap();
    heap.insert(4);
    heap.insert(8);
    heap.insert(20);
    heap.insert(18);
    heap.insert(16);
    heap.printHeap();

    // 删除堆顶元素
    System.out.println("删除堆顶元素：" + heap.removeMaxValue());
    heap.printHeap();
    System.out.println("删除堆顶元素：" + heap.removeMaxValue());
    heap.printHeap();

    // 堆排序
    heapSort(heap.a, heap.count);
    heap.printHeap();

  }

  /**
   * 从上往下堆化
   *
   * @param a 数组
   * @param n 数组中元素的个数
   * @param i 节点索引
   */
  private static void heapify(int[] a, int n, int i) {
    while (true) {
      // 记录最大值的索引
      int maxPos = i;
      // 与左子节点进行比较
      if (i * 2 <= n && a[maxPos] < a[i * 2]) {
        maxPos = i * 2;
      }
      // 与右子节点进行比较
      if (i * 2 + 1 <= n && a[maxPos] < a[i * 2 + 1]) {
        maxPos = i * 2 + 1;
      }
      // 当前节点已经大于等于左右子节点值
      if (maxPos == i) {
        break;
      }
      // 交换节点
      swap(a, i, maxPos);
      i = maxPos;
    }

  }
  
  /**
   * 堆排序
   * <p>
   * 1. 建堆：
   * 2. 排序：将堆堆顶的元素与堆中最后的元素（数组中的最后的）进行交换，堆的元素数量减一，执行自上往下的堆化，迭代的执行
   *
   * @param a 数组
   * @param n 数组中元素的个数
   */
  private static void heapSort(int[] a, int n) {
    if (a == null || a.length == 0) {
      return;
    }
    
    // 建堆
    buildHeap(a, n);
    
    // 注意这里的的数组中数据是从索引 1 开始的，正常的排序，数组索引从 0 开始，需要修改下这的变量
    // 排序
    int i = n;
    while (i > 0) {
      // 交换节点元素
      swap(a, 1, i);
      i--;
      // 执行堆化
      // 注意这里的的数组中数据是从索引 1 开始的
      heapify(a, i, 1);
    }
  }

  /**
   * 建堆
   * <p>
   * 在数组中，从非叶子节点（索引 n/2）开始从后往前的处理数据，自上而下的堆化。
   *
   * @param a 数组
   * @param n 数组中元素的个数
   */
  private static void buildHeap(int[] a, int n) {

    for (int i = n / 2; i > 0; i--) {
      heapify(a, n, i);
    }

  }

  private static void swap(int[] a, int i, int j) {
    int tmp = a[i];
    a[i] = a[j];
    a[j] = tmp;
  }

  /**
   * 插入元素
   *
   * @param value 数据
   */
  public void insert(int value) {
    if (count == capacity) {
      // 堆已满
      return;
    }
    // 数据从下标为 1 开始存储数据
    ++count;
    a[count] = value;
    // 堆化
    int i = count;
    // 自下往上的堆化
    while (i > 1 && a[i] > a[i / 2]) {
      swap(a, i, i / 2);
      i = i / 2;
    }
  }
  
  /**
   * 删除堆顶元素
   *
   * @return 堆顶元素值，没有则返回 -1
   */
  private int removeMaxValue() {
    assert count > 0;
    // 将数组中最后一个元素放到堆顶，然后进行自上往下的堆化
    int deletedValue = a[1];
    a[1] = a[count];
    a[count] = 0;
    count--;
    // 堆化
    heapify(a, count, 1);
    return deletedValue;
  }
  
  private void printHeap() {
    System.out.println(Arrays.toString(a));
  }
  
}
