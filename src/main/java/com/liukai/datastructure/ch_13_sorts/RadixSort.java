package com.liukai.datastructure.ch_13_sorts;

/**
 * 13-3 基数排序
 * <p>
 * 核心思想：将要排序的数据分割成独立的“位”，依次按照低位和高位排序，其中按位排序时使用稳定排序算法。根据每一位来进行排序可以用桶排序
 * 或者计数排序，它们的时间复杂度可以做到 O(n)。
 * <p>
 * 特点：
 * 1. 时间复杂度为 O(n)
 * 2. 适用场景：要求排序的数据可以分割成独立的“位”来排序，位之间有递进的关系，如果 a 数据的高位比 b 数据大，
 * 那剩下的就不用比较了。而且每一位的数据范围不能太大，要可以用线性排序算法来排序。
 */
public class RadixSort {

  /**
   * 基数排序
   *
   * @param arr 要排数的数组
   */
  public static void radixSort(int[] arr) {
    // 获取数组最大值
    int max = arr[0];
    for (int i = 0; i < arr.length; i++) {
      if (arr[i] > max) {
        max = arr[i];
      }
    }

    // 从个位开始，对数组 arr 按“指数”进行排序
    for (int exp = 1; max / exp > 0; exp *= 10) {
      countingSort(arr, exp);
    }

  }

  /**
   * 计数排序——对数组按照“位”进行排序
   *
   * @param arr 数组
   * @param exp 指数
   */
  private static void countingSort(int[] arr, int exp) {
    if (arr == null || arr.length < 2) {
      return;
    }

    // 计算每个元素的个数
    int[] c = new int[10];// int 的位数最大不超过 10
    for (int i = 0; i < arr.length; i++) {
      // 取出数组的元素的某个位
      int i1 = (arr[i] / exp) % 10;
      c[i1]++;
    }

    // 依次累加，计算排序后的位置
    for (int i = 1; i < c.length; i++) {
      c[i] += c[i - 1];
    }

    int[] r = new int[arr.length];
    for (int i = arr.length - 1; i >= 0; i--) {
      // 位的值
      int index = (arr[i] / exp) % 10;
      // 小于等于位值元素的数量
      int count = c[index];
      r[count - 1] = arr[i];
      c[index]--;
    }

    // 拷贝数据到数组 arr
    System.arraycopy(r, 0, arr, 0, r.length);
  }

}
