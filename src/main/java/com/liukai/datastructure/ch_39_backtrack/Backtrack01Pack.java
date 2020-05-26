package com.liukai.datastructure.ch_39_backtrack;

import org.apache.commons.lang3.RandomUtils;

import java.util.Arrays;

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
  private final int[] items;

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

  public Backtrack01Pack(int[] itemWeights, int itemNum, int backMaxWeight) {
    this.items = itemWeights;
    this.itemNum = itemNum;
    this.backMaxWeight = backMaxWeight;
  }

  public static void main(String[] args) {

    // 物品个数
    int itemNum = 10;

    // 物品重量
    int[] itemWeights = new int[itemNum];
    // 初始化物品重量
    for (int i = 0; i < itemWeights.length; i++) {
      itemWeights[i] = RandomUtils.nextInt(1, 100);
    }
    // 背包可承受的最大重量
    int backMaxWeight = 50;

    Backtrack01Pack pack = new Backtrack01Pack(itemWeights, itemNum, backMaxWeight);

    pack.loadPackMaxWeight();

    System.out.println(pack);
    // Backtract01Pack{物品重量信息=[40, 52, 41, 98, 4, 45, 88, 12, 45, 17], 物品个数=10, 背包容量=50kg,
    // 最大可装载的重量=49kg}
  }

  /**
   * 求背包中可以装的最大重量
   */
  public void loadPackMaxWeight() {
    packMaxWeight(0, 0);
  }

  /**
   * 求背包中可以装的最大重量
   *
   * @param i  当前考察到哪个商品
   * @param cw 当前已经装进背包的物品的重量和
   * @desc 假设背包可承受的重量是 100，物品个数 10，物品重量存储在数组 a 中，可这样调用函数 f(0, 0, a, 10, 100)
   */
  private void packMaxWeight(int i, int cw) {

    if (i == itemNum || cw == backMaxWeight) {
      // i == n 表示已经考察完所有的物品，cw == w 表示已经装满
      if (cw > maxW) {
        maxW = cw;
      }
      return;
    }

    // 这里很巧妙！
    // 物品不装进背包
    packMaxWeight(i + 1, cw);

    // 已经超过了背包可承受的最大重量时，就不要再装了
    if (cw + items[i] <= backMaxWeight) {
      // 物品装进背包
      packMaxWeight(i + 1, cw + items[i]);
    }
  }

  @Override
  public String toString() {
    return "Backtract01Pack{" + "物品重量信息=" + Arrays.toString(items) + ", 物品个数=" + itemNum + ", 背包容量="
      + backMaxWeight + "kg, 最大可装载的重量=" + maxW + "kg}";
  }

}
