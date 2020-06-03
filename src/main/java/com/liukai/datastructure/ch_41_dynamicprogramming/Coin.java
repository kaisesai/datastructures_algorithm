package com.liukai.datastructure.ch_41_dynamicprogramming;

import java.util.Arrays;

/**
 * 41-2 动态规划——钱币找零问题
 */
public class Coin {

  static final int AMOUNT = 100;

  // 备忘录。i 表示硬币个数，j 表示总金额
  static boolean[][] mem = new boolean[AMOUNT][AMOUNT + 1];

  static int coinNumBTNum = 0;

  // 回溯算法
  private static int minNum = Integer.MAX_VALUE;

  public static void main(String[] args) {
    int[] coins = {1, 3, 5, 7, 9};
    // int[] coins = {2147483647};
    coinNumBT(0, coins, 0, AMOUNT);
    System.out.println("最少需要硬币数量：" + minNum + ", coinNumBT 方法调用了 " + coinNumBTNum + " 次");

    int coinNumDP1 = coinNumDP1(coins, AMOUNT);
    System.out.println("coinNumDP1 = " + coinNumDP1);

    int coinNumDP2 = coinNumDP2(coins, AMOUNT);
    System.out.println("coinNumDP2 = " + coinNumDP2);

    int coinNumDP3 = coinNumDP3(coins, AMOUNT);
    System.out.println("coinNumDP3 = " + coinNumDP3);
  }

  /**
   * 动态规划——状态转移方程法
   *
   * <p>自下而上，从最大金额开始往前推导。
   *
   * <p>状态数组为 int[] count，下标表示金额，值为最小硬币数
   *
   * <p>公式：f(s) = min(f(s-coin1), f(s-coin2), f(s-coin3)) + 1;
   *
   * <p>s 为金额，coin$n 硬币价值，在结尾时设置状态值，递归方式求解，最终设置状态值
   *
   * @see
   *     参考力扣题解：https://leetcode-cn.com/problems/coin-change/solution/322-ling-qian-dui-huan-by-leetcode-solution/
   * @param coins
   * @param amount
   * @return
   */
  public static int coinNumDP2(int[] coins, int amount) {
    int[] count = new int[amount];
    // 动态规划：自下而上
    return coinChange(coins, amount, count);
  }

  private static int coinChange(int[] coins, int rem, int[] count) {
    if (rem < 0) {
      return -1;
    }
    if (rem == 0) {
      return 0;
    }
    if (count[rem - 1] != 0) {
      return count[rem - 1];
    }
    int min = Integer.MAX_VALUE;
    for (int coin : coins) {
      int res = coinChange(coins, rem - coin, count);
      if (res >= 0 && res < min) {
        min = res + 1;
      }
    }

    count[rem - 1] = (min == Integer.MAX_VALUE) ? -1 : min;
    return count[rem - 1];
  }

  /**
   * 动态规划——状态转移表法
   *
   * <p>优化的方法
   *
   * <p>自上而下
   *
   * @param coins
   * @param maxAmount
   * @return
   */
  public static int coinNumDP3(int[] coins, int maxAmount) {
    int max = maxAmount + 1;
    // 定义状态数组，下标为金额，值为最小硬币数
    int[] states = new int[max];

    // 初始化硬币数为最大值
    Arrays.fill(states, max);

    // 初始化第 0 个金额
    states[0] = 0;

    for (int i = 1; i <= maxAmount; i++) {
      // 动态规划
      for (int coin : coins) {
        if (i - coin >= 0) {
          // 求当前金额减去三枚硬币各自需要的最小值
          states[i] = Math.min(states[i], states[i - coin] + 1);
        }
      }
    }
    return states[maxAmount];
  }

  /**
   * 动态规划（状态转移表法）——求解硬币找零问题
   *
   * <p>自上而下
   *
   * @param coins 硬币信息
   * @param maxAmount 最大金额
   * @return 硬币数量
   */
  public static int coinNumDP1(int[] coins, int maxAmount) {
    // 状态数组，，第一个数组长度，表示一共有 9 个阶段，每个阶段最多需要支付有 9 枚硬币
    // 其实，每个阶段最多需要支付的硬币数，为 阶段的个数。
    int[] states = new int[maxAmount + 1];
    // 初始化阶段数目
    for (int coin : coins) {
      // 初始化数组，金额为硬币值的时候，状态设置为 1
      states[coin] = 1;
    }

    // 动态规划
    for (int i = 1; i <= maxAmount; i++) {
      if (states[i] == 0) {
        // 获取每个对应的最小硬币数量
        int minTemp = Integer.MAX_VALUE;
        for (int coin : coins) {
          if (i - coin > 0) {
            // i - states[j] 表示，剩余多少钱，
            int v1 = states[i - coin] + 1;
            if (v1 < minTemp) {
              minTemp = v1;
            }
          }
        }
        states[i] = minTemp;
      }
    }

    // 找出最后一行的最小个数
    return states[maxAmount];
  }

  /**
   * 回溯算法——求解硬币找零问题
   *
   * @param i 当前第几个硬币
   * @param coins 硬币信息 int[] coins = {1,3,5};
   * @param currentAmount 当前价值
   * @param maxAmount 总价值
   */
  public static void coinNumBT(int i, int[] coins, int currentAmount, int maxAmount) {
    coinNumBTNum++;
    if (currentAmount == maxAmount) {
      if (i < minNum) {
        minNum = i;
      }
      return;
    }

    // 备忘录已经存在
    if (mem[i][currentAmount]) {
      return;
    }

    mem[i][currentAmount] = true;
    // 设置备忘录

    // 需要支付的金额
    int needAmount = maxAmount - currentAmount;

    for (int coin : coins) {
      if (needAmount >= coin) {
        // 选择第 i 个硬币
        coinNumBT(i + 1, coins, currentAmount + coin, maxAmount);
      }
    }
  }
}
