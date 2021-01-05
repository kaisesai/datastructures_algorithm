package com.kaige.datastructure.ch_39_backtrack;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 39-2 回溯算法经典应用——0-1 背包问题
 *
 * <p>问题描述：0-1 背包问题有很多变体，我这里介绍一种比较基础的。我们有一个背包，背包总的承载重量是 Wkg。 现在我们有 n 个物品，每个物品的重量不等，并且不可分割。
 * 我们现在期望选择几件物品，装载到背包中。在不超过背包所能装载重量的前提下，如何让背包中物品的总重量最大？
 *
 * <p>解题思路：对于每个物品来说，都有两种选择，装进背包或者不装进背包。对于 n 个物品来说，总的装法就有 2^n 种，去掉总重量超过 Wkg 的，从剩下的装法中选择总重量最接近 Wkg 的。
 * 不过，我们如何才能不重复地穷举出这 2^n 种装法呢？
 *
 * <p>这里就可以用回溯的方法。我们可以把物品依次排列，整个问题就分解为了 n 个阶段，每个阶段对应一个物品怎么选择。 先对第一个物品进行处理，选择装进去或者不装进去，然后再递归地处理剩下的物品。
 *
 * <p>这里还稍微用到了一点搜索剪枝的技巧，就是当发现已经选择的物品的重量超过 Wkg 之后，我们就停止继续探测剩下的物品。
 */
public class Backtrack01Pack {

  /**
   * 物品重量信息数组
   *
   * <p>下标表示物品索引，值表示物品的重量
   */
  private final ItemInfo[] items;

  /**
   * 物品总个数
   */
  private final int itemNum;

  /**
   * 背包可承受的最大重量
   */
  private final int backMaxWeight;
  
  /**
   * 存储可以背包中物品总重量的最大值
   */
  private int maxW = 0;
  
  /**
   * 存储可以背包中物品总价值的最大值
   */
  private int maxV = 0;
  
  private Backtrack01Pack(ItemInfo[] itemInfos, int itemNum, int backMaxWeight) {
    items = itemInfos;
    this.itemNum = itemNum;
    this.backMaxWeight = backMaxWeight;
  }
  
  public static void main(String[] args) {
    // 物品个数
    int itemNum = 10;
    // 物品重量
    ItemInfo[] itemInfos = new ItemInfo[itemNum];
    // 初始化物品重量
    // for (int i = 0; i < itemInfos.length; i++) {
    //   // 物品价值
    //   int value = RandomUtils.nextInt(10, 1000);
    //   // 物品重量
    //   int weight = RandomUtils.nextInt(1, 100);
    //   itemInfos[i] = new ItemInfo(value, weight);
    // }

    /*
     {价值=75元, 重量=27kg}
     {价值=404元, 重量=78kg}
     {价值=343元, 重量=84kg}
     {价值=588元, 重量=89kg}
     {价值=242元, 重量=86kg}
     {价值=429元, 重量=63kg}
     {价值=154元, 重量=51kg}
     {价值=152元, 重量=76kg}
     {价值=52元, 重量=45kg}
     {价值=626元, 重量=60kg}
    */
    itemInfos[0] = new ItemInfo(75, 27); // 单价：2.7
    itemInfos[1] = new ItemInfo(404, 78); // 单价：5.1
    itemInfos[2] = new ItemInfo(343, 84); // 单价：4.8
    itemInfos[3] = new ItemInfo(588, 89); // 单价：6.6
    itemInfos[4] = new ItemInfo(242, 86); // 单价：2.8
    itemInfos[5] = new ItemInfo(429, 63); // 单价：6.8
    itemInfos[6] = new ItemInfo(154, 51); // 单价：3
    itemInfos[7] = new ItemInfo(152, 76); // 单价：2
    itemInfos[8] = new ItemInfo(52, 45); // 单价：1.15
    itemInfos[9] = new ItemInfo(626, 60); // 单价：10.4

    System.out.println("商品信息：" + Arrays.toString(itemInfos));

    // 背包可承受的最大重量
    int backMaxWeight = 100;
    Backtrack01Pack pack = new Backtrack01Pack(itemInfos, itemNum, backMaxWeight);
    Pair<Integer, Integer> weightValuePair = pack.loadPackMaxWeight(true);

    String info = String
      .format("背包信息，物品个数=%s, 背包容量=%skg, 最可装载的重量=%skg, 最大价值=%s元", itemNum, backMaxWeight,
              weightValuePair.getLeft(), weightValuePair.getRight());
    System.out.println(info);
    /*
     商品信息：[{价值=75元, 重量=27kg}, {价值=404元, 重量=78kg}, {价值=343元, 重量=84kg}, {价值=588元, 重量=89kg}, {价值=242元, 重量=86kg}, {价值=429元, 重量=63kg}, {价值=154元, 重量=51kg}, {价值=152元, 重量=76kg}, {价值=52元, 重量=45kg}, {价值=626元, 重量=60kg}]
     当前背包中的物品信息：[]，背包中的总价值：0元，总重量：0kg
     当前背包中的物品信息：[{价值=626元, 重量=60kg}]，背包中的总价值：626元，总重量：60kg
     当前背包中的物品信息：[{价值=52元, 重量=45kg}]，背包中的总价值：52元，总重量：45kg
     当前背包中的物品信息：[{价值=152元, 重量=76kg}]，背包中的总价值：152元，总重量：76kg
     当前背包中的物品信息：[{价值=154元, 重量=51kg}]，背包中的总价值：154元，总重量：51kg
     当前背包中的物品信息：[{价值=154元, 重量=51kg}, {价值=52元, 重量=45kg}]，背包中的总价值：206元，总重量：96kg
     当前背包中的物品信息：[{价值=429元, 重量=63kg}]，背包中的总价值：429元，总重量：63kg
     当前背包中的物品信息：[{价值=242元, 重量=86kg}]，背包中的总价值：242元，总重量：86kg
     当前背包中的物品信息：[{价值=588元, 重量=89kg}]，背包中的总价值：588元，总重量：89kg
     当前背包中的物品信息：[{价值=343元, 重量=84kg}]，背包中的总价值：343元，总重量：84kg
     当前背包中的物品信息：[{价值=404元, 重量=78kg}]，背包中的总价值：404元，总重量：78kg
     当前背包中的物品信息：[{价值=75元, 重量=27kg}]，背包中的总价值：75元，总重量：27kg
     当前背包中的物品信息：[{价值=75元, 重量=27kg}, {价值=626元, 重量=60kg}]，背包中的总价值：701元，总重量：87kg
     当前背包中的物品信息：[{价值=75元, 重量=27kg}, {价值=52元, 重量=45kg}]，背包中的总价值：127元，总重量：72kg
     当前背包中的物品信息：[{价值=75元, 重量=27kg}, {价值=154元, 重量=51kg}]，背包中的总价值：229元，总重量：78kg
     当前背包中的物品信息：[{价值=75元, 重量=27kg}, {价值=429元, 重量=63kg}]，背包中的总价值：504元，总重量：90kg

     isNeedMaxValueForWeight 为 false 时，背包信息，物品个数=10, 背包容量=100kg, 最可装载的重量=96kg, 最大价值=701元
     isNeedMaxValueForWeight 为 true 时， 背包信息，物品个数=10, 背包容量=100kg, 最可装载的重量=87kg, 最大价值=701元
    */
  }
  
  /**
   * 求背包中可以装的最大重量和允许的最大价值
   *
   * @param isNeedMaxValueForWeight 是否求最大价值下的重量，如果为 true，则返回在背包可承载的重量一定的情况下，最大价值下对应的总重量；如果为
   *                                false，则返回背包可承载的重量一定的情况下，能装载的最大重量，和能支持的最大价值（最大重量和最大价值没有关联）。
   * @return 最大重量，最大价值
   */
  private Pair<Integer, Integer> loadPackMaxWeight(boolean isNeedMaxValueForWeight) {
    packMaxWeight(0, 0, 0, Collections.emptyList(), isNeedMaxValueForWeight);
    return Pair.of(maxW, maxV);
  }

  /**
   * 求背包中可以装的最大重量
   *
   * @param i                       当前考察到哪个商品
   * @param cw                      当前已经装进背包的物品的重量和
   * @param cv                      当前已经装进背包的物品的价值和
   * @param cItems                  当前背包的物品信息
   * @param isNeedMaxValueForWeight
   * @desc 假设背包可承受的重量是 100，物品个数 10，物品重量存储在数组 a 中，可这样调用函数 f(0, 0, a, 10, 100)
   */
  private void packMaxWeight(int i, int cw, int cv, List<ItemInfo> cItems,
                             boolean isNeedMaxValueForWeight) {

    if (i == itemNum || cw == backMaxWeight) {
      // i == n 表示已经考察完所有的物品，cw == w 表示已经装满

      if (isNeedMaxValueForWeight) {
        // 背包信息，物品个数=10, 背包容量=100kg, 最大可装载的重量=87kg, 最大价值=701元
        if (cw > maxW && cv > maxV) {
          maxW = cw;
          maxV = cv;
        }
      } else {
        // 背包信息，物品个数=10, 背包容量=100kg, 最大可装载的重量=96kg, 最大价值=701元
        if (cw > maxW) {
          maxW = cw;
        }
        if (cv > maxV) {
          maxV = cv;
        }
      }

      System.out.println("当前背包中的物品信息：" + cItems + "，背包中的总价值：" + cv + "元，总重量：" + cw + "kg");
      return;
    }

    // 当前背包中物品信息
    List<ItemInfo> newCItems = new ArrayList<>(cItems);

    // 这里很巧妙！
    // 物品不装进背包
    packMaxWeight(i + 1, cw, cv, newCItems, isNeedMaxValueForWeight);

    // 已经超过了背包可承受的最大重量时，就不要再装了
    ItemInfo item = items[i];
    if (cw + item.getWeight() <= backMaxWeight) {
      // 物品装进背包
      newCItems.add(item);
      packMaxWeight(i + 1, cw + item.getWeight(), cv + item.getValue(), newCItems,
                    isNeedMaxValueForWeight);
    }
  }

  /**
   * 物品信息
   */
  @Data
  @AllArgsConstructor
  static class ItemInfo {

    /** 价值 */
    int value;

    /** 重量 */
    private int weight;

    @Override
    public String toString() {
      return "{" + "价值=" + value + "元, 重量=" + weight + "kg}";
    }
  }
}
