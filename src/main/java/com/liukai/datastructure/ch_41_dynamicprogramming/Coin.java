package com.liukai.datastructure.ch_41_dynamicprogramming;

/**
 * 41-2 动态规划——钱币找零问题
 */
public class Coin {

  // 回溯算法
  private static int minNum = Integer.MAX_VALUE;

  public static void main(String[] args) {
    int[] coins = {1, 3, 5};
    // coinNumBT(0, coins, 0, 10);
    // System.out.println("最少需要硬币数量：" + minNum);

    int numDP1 = coinNumDP1(coins, 9);
    System.out.println("numDP1 = " + numDP1);
  }

  /**
   * 动态规划（状态转移表法）——求解硬币找零问题
   *
   * @param coins     硬币信息
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
   * @param i             当前第几个硬币
   * @param coins         硬币信息 int[] coins = {1,3,5};
   * @param currentAmount 当前价值
   * @param maxAmount     总价值
   */
  public static void coinNumBT(int i, int[] coins, int currentAmount, int maxAmount) {
    if (currentAmount == maxAmount) {
      if (i < minNum) {
        minNum = i;
      }
      return;
    }

    int needAmount = maxAmount - currentAmount;
    if (needAmount >= coins[0]) {
      // 选择第 0 个硬币，1元
      coinNumBT(i + 1, coins, currentAmount + coins[0], maxAmount);
    }

    if (needAmount >= coins[1]) {
      // 选择第 1 个硬币，3 元
      coinNumBT(i + 1, coins, currentAmount + coins[1], maxAmount);
    }

    if (needAmount >= coins[2]) {
      // 选择第 2 个硬币，5 元
      coinNumBT(i + 1, coins, currentAmount + coins[2], maxAmount);
    }
  }

}
