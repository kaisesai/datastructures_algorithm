package com.liukai.datastructure.ch_40_dynamicprogramming;

import org.apache.commons.lang3.RandomUtils;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * 40-1 初识动态规划
 *
 * <p>动态规划的思想：将问题分为多个阶段，每个阶段要做出一个决策，做出决策之后，就会多种不同的状态，我们合并重复的状态，根据当前上一个阶段所有状态，推导出做决策之后新的阶段的所有的状态。
 *
 * <p>问题描述：0-1 背包问题，我们在贪心、回溯算法的时候都有学过。
 *
 * <p>1. 对于一组不同重量，不可分割的物品，我们需要选择一些装入背包，在满足背包最大重量限制的前提下，背包中物品总重量的最大值是多呢？
 *
 * <p>2. 如果再加点难度，每个物品的价值不同，那如何在满足背包最大重量限制的前提下，那背包中物品总价值最大值是多少呢？
 */
public class Knapsack {

  public static void main(String[] args) {

    // 物品个数
    int n = 110;
    // 背包可承载的重量
    int w = 100;
    // 物品重量
    int[] weight = IntStream.range(0, n).map(i -> RandomUtils.nextInt(1, 100)).toArray();

    // 回溯算法求解背包问题
    BacktrackKnapsack backtrackKnapsack = new BacktrackKnapsack(weight, n, w);
    long st1 = System.currentTimeMillis();
    int maxValue = backtrackKnapsack.maxWeight(false);
    long et1 = System.currentTimeMillis();
    System.out.println("回溯算法——求解背包最大重量（不使用备忘录）：" + maxValue + "kg，耗时" + (et1 - st1) + "ms");
    // 回溯算法——求解背包最大重量（不使用备忘录）：100kg，耗时483ms

    st1 = System.currentTimeMillis();
    maxValue = backtrackKnapsack.maxWeight(true);
    et1 = System.currentTimeMillis();
    System.out.println("回溯算法——求解背包最大重量（使用备忘录）：" + maxValue + "kg，耗时" + (et1 - st1) + "ms");
    // 回溯算法——求解背包最大重量（使用备忘录）：100kg，耗时1ms

    st1 = System.currentTimeMillis();
    maxValue = knapsack(weight, n, w);
    et1 = System.currentTimeMillis();
    System.out.println("动态规划算法——求解背包最大重量：" + maxValue + "kg，耗时" + (et1 - st1) + "ms");
    // 动态规划算法——求解背包最大重量：100kg，耗时0ms

    st1 = System.currentTimeMillis();
    maxValue = knapsack2(weight, n, w);
    et1 = System.currentTimeMillis();
    System.out.println("动态规划算法——求解背包最大重量（空间优化之后）：" + maxValue + "kg，耗时" + (et1 - st1) + "ms");
    // 动态规划算法——求解背包最大重量（优化之后）：100kg，耗时0ms

    // 物品价值
    int[] value = IntStream.range(0, n).map(i -> RandomUtils.nextInt(1, 100)).toArray();
    st1 = System.currentTimeMillis();
    maxValue = knapsack3(weight, value, n, w);
    et1 = System.currentTimeMillis();
    System.out.println("动态规划算法——求解背包最大价值：" + maxValue + "元，耗时" + (et1 - st1) + "ms");
    // 动态规划算法——求解背包最大价值：672元，耗时0ms

    st1 = System.currentTimeMillis();
    maxValue = knapsack4(weight, value, n, w);
    et1 = System.currentTimeMillis();
    System.out.println("动态规划算法——求解背包最大价值（空间优化之后）：" + maxValue + "元，耗时" + (et1 - st1) + "ms");
    // 动态规划算法——求解背包最大价值（空间优化之后）：672元，耗时0ms
  }

  /**
   * 优化 knapsack3 方法
   *
   * <p>我们利用一个一维数组来记录商品重量、价值的状态信息
   *
   * @param weight 物品重量
   * @param value  物品价值
   * @param n      物品数量
   * @param w      背包可承载的重量
   * @return 背包最大的可装载的物品总价值
   */
  public static int knapsack4(int[] weight, int[] value, int n, int w) {
    // 状态数组，数组中存储的是重量状态对应的最大的
    int[] states = new int[w + 1];
    Arrays.fill(states, -1);

    // 第一行数据特殊处理
    states[0] = 0;
    if (weight[0] <= w) {
      states[weight[0]] = 0;
    }

    for (int i = 1; i < n; i++) {
      // 动态规划
      for (int j = w - weight[i]; j >= 0; j--) {
        if (states[j] >= 0) {
          // 将第 i 个物品放入背包
          int newValue = states[j] + value[i];
          if (newValue > states[j + weight[i]]) {
            // 新的价值大于旧价值
            states[j + weight[i]] = newValue;
          }
        }
      }
    }

    // 寻找最大值
    int maxValue = -1;
    for (int i = 0; i < w; i++) {
      if (states[i] > maxValue) {
        maxValue = states[i];
      }
    }
    return maxValue;
  }

  /**
   * 0-1 背包问题的升级版
   *
   * <p>描述：对于一组不同重量，不同价值，不可分割的物品，我们选择将某些物品装入背包，在满足背包最大重量限制的前提下，背包中可装入物品的总价值最大是多少？
   *
   * <p>这个问题，可以用回溯算法解决，但是不能用备忘录来提高了效率了。
   *
   * <p>我们采用动态规划，利用一个二维数组来记录商品重量、价值的状态信息
   *
   * @param weight 物品重量
   * @param value  物品价值
   * @param n      物品数量
   * @param w      背包可承载的重量
   * @return 背包最大的可装载的物品总价值
   */
  public static int knapsack3(int[] weight, int[] value, int n, int w) {
    // 状态数组，[][]数组中存储的内容为物品的最大价值,其实也是可以使用一个一维数组来处理
    int[][] states = new int[n][w + 1];

    // 初始化数组
    for (int[] state : states) {
      Arrays.fill(state, -1);
    }

    // 第一行数据特殊处理
    states[0][0] = 0;
    if (weight[0] <= w) {
      states[0][weight[0]] = 0;
    }

    // 动态规划
    for (int i = 1; i < n; i++) {

      for (int j = 0; j < w; j++) {
        // 第 i 个物品不装入背包
        if (states[i - 1][j] >= 0) {
          states[i][j] = states[i - 1][j];
        }
      }

      for (int j = 0; j <= w - weight[i]; j++) {
        // 第 i 个物品装入背包
        if (states[i - 1][j] >= 0) {
          // 新的价值
          int newV = states[i - 1][j] + value[i];
          // 新的价值，大于
          if (newV > states[i][j + weight[i]]) {
            states[i][j + weight[i]] = newV;
          }
        }
      }
    }

    // 找出最大值
    int maxValue = -1;
    for (int i = 0; i <= w; i++) {
      if (states[n - 1][i] > maxValue) {
        maxValue = states[n - 1][i];
      }
    }

    return maxValue;
  }

  /**
   * 优化 knapsack 方法
   *
   * <p>采用一个一位数组存储状态
   *
   * @param weight 物品重量
   * @param n      物品数量
   * @param w      背包可承载的重量
   * @return 背包最大可装入的总重量
   */
  public static int knapsack2(int[] weight, int n, int w) {
    // 采用一维数组存储状态
    boolean[] states = new boolean[w + 1];
    // 第一行特殊处理
    states[0] = true;
    if (weight[0] <= w) {
      states[weight[0]] = true;
    }

    for (int i = 1; i < n; i++) {
      // 动态规划
      for (int j = (w - weight[i]); j > 0; j--) {
        if (states[j]) {
          // 把第 i 个物品装进背包
          states[j + weight[i]] = true;
        }
      }
    }

    // 最近接 w 的值
    for (int i = w; i >= 0; i--) {
      if (states[i]) {
        return i;
      }
    }

    return 0;
  }

  /**
   * 动态规划的思想解决问题：我们把求解过程分成 n 个阶段，每个阶段会决策一个物品是否装入背包。每个决策完之后，背包中的物品的重量会有多种情况。
   * 也就是说，会达到多种不同的状态，对应到递归树中，就是会有很多不同的节点。
   *
   * <p>我们把每一层重复的状态（节点）合并，只记录不同的状态，然后基于上一层的状态集合，来推导下一层的状态集合。我们可以通过合并每层重复的状态，这样就保证每一层不同状态 都不会超过 w 个（w
   * 表示背包的承载重量），于是我们就成功避免了每层状态个数的指数及增长。
   *
   * <p>我们用一个二维数组 states[n][w+1]，来记录每层可以达到的不同状态。
   *
   * @param weight 物品重量
   * @param n      物品个数
   * @param w      背包可承载的重量
   * @return 背包最大可装入的总重量
   */
  public static int knapsack(int[] weight, int n, int w) {
    // 记录状态数组
    boolean[][] states = new boolean[n][w + 1];

    // 第一行特殊处理，可以利用哨兵优化
    states[0][0] = true;
    if (weight[0] <= w) {
      states[0][weight[0]] = true;
    }

    // 动态规划
    for (int i = 1; i < n; i++) {
      for (int j = 0; j <= w; j++) {
        if (states[i - 1][j]) {
          // 不把第 i 个物品装进背包
          states[i][j] = true;
        }
      }

      for (int j = 0; j <= w - weight[i]; j++) {
        if (states[i - 1][j]) {
          // 把第 i 个物品装进背包
          states[i][j + weight[i]] = true;
        }
      }
    }

    // 在最后一行，找出最近接 w 的重量状态
    for (int i = w; i > 0; i--) {
      if (states[n - 1][i]) {
        return i;
      }
    }

    return 0;
  }

  /**
   * 回溯算法解决 0-1 背包问题
   *
   * <p>要求：对于一组不同重量，不可分割的物品，我们需要选择一些装入背包，在满足背包最大重量限制的前提下，求出背包中物品总重量的最大值。
   *
   * <p>利用备忘录模式提高查询效率
   *
   * <p>（注意：如果增加了物品的价值，要在背包最大承重限制下，求背包中的最大价值，就不能用备忘录模式了。）
   */
  static class BacktrackKnapsack {

    // 物品重量信息
    private final int[] weight;

    // 备忘录
    private final boolean[][] state;

    // 物品数量
    private final int n;

    // 物品最大可承受重量
    private final int w;

    // 保存结果
    private int maxV = 0;

    public BacktrackKnapsack() {
      this(new int[] {2, 2, 4, 6, 3}, 5, 9);
    }

    public BacktrackKnapsack(int[] weight, int n, int w) {
      this.weight = weight;
      this.n = n;
      this.w = w;
      this.state = new boolean[n][w + 1];
    }

    /**
     * 求在背包最大可承载重量下，能装进背包中物品的最大重量
     *
     * @param isUseStates 是否使用备忘录
     * @return 能装进背包中物品的最大重量
     */
    public int maxWeight(boolean isUseStates) {
      maxV = 0;
      f(0, 0, isUseStates);
      return maxV;
    }

    /**
     * @param i           第 i 个商品
     * @param cw          当前背包中商品总重量
     * @param isUseStates 是否使用备忘录
     */
    private void f(int i, int cw, boolean isUseStates) {
      if (i == n || cw == w) {
        if (cw > maxV) {
          maxV = cw;
        }
        return;
      }

      if (isUseStates) {
        // 备忘录已经记录了，第 i 行，第 cw 列的状态
        if (state[i][cw]) {
          return;
        }

        // 设置备忘录的值
        state[i][cw] = true;
      }

      // 第 i 个商品不装进入背包
      f(i + 1, cw, isUseStates);

      if (cw + weight[i] <= w) {
        // 第 i 个商品装进入背包
        f(i + 1, cw + weight[i], isUseStates);
      }
    }

  }

}
