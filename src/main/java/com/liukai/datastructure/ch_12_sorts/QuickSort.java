package com.liukai.datastructure.ch_12_sorts;

import java.util.Arrays;

/**
 * 12-2 快速排序
 * <p>
 * 思想：利用分治思想 + 递归技巧处理排序。将一个数组 A[p...r] 选择一个元素作为 pivot（分区点），将数组分为 A[p...q-1]、A[q]、A[q+1...r] 三部分，
 * 然后将数组中小于 pivot（即 A[q]） 的元素放到数组 A[p...q-1]，大于 pivot 的元素放入 A[q+1...r]。
 * <p>
 * 特点：
 * 1. 最好情况时间复杂度：O(nlogn)，最坏情况时间复杂度：O(n^2)，平均情况时间复杂度：O(nlogn)
 * 2. 非稳定排序
 * 3. 原地排序算法
 */
public class QuickSort {

  public static void main(String[] args) {
    int[] arr = {10, 20, 5, 1, 15, 20, 33, 8};
    System.out.println(Arrays.toString(arr));
    quickSort(arr);
    System.out.println(Arrays.toString(arr));
  }

  public static void quickSort(int[] arr) {
    quickSortC(arr, 0, arr.length - 1);
  }

  private static void quickSortC(int[] arr, int p, int r) {
    if (p >= r) {
      return;
    }
    // 分区函数
    int q = partition(arr, p, r);
    // 递归排序
    quickSortC(arr, p, q - 1);
    quickSortC(arr, q + 1, r);
  }

  private static int partition(int[] arr, int p, int r) {
    // 选择最后一个元素最为分区值
    int pivot = arr[r];
    // 变量 i 将数组分为两部分，A[p...i-1]作为“已处理区间”，A[i...r-1] 作为未处理区间
    int i = p;
    for (int j = p; j < r; j++) {
      if (arr[j] < pivot) {
        swap(arr, i, j);
        i++;
      }
    }
    swap(arr, i, r);
    return i;
  }

  private static void swap(int[] arr, int i, int j) {
    if (i == j) {
      return;
    }
    int tmp = arr[i];
    arr[i] = arr[j];
    arr[j] = tmp;
  }

}
