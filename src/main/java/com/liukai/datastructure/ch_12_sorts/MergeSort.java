package com.liukai.datastructure.ch_12_sorts;

import java.util.Arrays;

/**
 * 12-1 归并排序
 * <p>
 * 思想：采用分治思想 + 递归的技巧，将一个数组从中间分解为左右两个子数组，对两个子数组进行排序，
 * 最后将排好序的两个子数组合并为一个新的数组。
 * <p>
 * 特点：
 * 1. 时间复杂度 O(nlogn)
 * 2. 空间复杂度 O(n)
 * 3. 非原地排序算法
 * 4. 稳定排序算法
 */
public class MergeSort {

  public static void main(String[] args) {
    int[] a = {4, 3, 5, 1, 9, 2};
    System.out.println(Arrays.toString(a));
    mergeSort(a);
    System.out.println(Arrays.toString(a));
  }

  public static void mergeSort(int[] a) {
    mergeSortC(a, 0, a.length - 1);
  }

  private static void mergeSortC(int[] a, int p, int r) {
    // 终止条件
    if (p >= r) {
      return;
    }
    // 取 p 到 r 之间的中间位置 q，放置（p+r）的和超过 int 类型的最大值
    int q = p + (r - p) / 2;
    // 分支递归
    mergeSortC(a, p, q);
    mergeSortC(a, q + 1, r);
    // 合并数据
    // merge(a, p, q, r);
    mergeBySentry(a, p, q, r);
  }

  private static void merge(int[] a, int p, int q, int r) {
    int i = p;// 记录 left 的索引
    int j = q + 1;// 记录 right 的索引
    int k = 0;// 记录新数组的的索引
    int[] tmp = new int[r - p + 1];

    // 比较两个子数组，将最小的值放入临时数组中
    while (i <= q && j <= r) {
      if (a[i] <= a[j]) {
        tmp[k++] = a[i++];
      } else {
        tmp[k++] = a[j++];
      }
    }

    // 将子数组中剩余元素放入依次放入 tmp 数组中
    for (; i <= q; i++) {
      tmp[k++] = a[i];
    }

    for (; j <= r; j++) {
      tmp[k++] = a[j];
    }

    // 将 tmp 数组合并到 a 数组中
    k = 0;
    for (int l = p; l <= r; l++) {
      a[l] = tmp[k++];
    }
  }

  /**
   * 合并（哨兵）
   *
   * @param arr 原始数组
   * @param p   左边数组第一个索引
   * @param q   原始数组中间索引
   * @param r   右侧数组第一个索引
   */
  private static void mergeBySentry(int[] arr, int p, int q, int r) {
    int[] leftArr = new int[q - p + 2];
    int[] rightArr = new int[r - q + 1];

    for (int i = 0; i <= q - p; i++) {
      leftArr[i] = arr[p + i];
    }

    // 第一个数组添加哨兵（最大值）
    leftArr[q - p + 1] = Integer.MAX_VALUE;

    for (int i = 0; i < r - q; i++) {
      rightArr[i] = arr[q + 1 + i];
    }

    // 第二个数组添加哨兵（最大值）
    rightArr[r - q] = Integer.MAX_VALUE;

    int i = 0;
    int j = 0;
    int k = p;
    while (k <= r) {
      // 当左边数组到达哨兵值时，i 不再增加，直到右边数组读取完剩余值，同理右边数组也是一样
      if (leftArr[i] <= rightArr[j]) {
        arr[k++] = leftArr[i++];
      } else {
        arr[k++] = rightArr[j++];
      }
    }
  }

}
