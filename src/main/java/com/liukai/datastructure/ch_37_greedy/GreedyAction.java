package com.liukai.datastructure.ch_37_greedy;

import com.google.common.collect.Lists;
import lombok.Data;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 37-1 贪心算法实战
 */
public class GreedyAction {

  static int methodInvokeCount = 0;

  public static void main(String[] args) {
    // 1. 分糖果
    allocationSweets();
    System.out.println();

    // 2. 钱币找零
    coinChange(394);
    // 拥有的钱币数目：[{币值=100, 数量=1}, {币值=50, 数量=8}, {币值=20, 数量=7}, {币值=10, 数量=7}, {币值=5, 数量=0}, {币值=2,
    // 数量=3}, {币值=1, 数量=8}]
    // 394元需要找零钱币数：{1=1, 2=1, 10=0, 20=2, 50=5, 100=0}
    // 剩余拥有的钱币数目：[{币值=100, 数量=0}, {币值=50, 数量=3}, {币值=20, 数量=5}, {币值=10, 数量=7}, {币值=5, 数量=0}, {币值=2,
    // 数量=2}, {币值=1, 数量=7}]
    // 剩余需要找零1元
    System.out.println();

    // 3. 区间覆盖
    sectionCover();
    // 选出的不相交区间的个数：3, sectionMaxNum 方法一共被调用的次数：18
    // 区间信息是：[2=4, 6=8, 8=10]

  }

  /**
   * 贪心算法实例三：区间覆盖
   *
   * <p>描述：假设我们有 n 个区间，区间的起始端点和结束端点分别是[l1, r1]，[l2, r2]，[l3, r3]，……，[ln, rn]。我们从这 n
   * 个区间中选出一部分区间，这部分区间满足两两不相交（端点相交的情况不算相交），最多能选出多少个区间呢？
   *
   * <p>分析：我们假设这 n 个区间中最左端点是 lmin，最右端点是 rmax。我们选择几个不相交的区间，从左到右将 [lmin, rmax] 覆盖上。 按照起始端点从小到大的顺序对这 n
   * 个区间排序。
   *
   * <p>每次选择的时，选择左端点跟前面已经覆盖的区间不重复的的区间，右端点又尽量小，这样可以让剩下的未覆盖区间尽可能的大，就可以放置更多的区间。
   */
  public static void sectionCover() {
    // 将区间按照左端点从小到大排序
    // 例如： [6,8] [2,4] [3,5] [5,9] [8,10]
    // 使用红黑树保存区间信息，自动按照 key 从小到大排序区间信息
    TreeMap<Integer, Integer> section = new TreeMap<>();
    section.put(6, 8);
    section.put(2, 4);
    section.put(3, 5);
    section.put(1, 5);
    section.put(5, 9);
    section.put(8, 10);

    // 遍历每个区间，计算每次循环的最多区间数
    Pair<Integer, List<Map.Entry<Integer, Integer>>> max = Pair.of(0, Collections.emptyList());
    for (Map.Entry<Integer, Integer> entry : section.entrySet()) {
      Pair<Integer, List<Map.Entry<Integer, Integer>>> pair = sectionMaxNum(0, 1, entry, section);
      if (max.getLeft() < pair.getLeft()) {
        max = pair;
      }
    }
    System.out
      .println("选出的不相交区间的个数：" + max.getLeft() + ", sectionMaxNum 方法一共被调用的次数：" + methodInvokeCount);
    System.out.println("区间信息是：" + max.getRight());
  }

  /**
   * 递归获取指定区间之后的最长区间数信息
   *
   * @param index          当前遍历的索引
   * @param sectionNum     当前区间最大数量
   * @param currentSection 当前区间信息
   * @param section        所有区间
   * @return pair，left 是区间的最大数量，right 为各个区间信息
   */
  private static Pair<Integer, List<Map.Entry<Integer, Integer>>> sectionMaxNum(int index,
                                                                                int sectionNum,
                                                                                Map.Entry<Integer, Integer> currentSection,
                                                                                TreeMap<Integer, Integer> section) {

    // 记录方法被调用的次数
    methodInvokeCount++;

    if (index == section.size() - 1) {
      return Pair.of(sectionNum, Collections.singletonList(currentSection));
    }

    // 返回的 List 区间，为当前区间之后，所有可用的区间
    List<Map.Entry<Integer, Integer>> path = Lists.newArrayList();
    path.add(currentSection);
    List<Map.Entry<Integer, Integer>> subPath = Lists.newArrayList();
    int lastSum = 1;

    int maxSectionNum = sectionNum;
    SortedMap<Integer, Integer> tailMap = section.tailMap(currentSection.getKey());
    if (MapUtils.isNotEmpty(tailMap)) {
      Set<Map.Entry<Integer, Integer>> entries = tailMap.entrySet();
      Iterator<Map.Entry<Integer, Integer>> iterator = entries.iterator();
      int i = index + 1;
      while (iterator.hasNext()) {
        Map.Entry<Integer, Integer> nextSection = iterator.next();
        // 前面已经覆盖的区间与下一个左端点与
        if (currentSection.getValue() <= nextSection.getKey()) {
          // 返回一个该区间下，一共满足条件的区间信息
          Pair<Integer, List<Map.Entry<Integer, Integer>>> pair = sectionMaxNum(i, sectionNum + 1,
                                                                                nextSection,
                                                                                section);
          // 子路径区间数
          Integer subSectionNum = pair.getLeft();
          if (maxSectionNum < subSectionNum) {
            // 子路径区间相差值之和
            int sum = getSectionSum(currentSection, pair);
            subPath = pair.getRight();
            lastSum = sum;
            maxSectionNum = subSectionNum;
          } else if (maxSectionNum == subSectionNum) {
            // 子路径区间相差值之和
            int sum = getSectionSum(currentSection, pair);
            // 区间数相同，比较区间相差值
            if (sum > lastSum) {
              subPath = pair.getRight();
              lastSum = sum;
            }
          }
        }
        i++;
      }
    }
    path.addAll(subPath);
    return Pair.of(maxSectionNum, path);
  }

  private static int getSectionSum(Map.Entry<Integer, Integer> currentSection,
                                   Pair<Integer, List<Map.Entry<Integer, Integer>>> pair) {
    int sum = 0;
    int lastEnd = currentSection.getValue();
    for (Map.Entry<Integer, Integer> entry : pair.getRight()) {
      sum += (entry.getKey() - lastEnd);
      lastEnd = entry.getValue();
    }
    return sum;
  }

  /**
   * 贪心算法实例二：钱币找零
   *
   * <p>描述：假设我们有 1 元、2 元、5 元、10 元、20 元、50 元、100 元这些面额的纸币，它们的张数分别是
   * c1、c2、c5、c10、c20、c50、c100。我们现在要用这些钱来支付 K 元，最少要用多少张纸币呢？
   *
   * <p>分析：限制值是 K 元，期望值是钱币张数量，在同等限制值下贡献量要最大的，即钱币的价值最大。
   *
   * @param k 需要找零的金额
   */
  private static void coinChange(int k) {

    // 随机生成拥有的钱币数量
    int[] coinValue = {1, 2, 5, 10, 20, 50, 100};

    // 随机生成钱币
    List<Coin> coins = Arrays.stream(coinValue)
      .mapToObj(value -> new Coin(value, RandomUtils.nextInt(0, 10))).collect(Collectors.toList());

    // 将钱币按照价值从高到低排序
    coins.sort(Comparator.comparingInt(Coin::getValue).reversed());

    System.out.println("拥有的钱币数目：" + coins);

    // 开始找零
    int surplusMoney = k;
    Map<Integer, Integer> consumerCoin = new TreeMap<>();
    for (Coin coin : coins) {
      if (coin.num == 0) {
        continue;
      }
      // 开始找零钱
      // 需要的张数
      int needNum = k / coin.value;
      while (needNum != 0 && coin.num != 0) {
        consumerCoin.compute(coin.value, (key, value) -> (value == null) ? 0 : value + 1);
        int money = surplusMoney - coin.value;
        if (money <= 0) {
          break;
        }
        surplusMoney = money;
        coin.num--;
        needNum--;
      }
    }

    System.out.println(k + "元需要找零钱币数：" + consumerCoin);
    System.out.println("剩余拥有的钱币数目：" + coins);
    System.out.println("剩余需要找零" + surplusMoney + "元");
  }

  /**
   * 贪心算法实例一：分配糖果
   *
   * <p>描述：有 n 个孩子，和 m 个糖果。其中孩子数量大于糖果数量（n > m），每个糖果的大小不一样，每个孩子对糖果大小的需求也不一样， 如何分配糖果可以让分配给更多的孩子呢？
   *
   * <p>分析：根据贪心算法分析，将问题看作是，从 n 个孩子中选出一部分孩子，分配糖果。
   *
   * <p>限制值是糖果数量 m，期望值是孩子数量，在限制值一定的情况下，同等限制值下贡献量要大，期望值最大，也就是孩子的数量最大。
   *
   * <p>方法：按照孩子需求量最小的分配
   */
  private static void allocationSweets() {
    // 一组孩子
    int childNum = 10;
    int[] children = new int[childNum];
    for (int i = 0; i < childNum; i++) {
      // 孩子对糖果尺寸的大小需求
      children[i] = RandomUtils.nextInt(10, 20);
    }

    // 一组糖果
    int sweetNum = 5;
    List<Integer> sweets = Lists.newArrayListWithCapacity(sweetNum);
    for (int i = 0; i < sweetNum; i++) {
      // 糖果尺寸大小
      sweets.add(RandomUtils.nextInt(1, 20));
    }

    // 先对孩子需求从小到大排序
    Arrays.sort(children);

    // 将糖果从小到大排序
    sweets.sort(null);

    List<Integer> result = Lists.newArrayList();

    // 分配糖果
    for (int child : children) {
      ListIterator<Integer> iterator = sweets.listIterator();
      while (iterator.hasNext()) {
        Integer sweetSize = iterator.next();
        System.out.println("孩子的需求：" + child + "，糖果的大小：" + sweetSize);
        if (sweetSize >= child) {
          result.add(sweetSize);
          iterator.remove();
          System.out.println("满足孩子的需求");
          break;
        }
      }
    }

    System.out.println("分配的孩子有" + result.size() + "个，需求为：" + result);
  }

  @Data
  static class Coin {

    /**
     * 价值
     *
     * <p>比如。1 元、2 元、5 元等等
     */
    int value;

    /**
     * 数量
     */
    int num;

    public Coin(int value, int num) {
      this.value = value;
      this.num = num;
    }

    @Override
    public String toString() {
      return "{" + "币值=" + value + ", 数量=" + num + '}';
    }

  }

}
