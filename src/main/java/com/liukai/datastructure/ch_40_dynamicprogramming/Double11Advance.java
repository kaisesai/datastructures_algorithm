package com.liukai.datastructure.ch_40_dynamicprogramming;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * 40-2 动态规划——双十一满减凑单
 *
 * <p>问题描述：淘宝的“双十一”购物节有各种促销活动，比如“满 200 元减 50 元”。假设你的购物车中有 n 个（n>100）想买的商品，
 * 你希望从里面选几个，在凑够满减条件的前提下，让选出来的商品价格总和最大程度地接近满减条件（200 元），这样就可以极大限度地“薅羊毛”。能不能编个代码来搞定呢？
 */
public class Double11Advance {

  public static void main(String[] args) {

    // 满减金额
    int w = 200;
    // 商品个数
    int n = 100;
    // 商品
    // int[] items = IntStream.range(0, n).map(i -> RandomUtils.nextInt(1, 100)).toArray();
    int[] items = IntStream.range(1, n + 1).toArray();

    System.out.println("商品：" + Arrays.toString(items));

    double11Advance(items, n, w);
    /*
      商品：[1, 2, 3, 4, 5, 6, 7, 8, 9,......, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100]
      最优的凑单满 200 元减免优惠的商品：100 99 1
     */
  }

  /**
   * 要求不仅要求大于等于 200 的总价格中的最小的，我们还要找出这个最小总价格对应都要购买哪些商品。
   *
   * <p>思路：使用动态规划，维护二维数组 states[n][x]，来记录每次决策之后所有可达的状态。同时利用这个二维数组倒推出这个被选择的商品序列（也可以优化成一维数组）。
   *
   * <p>0-1 背包问题中，我们找是小于等于 w 的最大值，x 就是背包中的最大承载重量 w + 1。对于这个问题来说，我们要找的是 大于等于 200（满减条件）的值中最小的，所以就不能设置为
   * 200 加 1.就这个实际问题而言，如果要购买的物品的总价格超过 200 太多， 比如 1000，那这个羊毛“薅”的就没有太大意义了，所以，我们可以限定 x 值为 1001。
   *
   * @param items 商品价格
   * @param n     商品数量
   * @param w     所能承受支付的最大金额
   */
  public static void double11Advance(int[] items, int n, int w) {
    int maxW = 3 * w + 1;
    // 状态数组，超过3倍就没有薅羊毛的价值了
    // boolean[][] states = new boolean[n][maxW];
    boolean[] states = new boolean[maxW];

    // 第一行数据特殊处理
    // states[0][0] = true;
    // if (items[0] <= maxW) {
    //   states[0][items[0]] = true;
    // }

    states[0] = true;
    if (items[0] <= maxW) {
      states[items[0]] = true;
    }

    for (int i = 1; i < n; i++) {
      // 动态规划
      // for (int j = 0; j < maxW; j++) {
      //   // 不购买第 i 个商品
      //   if (states[i - 1][j]) {
      //     states[i][j] = true;
      //   }
      // }
      //
      // for (int j = 0; j < (maxW - items[i]); j++) {
      //   // 购买第 i 个商品
      //   if (states[i - 1][j]) {
      //     states[i][j + items[i]] = true;
      //   }
      // }

      for (int j = maxW - items[i] - 1; j >= 0; j--) {
        // 购买第 i 个商品
        if (states[j]) {
          states[j + items[i]] = true;
        }
      }
    }

    // 输出结果大于等于 w 的最小值
    int j;
    for (j = w; j < maxW; j++) {
      //   if (states[n - 1][j]) {
      if (states[j]) {
        break;
      }
    }

    if (j == maxW) {
      // 没有可行解
      return;
    }

    System.out.print("最优的凑单满 200 元减免优惠的商品：");
    for (int i = n - 1; i > 0; i--) {
      //
      // if (j - items[i] >= 0 && states[i - 1][j - items[i]]) {
      if (j - items[i] >= 0 && states[j - items[i]]) {
        // 购买这个商品
        System.out.print(items[i] + " ");
        j = j - items[i];
      }
    }

    if (j != 0) {
      System.out.print(items[0]);
    }
    System.out.println();
  }

}
