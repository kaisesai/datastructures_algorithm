package com.kaige.alog.test.mianshi;

import org.junit.Test;

import java.util.Arrays;

public class SortTest_07 {
  
  // 插入排序
  private static void insertSort(int[] array, int n) {
    // 将区间划分为已排序和未排序区间，从未排序区间中取出一个元素，放入已排序区间中
    
    for (int i = 1; i < n; i++) {
      int value = array[i];
      int j = i - 1;
      for (; j >= 0; j--) {
        if (value < array[j]) {
          // 交换数据
          // 1,3,5,2,4
          array[j + 1] = array[j];
        } else {
          break;
        }
      }
      // 插入元素
      array[j + 1] = value;
    }
  }
  
  // 快速排序
  private static void quickSort(int[] array, int n) {
    quickSort_(array, 0, n - 1);
  }
  
  private static void quickSort_(int[] array, int p, int r) {
    if (p >= r) {
      return;
    }
    // 查找分区点
    int q = partition(array, p, r);
    // 左半部分排序
    quickSort_(array, p, q - 1);
    // 右半部分排序
    quickSort_(array, q + 1, r);
  }
  
  private static int partition(int[] array, int p, int r) {
    // 三数取中法
    int mid = (r - p) / 2 + p;
    int pivot = array[mid];
    swap(array, mid, r);
    
    int i = p;
    // 开始分区，将 p,i-1 分为已处理区间，i,r 是处理区间
    for (int j = p; j < r; j++) {
      if (array[j] < pivot) {
        swap(array, i, j);
        i++;
      }
    }
    
    swap(array, i, r);
    
    return i;
  }
  
  private static void swap(int[] array, int mid, int r) {
    if (mid == r) {
      return;
    }
    int tmp = array[mid];
    array[mid] = array[r];
    array[r] = tmp;
  }
  
  @Test
  public void testSort() {
    int[] arr = {5, 1, 0, 3, 2, 6, 9, 4};
    System.out.println(Arrays.toString(arr));
    // insertSort(arr, arr.length);
    quickSort(arr, arr.length);
    System.out.println(Arrays.toString(arr));
  }
  
}
