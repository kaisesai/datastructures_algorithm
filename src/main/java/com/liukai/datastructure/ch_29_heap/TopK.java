package com.liukai.datastructure.ch_29_heap;

import java.util.*;

/**
 * 29-3 堆应用二：求 Top K 数据
 * <p>
 * 描述：Top K 问题可以抽象为静态数据和动态数据两类。
 * <p>
 * 1. 针对静态数据，对于一个包含 n 个数据的数组，我们可以维护一个大小为 K 的小顶堆，顺序遍历数组，从数组中取出数据与堆顶元素比较。
 * 如果比堆顶元素大，我们就把堆顶元素删除，并且将这个元素插入到堆中；如果比堆顶元素小，则不做处理，继续遍历数组。
 * 这样等数组中的数据都遍历完之后，堆中的数据就是前 K 大数据了。
 * <p>
 * 遍历数组需要 O(n) 的时间复杂度，一次堆化操作需要 O(logK) 的时间复杂度，所以最坏情况下，n 个元素都入堆一次，时间复杂度就是 O(nlogK)。
 * <p>
 * 2. 针对动态数据求得 Top K 就是实时 Top K。我们维护一个大小为 K 的小顶堆，当数据被添加到集合后，我们将该数据添加到堆中。
 * 当堆满之后，数据比堆顶的元素大时则删除堆顶元素，且将该数据加入到堆中；如果数据比堆顶元素小，则不作处理。这样任意时刻，堆中的数据就是 Top K 大数据。
 * <p>
 */
public class TopK {

  public static final int HEAP_CAPACITY = 10;

  /**
   * 小顶堆
   */
  private final PriorityQueue<Integer> heap = new PriorityQueue<>(HEAP_CAPACITY);

  private final List<Integer> data = new ArrayList<>(100);

  public static void main(String[] args) {

    Random random = new Random();
    TopK topK = new TopK();
    for (int i = 0; i < 20; i++) {
      topK.add(random.nextInt(1000));
    }

    topK.printData();

    List<Integer> staticData = new ArrayList<>(20);
    for (int i = 0; i < 20; i++) {
      staticData.add(random.nextInt(1000));
    }

    List<Integer> topKForStaticData = topKForStaticData(5, staticData);
    System.out.println("静态数据：" + staticData);
    System.out.println("静态数据 Top K 大元素：" + topKForStaticData);

  }

  /**
   * 静态数据 Top K 大数据
   *
   * @param k    Top K 值
   * @param data 静态数据
   * @return Top K 大数据
   */
  public static List<Integer> topKForStaticData(int k, List<Integer> data) {
    PriorityQueue<Integer> queue = new PriorityQueue<>(k);
    for (Integer value : data) {
      if (queue.size() < k) {
        queue.add(value);
      } else if (queue.peek() != null && value > queue.peek()) {
        queue.poll();
        queue.add(value);
      }
    }
    return Arrays.asList(queue.toArray(new Integer[0]));
  }

  /**
   * 动态数据 Top K 的数据（未排序数据）
   *
   * @return 返回未排序的 top k 数据
   */
  public List<Integer> topK() {
    Integer[] integers = heap.toArray(new Integer[0]);
    // 可以不排序
    // Arrays.sort(integers, (o1,o2)-> o2-o1);
    return Arrays.asList(integers);
  }

  /**
   * 添加数据
   * <p>
   * 维护小顶堆
   *
   * @param value 数据
   */
  public void add(Integer value) {
    data.add(value);
    if (heap.size() < HEAP_CAPACITY) {
      // 堆未满
      heap.add(value);
    } else if (value > heap.peek()) {
      // 堆满，新元素是否大于堆顶元素
      heap.poll();
      heap.add(value);
    }
  }

  public void printData() {
    System.out.println("集合元素：" + data);
    System.out.println("集合 Top K 大元素：" + topK());
  }

}
