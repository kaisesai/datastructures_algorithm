package com.liukai.datastructure.ch_16_binarysearch;

import java.util.Arrays;

/**
 * 16-1 二分查找法
 */
public class BinarySearch {

  public static void main(String[] args) {
    // int[] a = {3, 6, 9, 11, 15, 15, 22, 35, 30, 40};
    int[] a = {5, 1, 3};
    System.out.println("数据：" + Arrays.toString(a));

    int value = 5;
    // int index = binarySearch(a, value);
    // int index = binarySearchInternally(a, 0, a.length - 1, value);
    // System.out.println("通过二分查找法，查找元素 value 为 " + value + " 的元素在数组中的位置为：" + index);

    // int index = binarySearchForFirstEqValue(a, value);
    // System.out.println("查找第一个值等于" + value + "的元素，索引为：" + index);

    // index = binarySearchForLastEqValue(a, value);
    // System.out.println("查找最后一个值等于" + value + "的元素， 索引为" + index);

    // index = binarySearchForFirstGeValue(a, value);
    // System.out.println("查找第一个值大于等于" + value + "的元素，索引为" + index);

    // index = binarySearchForLastLeValue(a, value);
    // System.out.println("查找最后一个小于等于" + value + "的元素，索引为" + index);

    int index = binarySearchCirculationArray(a, value);
    System.out.println("查找循环数组中等于" + value + "的元素，索引为" + index);

  }

  /**
   * 力扣 33 题
   * <p>
   * 假设按照升序排序的数组在预先未知的某个点上进行了旋转。
   * <p>
   * ( 例如，数组 [0,1,2,4,5,6,7] 可能变为 [4,5,6,7,0,1,2] )。
   * <p>
   * 搜索一个给定的目标值，如果数组中存在这个目标值，则返回它的索引，否则返回 -1 。
   * <p>
   * 你可以假设数组中不存在重复的元素。
   * <p>
   * 你的算法时间复杂度必须是 O(log n) 级别。
   *
   * @param a     循环数组
   * @param value 目标值
   * @return 给定值在数组中的索引
   */
  public static int binarySearchCirculationArray(int[] a, int value) {
    /*
    假设按照升序排序的数组在预先未知的某个点上进行了旋转。

    ( 例如，数组 [0,1,2,4,5,6,7] 可能变为 [4,5,6,7,0,1,2] )。

    搜索一个给定的目标值，如果数组中存在这个目标值，则返回它的索引，否则返回 -1 。

    你可以假设数组中不存在重复的元素。

    你的算法时间复杂度必须是 O(log n) 级别。
     */
    if (a == null || a.length < 1) {
      return -1;
    }

    int low = 0;
    int high = a.length - 1;
    while (low <= high) {
      int mid = low + ((high - low) >> 1);
      if (a[mid] == value) {
        return mid;
      }

      if (a[low] <= a[mid]) {
        // 前半部分是有序数组，后半部分是循环数组
        // 如果元素在有序数组中,并且前半部分的头元素小等于 value
        if (a[mid] > value && a[low] <= value) {
          high = mid - 1;
        } else {
          low = mid + 1;
        }

      } else {
        // 前半部分是循环数组，后半部分是有序数组
        // value 值大于中间值，并且 value 值小于等于末位置
        if (a[mid] < value && a[high] >= value) {
          low = mid + 1;
        } else {
          high = mid - 1;
        }
      }

    }

    return -1;
  }

  /**
   * 二分查找变体四：查找最后一个小于等于给定值的元素
   *
   * @param a     从小到大排序的数组
   * @param value 给定值
   * @return 数组中给定值的索引
   */
  public static int binarySearchForLastLeValue(int[] a, int value) {
    int low = 0;
    int high = a.length - 1;
    while (low <= high) {
      // value = 2
      // 1,2,4,5, 5,6,8,19
      int mid = low + ((high - low) >> 1);
      if (a[mid] <= value) {
        if (mid == a.length - 1 || (a[mid + 1] > value)) {
          return mid;
        } else {
          low = mid + 1;
        }
      } else {
        high = mid - 1;
      }
    }
    return -1;
  }

  /**
   * 二分查找变体三：查找第一个大于等于给定值的元素
   *
   * @param a     从小到大排序的数组
   * @param value 给定值
   * @return 数组中给定值的索引
   */
  public static int binarySearchForFirstGeValue(int[] a, int value) {
    int low = 0;
    int high = a.length - 1;
    while (low <= high) {
      int mid = low + ((high - low) >> 1);
      if (value > a[mid]) {
        low = mid + 1;
      } else {
        if (mid == 0 || (a[mid - 1] != value)) {
          return mid;
        } else {
          high = mid - 1;
        }
      }
    }

    return -1;
  }

  /**
   * 二分查找变体二：查找最后一个值等于给定值的元素
   *
   * @param a     从小到大排序的数组
   * @param value 给定值
   * @return 数组中给定值的索引
   */
  public static int binarySearchForLastEqValue(int[] a, int value) {
    int low = 0;
    int high = a.length - 1;

    while (low <= high) {
      int mid = low + ((high - low) >> 1);
      if (value > a[mid]) {
        low = mid + 1;
      } else if (value < a[mid]) {
        high = mid - 1;
      } else {
        if (mid == 0 || (a[mid + 1] != value)) {
          return mid;
        } else {
          low = mid + 1;
        }
      }
    }
    return -1;
  }

  /**
   * 二分查找变体一：查找第一个值等于给定值的元素
   *
   * @param a     元素从小到大排序的数组
   * @param value 给定值
   * @return 返回给定值在数组中的索引
   */
  public static int binarySearchForFirstEqValue(int[] a, int value) {
    int low = 0;
    int high = a.length - 1;

    while (low <= high) {
      int mid = low + ((high - low) >> 1);
      if (value > a[mid]) {
        low = mid + 1;
      } else if (value < a[mid]) {
        high = mid - 1;
      } else {
        // mid 为第一个元素，或者它的前一个元素不是 value
        if (mid == 0 || (a[mid - 1] != value)) {
          return mid;
        } else {
          high = mid - 1;
        }
      }
    }
    return -1;
  }

  /**
   * 通过二分查找找出数组中指定的元素
   *
   * @param a     数组
   * @param value 指定元素
   * @return 数组中指定元素的索引
   */
  public static int binarySearch(int[] a, int value) {
    if (a == null) {
      return -1;
    }
    int low = 0;
    int high = a.length - 1;

    while (low <= high) {
      int mid = low + ((high - low) >> 1);
      if (a[mid] == value) {
        return mid;
      } else if (value > a[mid]) {
        low = mid + 1;
      } else {
        high = mid - 1;
      }
    }
    return -1;
  }

  /**
   * 二分查找递归实现
   *
   * @param a     数组
   * @param low   低位索引
   * @param high  高位索引
   * @param value 查找的元素
   * @return 查找元素对应数组的索引
   */
  public static int binarySearchInternally(int[] a, int low, int high, int value) {
    if (low > high) {
      return -1;
    }
    int mid = low + ((high - low) >> 1);
    if (a[mid] == value) {
      return mid;
    } else if (value > a[mid]) {
      return binarySearchInternally(a, mid + 1, high, value);
    } else {
      return binarySearchInternally(a, low, mid - 1, value);
    }
  }

}
