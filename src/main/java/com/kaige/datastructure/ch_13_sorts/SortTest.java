package com.kaige.datastructure.ch_13_sorts;

import com.kaige.datastructure.ch_11_sorts.Sorts;
import com.kaige.datastructure.ch_12_sorts.MergeSort;
import com.kaige.datastructure.ch_12_sorts.QuickSort;

import java.util.Random;

/**
 * 13-5 排序测试类
 */
public class SortTest {
  
  private static final int arrLength = 1_00;
  
  private static final int intBound = 1_00_000;
  
  private static long start;
  
  public static void main(String[] args) {
    
    // 随机创建一个很大的随机数组
    // 100 万个元素
    Random random = new Random();
    int[] bigArr = new int[arrLength];
    for (int i = 0; i < bigArr.length; i++) {
      bigArr[i] = random.nextInt(intBound);
    }
    // System.out.println(Arrays.toString(bigArr));
    start = System.currentTimeMillis();

    // 归并排序	arrLength = 1_000_000	intBound = 100_000	耗时：216ms
    // 归并排序	arrLength = 10_000_000	intBound = 100_000	耗时：1680ms
    // testMergeSort(bigArr);

    // 快速排序	arrLength = 1000_000	intBound = 100_000	耗时：83ms
    // 快速排序	arrLength = 10_000_000	intBound = 100_000	耗时：847ms
    testQuickSort(bigArr);

    // 桶排序	arrLength = 1_000_000	intBound = 100_000	bucketSize = 1000	耗时：71ms
    // 桶排序	arrLength = 10_000_000	intBound = 100_000	bucketSize = 1000	耗时：860ms
    // testBucketSort(bigArr);

    // 计数排序	arrLength = 1_000_000	intBound = 100_000	耗时：15ms
    // 计数排序	arrLength = 10_000_000	intBound = 100_000	耗时：110ms
    // testCountingSort(bigArr);

    // 基数排序	arrLength = 1_000_000	intBound = 100_000	耗时：58ms
    // 基数排序	arrLength = 10_000_000	intBound = 100_000	耗时：473ms
    // testRadixSort(bigArr);
    // System.out.println(Arrays.toString(bigArr));

  }

  private static void testRadixSort(int[] bigArr) {
    RadixSort.radixSort(bigArr);
    System.out.println("基数排序\tarrLength = " + arrLength + "\tintBound = " + intBound + "\t耗时：" + (
      System.currentTimeMillis() - start) + "ms");
  }

  private static void testCountingSort(int[] bigArr) {
    CountingSort.countingSort(bigArr);
    System.out.println("计数排序\tarrLength = " + arrLength + "\tintBound = " + intBound + "\t耗时：" + (
      System.currentTimeMillis() - start) + "ms");
  }

  // 桶排序	arrLength = 1000000	intBound = 100000000	bucketSize = 1000	耗时：449ms
  // 桶排序	arrLength = 1000000	intBound = 10000000	bucketSize = 1000	耗时：87ms
  // 桶排序	arrLength = 1000000	intBound = 1000000	bucketSize = 1000	耗时：69ms
  // 桶排序	arrLength = 1000000	intBound = 100000	bucketSize = 1000	耗时：94ms
  private static void testBucketSort(int[] bigArr) {
    int bucketSize = 1_000;
    BucketSort.bucketSort(bigArr, bucketSize);
    System.out.println(
      "桶排序\tarrLength = " + arrLength + "\tintBound = " + intBound + "\tbucketSize = " + bucketSize
        + "\t耗时：" + (System.currentTimeMillis() - start) + "ms");
  }

  // 快速排序	arrLength = 1000000	intBound = 100000000	耗时：122ms
  // 快速排序	arrLength = 1000000	intBound = 1000000	耗时：93ms
  // 快速排序	arrLength = 1000000	intBound = 10000000	耗时：95ms
  private static void testQuickSort(int[] bigArr) {
    QuickSort.quickSort(bigArr);

    System.out.println("快速排序\tarrLength = " + arrLength + "\tintBound = " + intBound + "\t耗时：" + (
      System.currentTimeMillis() - start) + "ms");
  }

  // 归并排序	arrLength = 1000000	intBound = 100000000	耗时：223ms
  private static void testMergeSort(int[] bigArr) {
    MergeSort.mergeSort(bigArr);
    System.out.println("归并排序\tarrLength = " + arrLength + "\tintBound = " + intBound + "\t耗时：" + (
      System.currentTimeMillis() - start) + "ms");
  }

  private static void testInsertionSort(int[] bigArr) {
    Sorts.insertionSort(bigArr, arrLength);
    System.out.println(" 插入排序\tarrLength = " + arrLength + "\tintBound = " + intBound + "\t耗时：" + (
      System.currentTimeMillis() - start) + "ms");
  }

}
