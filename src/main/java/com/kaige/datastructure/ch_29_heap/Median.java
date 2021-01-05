package com.kaige.datastructure.ch_29_heap;

import java.util.*;

/**
 * 29-4 堆应用三：求中位数
 * <p>
 * 描述：数据的中位数，就是数组排好序之后的中间位置的数据。如果数组大小为 n，它是奇数是，它的中位数就是 n/2 + 1，
 * 如果是偶数，则它的中位数有两个 n/2，n/2+1，我们可以取里数据开头近的数据 n/2。
 * <p>
 * 数据可以分为静态数据和动态数据。
 * <p>
 * 针对静态数据
 * 1. 我们可以先排序，然后去中间位置的数。
 * 2. 也可以使用两个堆，一个大顶堆，一个小顶堆来存储这些数据，各存储一半数据。大顶堆中的数据小于小顶堆的数据
 * 大顶堆堆顶的数据就是中位数。
 * <p>
 * 针对动态数据，数据会不停的往集合中添加或者删除，所以我们需要动态的维护两个堆的数据平衡
 * 1. 维护两个堆，大顶堆和小顶堆。大顶堆的数据小于小顶堆。
 * 2. 当有数据添加到集合后，根据数据大小添加到对应的堆中，最后调整两个堆中数据比例。
 * <p>
 * 扩展：可以其他百分位数，比如 99% 数，90%等。
 */
public class Median {

  private final PriorityQueue<Integer> bigTopHeap = new PriorityQueue<>(50, Comparator
    .comparingInt(o -> (int) o).reversed());

  private final PriorityQueue<Integer> smallTopHeap = new PriorityQueue<>(50, Comparator
    .comparingInt(o -> o));

  public static void main(String[] args) {
    Median median = new Median();

    median.addData(3);
    median.addData(19);
    median.addData(10);
    median.addData(6);
    median.addData(8);
    median.addData(5);
    median.addData(1);
    median.addData(200);

    System.out.println("数据元素：" + median.data());
    System.out.println("中位数：" + median.median());

    median.delete(6);
    median.delete(10);

    System.out.println("数据元素：" + median.data());
    System.out.println("中位数：" + median.median());
  }

  public List<Integer> data() {
    List<Integer> list1 = Arrays.asList(bigTopHeap.toArray(new Integer[0]));
    List<Integer> list2 = Arrays.asList(smallTopHeap.toArray(new Integer[0]));
    ArrayList<Integer> list = new ArrayList<>(list1.size() + list2.size());
    list.addAll(list1);
    list.addAll(list2);
    list.sort(Comparator.comparingInt(o -> o));
    return list;
  }

  /**
   * @return 中位数
   */
  public Integer median() {
    return bigTopHeap.peek();
  }

  /**
   * 删除数据
   *
   * @param value 数据
   * @return 是否删除成功
   */
  public boolean delete(Integer value) {
    boolean result = bigTopHeap.remove(value);
    if (!result) {
      result = smallTopHeap.remove(value);
    }
    if (result) {
      // 调整两个堆数量比例
      adjustHeapRatio();
    }
    return result;
  }

  /**
   * 插入元素
   *
   * @param value 数据
   */
  public void addData(Integer value) {
    // 将所有的元素都加入到大顶堆
    bigTopHeap.add(value);
    // 两个堆中数量差
    adjustHeapRatio();
  }

  /**
   * 调整两个堆的比例
   * <p>
   * 如果两个堆中的数量差值 n 大于 1，则从数量大的堆搬移 n - 1 个数据到数据量小的堆
   */
  private void adjustHeapRatio() {
    int diffSize = bigTopHeap.size() - smallTopHeap.size();
    // 调整比例
    if (diffSize > 0) {
      // 大顶堆中的数据多，那就将大顶堆中的数据移动到小顶堆中
      // 相差奇数时，则移动 n /2 + 1 个元素到小顶堆
      // 相差偶数时，移动 n/2 个元素到小顶堆
      for (int i = diffSize / 2; i > 0; i--) {
        smallTopHeap.add(bigTopHeap.poll());
      }
    } else if (diffSize < -1) {
      // 小顶堆中的数据多，那就将小顶堆中的数据移动到大顶堆中
      for (int i = Math.abs(diffSize) / 2; i > 0; i--) {
        bigTopHeap.add(smallTopHeap.poll());
      }
    }
  }

}
