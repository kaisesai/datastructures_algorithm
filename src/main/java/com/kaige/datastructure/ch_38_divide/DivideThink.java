package com.kaige.datastructure.ch_38_divide;

import java.util.Arrays;

/**
 * 38-1 分治思想
 *
 * <p>描述：分治思想，就是分而治之，它是一种处理思想，可以借助编程技巧递归来实现。
 */
public class DivideThink {

  private static int num = 0;

  public static void main(String[] args) {
    int[] a = {10, 6, 9, 20, 30, 2, 22, 56, 33};
    System.out.println("排序之前的数据：" + Arrays.toString(a));
    // 排序并且统计逆序对个数
    count(a, a.length);
    System.out.println("排序之后的数据：" + Arrays.toString(a) + "\t 逆序对个数：" + num);
  }

  /**
   * 分治思想：利用归并排序，统计数组中数据的逆序对个数。
   *
   * <p>逆序对指的是一个数组中，元素无序的个数对数之和。
   *
   * @param a 数组
   * @param n 数组中元素的个数
   */
  public static int count(int[] a, int n) {
    num = 0;
    mergeSortCount(a, 0, n - 1);
    return num;
  }

  /**
   * 归并排序元素，并且统计数组的逆序对个数
   *
   * @param a 数组
   * @param p 开头索引
   * @param r 结束索引
   */
  public static void mergeSortCount(int[] a, int p, int r) {
    if (p >= r) {
      return;
    }
    // 计算中间值
    int q = p + (r - p) / 2;
    mergeSortCount(a, p, q);
    mergeSortCount(a, q + 1, r);
    // 合并数据
    merge(a, p, q, r);
  }

  /**
   * 合并有序元素，并且统计逆序对个数
   *
   * @param a 数组
   * @param p 开始索引
   * @param q 中间索引
   * @param r 结束索引
   */
  private static void merge(int[] a, int p, int q, int r) {
    int i = p;
    int j = q + 1;
    int k = 0;
    int[] tmp = new int[r - p + 1];

    while (i <= q && j <= r) {
      if (a[i] <= a[j]) {
        tmp[k++] = a[i++];
      } else {
        // 统计 p-q 之间，a[i] 比 a[j] 大的元素的个数
        num += q - i + 1;
        tmp[k++] = a[j++];
      }
    }

    // 处理剩下的元素
    while (i <= q) {
      tmp[k++] = a[i++];
    }

    while (j <= r) {
      tmp[k++] = a[j++];
    }

    // 合并数据
    for (i = 0; i < k; i++) {
      a[p + i] = tmp[i];
    }
  }

}
