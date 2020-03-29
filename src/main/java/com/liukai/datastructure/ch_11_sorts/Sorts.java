package com.liukai.datastructure.ch_11_sorts;

/**
 * 11-1 排序算法
 */
public class Sorts {

  public static void main(String[] args) {
    int[] a = {8, 1, 2, 4};
    printArray(a);
    // 冒泡排序
    // bubbleSort(a, a.length);
    // 插入排序
    // insertionSort(a, a.length);
    // 选择排序
    selectionSort(a, a.length);
    printArray(a);

  }

  /**
   * 选择排序
   * <p>
   * 排序思想：将数据分为已排序区间和未排序区间，在未排序区间中找到最小值，然后放入到已排序区间中最后一个元素的后面
   * <p>
   * 特点：
   * <p>
   * 1. 原地排序
   * <p>
   * 2. 时间复杂度 O(n^2)
   * <p>
   * 3. 非稳定的排序算法
   *
   * @param a 数组
   * @param n 数组大小
   */
  public static void selectionSort(int[] a, int n) {
    if (n <= 1) {
      return;
    }
    for (int i = 0; i < n; i++) {
      int minIndex = i;
      // 从未排序区间中选出最小元素索引
      for (int j = i + 1; j < n; j++) {
        if (a[j] < a[minIndex]) {
          minIndex = j;
        }
      }
      // 比较与交换
      int tmp = a[minIndex];
      a[minIndex] = a[i];
      a[i] = tmp;
    }
  }

  /**
   * 插入排序
   * <p>
   * 排序思想：将数据分为已排序区间和未排序区间，从未排序区间中取出元素插入到已排序区间中。
   * 初始已排序区间是第一个元素。
   * <p>
   * 特点：
   * <p>
   * 1. 原地排序
   * <p>
   * 2. 时间复杂度 O(n^2)
   * <p>
   * 3. 稳定的排序算法
   *
   * @param a 数组
   * @param n 数组大小
   */
  public static void insertionSort(int[] a, int n) {
    if (n <= 1) {
      return;
    }
    for (int i = 1; i < n; i++) {// 外层循环，作为从未排序区间中取出元素
      int value = a[i];
      int j = i - 1;
      for (; j >= 0; j--) {// 内层循环，作为与已排序区间的数据进行对比，并移动数据，从已排序区间的尾部向头部遍历
        if (value < a[j]) {
          // 移动数据
          a[j + 1] = a[j];
        } else {
          break;
        }
      }
      // 插入元素
      a[j + 1] = value;
    }

  }

  /**
   * 插入排序
   * <p>
   * 插入的元素从有序元素区间头部到尾部比较
   *
   * @param a 数组
   * @param n 数组大小
   */
  public static void insertionSort2(int[] a, int n) {
    if (n <= 1) {
      return;
    }
    for (int i = 1; i < n; i++) {
      int value = a[i];
      int j = 0;
      for (; j < i; j++) {
        if (value < a[j]) {

        }
      }

    }
  }

  /**
   * 冒泡排序改进
   * <p>
   * 在每一轮排序后记录最后一次元素交换的位置，作为下次比较的边界，对于边界外的元素下次循环中无需要比较
   *
   * @param a 数组
   * @param n 数组大小
   */
  public static void bubbleSort2(int[] a, int n) {
    if (n <= 1) {
      return;
    }
    // 最后一次交换的位置
    int lastExchange = 0;
    // 无序数据的边界，每次只需要比较到这里即可退出
    int sorBorder = n - 1;
    for (int i = 0; i < n; i++) {
      // 提前退出标志位
      boolean flag = false;
      for (int j = 0; j < sorBorder; j++) {
        if (a[j] > a[j + 1]) {
          // 交换
          int tmp = a[j];
          a[j] = a[j + 1];
          a[j + 1] = tmp;
          flag = true;
          lastExchange = j;
        }
      }

      sorBorder = lastExchange;
      if (!flag) {
        // 没有数据交换，提前退出
        break;
      }

    }

  }

  /**
   * 冒泡排序
   * <p>
   * 排序思想：每一次冒泡就是从头开始，将两个相邻的元素进行比较与交换，执行到最后就会选择出一个最大值，一次冒泡结束。
   * 需要执行 n 次冒泡
   * <p>
   * 特点：
   * <p>
   * 1. 原地排序
   * <p>
   * 2. 时间复杂度 O(n^2)
   * <p>
   * 3. 稳定的排序算法
   *
   * @param a 数组
   * @param n 数组大小
   */
  public static void bubbleSort(int[] a, int n) {
    if (n <= 1) {
      return;
    }
    // 提前退出标志位
    for (int i = 0; i < n; i++) {
      boolean flag = false;
      // 每次冒泡就会选择出一个最大元素，所以下次比较次数为：数组长度-冒泡次数
      for (int j = i; j < n - i - 1; j++) {
        if (a[j] > a[j + 1]) {
          int tmp = a[j];
          a[j] = a[j + 1];
          a[j + 1] = tmp;
          flag = true;
        }
      }
      // 没有数据交换，提前退出
      if (!flag) {
        break;
      }
    }

  }

  public static void printArray(int[] a) {
    StringBuilder sb = new StringBuilder();
    for (int value : a) {
      sb.append(value).append(" ");
    }
    System.out.println(sb);
  }

}
