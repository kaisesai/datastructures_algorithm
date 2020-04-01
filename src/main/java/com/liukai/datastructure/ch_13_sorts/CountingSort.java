package com.liukai.datastructure.ch_13_sorts;

/**
 * 13=2 计数排序
 * <p>
 * 核心思想：当要排序的 n 个数据，所处的范围并不大的时候，比如最最大值是 k，我们就可以把数据划分成 k 个桶。
 * 每个桶内的数据值都是相同的，省掉了桶内排序的时间。
 * <p>
 * 具体实现：将要排序的数据，使用单独的一组计数数组进行统计每个元素对应的的个数，这个计数数组的索引值为排序数组的元素值，
 * 对饮的值为元素值对应的个数，之后依次累加计数数组，这样得到值就是每个小于等于元素的个数值。最后依次遍历要排序的额元素，
 * 从计数数组中取出来的元素个数就是这个元素对应的排序的索引，之后将计数数组索引对应值减一。以此类推，遍历完成之后所有的
 * 元素就是有序的了。
 * <p>
 * 特点：
 * 1. 时间复杂度 O(n)
 * 2. 适用场景：排序数据的数据量大，但是范围较小。而且数据得是非负整数。
 */
public class CountingSort {

  /**
   * 计数排序
   *
   * @param arr 要排序的数组
   */
  public static void countingSort(int[] arr) {
    if (arr == null || arr.length < 2) {
      return;
    }

    // 查找数组中数据的范围
    int max = arr[0];
    for (int i = 0; i < arr.length; i++) {
      if (arr[i] > max) {
        max = arr[i];
      }
    }
    // 申请一个计数数组 c，下标大小为 [0,max]
    int[] c = new int[max + 1];

    // 计算每个元素的个数
    for (int i = 0; i < arr.length; i++) {
      c[arr[i]]++;
    }

    // 依次累加
    for (int i = 1; i < c.length; i++) {
      c[i] = c[i] + c[i - 1];
    }

    // 临时数组 r，存储排序之后的结果
    int[] r = new int[arr.length];

    // 计算排序的关键步骤
    for (int i = 0; i < arr.length; i++) {
      int count = c[arr[i]];
      // 设置元素的排序索引
      r[count - 1] = arr[i];
      // 将元素对应的数量减一
      c[arr[i]]--;
    }

    // 将结果拷贝到 a 数组
    System.arraycopy(r, 0, arr, 0, r.length);
  }

}
