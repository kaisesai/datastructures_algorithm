package com.kaige.datastructure.ch_12_sorts;

import java.util.Arrays;

/**
 * 12-3 求在 O(n) 时间复杂度内找到第 K 大元素，例如：2，1，5，3，中 第 2 大元素是 2
 * <p>
 * 思想：利用快排的思想，从数组 A[p...r] 中取出一个 pivot 作为分区点，将数组分为 A[p...q-1]、A[q]，A[q+1...r] 三部分。
 * 当 k == q+1 时，则第 k 大元素就是 A[q]，当 k< q+1 时，则在 A[p...q-1] 数组中在进行分区查找，直到找第 k 大元素为止。
 * 同理当 k > q+1 时，则在 A[q+1,r] 中进行分区查找。
 * 小于 pivot 的元素放到数组 A[p...q-1]中，等于 pivot 的则为 A[q]，大于 pivot 的元素放入 A[q+1...r] 数组中。
 */
public class KthSmallest {

  public static void main(String[] args) {
    int[] arr = {4, 2, 5, 3, 0, 1};
    System.out.println(Arrays.toString(arr));
    // 查找第 k 大元素
    int k = 4;
    System.out.println(findKthSmallest(arr, k));
    System.out.println(Arrays.toString(arr));
  }

  public static int findKthSmallest(int[] arr, int k) {
    if (arr == null || arr.length < k) {
      return -1;
    }

    // 执行分区点操作
    int q = partition(arr, 0, arr.length - 1);
    while (k != (q + 1)) {

      if (k < q + 1) {
        q = partition(arr, 0, q - 1);
      } else {
        q = partition(arr, q + 1, arr.length - 1);
      }
    }
    return arr[q];
  }

  private static int partition(int[] arr, int p, int r) {
    // 选择一个 pivot
    int pivot = arr[r];
    int i = p;
    int j = p;
    for (; j < r; j++) {
      if (arr[j] <= pivot) {
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
