package com.kaige.datastructure.ch_13_sorts;

/**
 * 13-1 桶排序
 * <p>
 * 思想：将排序的数据划分到多个有序的桶中，每个桶里的数据再单独排序，桶内排完序之后，再把每个桶里的数据依次取出，，组成的序列就是排好序的了。
 * 特点：
 * 1. 时间复杂度是 O(n)
 * 2. 适合用在外部排序中，外部排序就是即数据存储在外部磁盘，数据量大，内存有限，无法一次性加载到内存。
 */
class BucketSort {
  
  /**
   * 桶排序
   *
   * @param arr        无序数组
   * @param bucketSize 每个桶的大小
   */
  static void bucketSort(int[] arr, int bucketSize) {
    if (arr == null || arr.length == 1) {
      return;
    }
    
    // 获取数组中最大值与最小值
    int minValue = arr[0];
    int maxValue = arr[0];
    for (int i = 0; i < arr.length; i++) {
      if (arr[i] > maxValue) {
        maxValue = arr[i];
      } else if (arr[i] < minValue) {
        minValue = arr[i];
      }
    }

    // 计算桶的数量
    int bucketCount = (maxValue - minValue) / bucketSize + 1;
    // 桶
    int[][] buckets = new int[bucketCount][bucketSize];
    // 额外数组记录每个桶的实际元素个数
    int[] indexArr = new int[bucketCount];

    for (int i = 0; i < arr.length; i++) {
      // 计算桶索引
      int bucketIndex = (arr[i] - minValue) / bucketSize;
      // 判断桶是否需要扩容
      if (indexArr[bucketIndex] == buckets[bucketIndex].length) {
        // 执行桶扩容
        ensureCapacity(buckets, bucketIndex);
      }
      // 将元素放入桶内
      buckets[bucketIndex][indexArr[bucketIndex]++] = arr[i];
    }

    // 对每个桶中的元素进行排序
    int k = 0;
    for (int i = 0; i < buckets.length; i++) {
      // 桶中的元素数量为0 继续下一个桶排序
      if (indexArr[i] == 0) {
        continue;
      }
      // 对桶中的元素使用快速排序
      quickSortC(buckets[i], 0, indexArr[i] - 1);
      // 将排好序的桶的元素依次放入原始数组中
      for (int j = 0; j < indexArr[i]; j++) {
        arr[k++] = buckets[i][j];
      }
    }

  }

  /**
   * 桶扩容
   *
   * @param buckets     桶数组
   * @param bucketIndex 桶索引
   */
  private static void ensureCapacity(int[][] buckets, int bucketIndex) {
    int[] tmpArr = buckets[bucketIndex];
    int[] newBucket = new int[tmpArr.length * 2];
    // 复制数据
    for (int i = 0; i < tmpArr.length; i++) {
      newBucket[i] = tmpArr[i];
    }
    buckets[bucketIndex] = newBucket;
  }

  /**
   * 快速排序递归函数
   *
   * @param arr 数组
   * @param p   开始索引
   * @param r   结束索引
   */
  private static void quickSortC(int[] arr, int p, int r) {
    if (p >= r) {
      return;
    }
    int q = partition(arr, p, r);
    quickSortC(arr, p, q - 1);
    quickSortC(arr, q + 1, r);
  }

  private static int partition(int[] arr, int p, int r) {
    //  分区值
    int pivot = arr[r];
    int i = p;
    int j = p;
    for (; j < r; j++) {
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
