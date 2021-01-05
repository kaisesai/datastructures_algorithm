package com.kaige.datastructure.ch_41_dynamicprogramming;

import org.apache.commons.lang3.RandomUtils;

/**
 * 41-1 动态规划理论——最短路径长度
 *
 * <p>理论：动态规划可以总结为“一个模型三个特征”。其中一个模型指的是动态规划可以解决的问题的模型，我们把它定义为多阶段决策最优解模型。 三个特征分别指，最优子结构、无后效性、重复子问题。
 *
 * <p>举例分析：假设有一个 n * n 的矩阵 w[n][n]，矩阵存储的都是正整数。棋子起始位置在左上角，终止位置在右下角。
 * 我们将棋子从左上角移动到右下角。每次只能向右或向下移动一位。从左上角到右下角，会有很多不同的路径可以走。 我们把每条路径经过的数字加起来看作路径的长度。那从左上角移动到右下角的最短路径是多少？
 */
public class MinDist {

  // 状态转移表
  private final int[][] mem;

  int n;

  int[][] matrix;

  public MinDist(int n, int[][] matrix) {
    this.n = n;
    this.matrix = matrix;
    mem = new int[n][n];
  }

  public static void main(String[] args) {

    int n = 100;
    int[][] matrix = new int[n][n];
    // 初始化路径元素
    // matrix[0][0] = 1;
    // matrix[0][1] = 3;
    // matrix[0][2] = 5;
    // matrix[0][3] = 9;
    // matrix[1][0] = 2;
    // matrix[1][1] = 1;
    // matrix[1][2] = 3;
    // matrix[1][3] = 4;
    // matrix[2][0] = 5;
    // matrix[2][1] = 2;
    // matrix[2][2] = 6;
    // matrix[2][3] = 7;
    // matrix[3][0] = 6;
    // matrix[3][1] = 8;
    // matrix[3][2] = 4;
    // matrix[3][3] = 3;

    for (int i = 0; i < n; i++) {
      //
      for (int j = 0; j < n; j++) {
        matrix[i][j] = RandomUtils.nextInt(1, 11);
      }
    }

    MinDistBT minDistBT = new MinDistBT(n, matrix);

    long st;
    long et;

    // st = System.currentTimeMillis();
    // int minDist2 = minDistBT.getMinDist2();
    // et = System.currentTimeMillis();
    // System.out.println("回溯算法——最短路径getMinDist2：" + minDist2 + "，耗时：" + (et - st) + "ms，n=" + n);
    /*
    minDistBT方法一共执行了 184755 次
    回溯算法——最短路径getMinDist2：66，耗时：2ms，n=10
     */

    st = System.currentTimeMillis();
    int minDist = minDistBT.getMinDist();
    et = System.currentTimeMillis();
    System.out.println("回溯算法加备忘录——最短路径getMinDist：" + minDist + "，耗时：" + (et - st) + "ms，n=" + n);
    /*
    minDist方法一共执行了 1244 次
    回溯算法加备忘录——最短路径getMinDist：66，耗时：0ms，n=10

    minDist方法一共执行了 97851252 次
    回溯算法加备忘录——最短路径getMinDist：633，耗时：393ms，n=100
     */

    st = System.currentTimeMillis();
    int minDistDP = minDistDP(matrix, n);
    et = System.currentTimeMillis();
    System.out
      .println("动态规划——状态转移表法，实现最短路径minDistDP：" + minDistDP + "，耗时：" + (et - st) + "ms，n=" + n);
    // 动态规划——状态转移表法实现最短路径minDistDP：66，耗时：0ms，n=10
    // 动态规划——状态转移表法实现最短路径minDistDP：633，耗时：0ms，n=100

    MinDist dist = new MinDist(n, matrix);
    st = System.currentTimeMillis();
    int minDistDP2 = dist.minDistDP2(n - 1, n - 1);
    et = System.currentTimeMillis();
    System.out
      .println("动态规划——状态转移方程，实现最短路径minDistDP2：" + minDistDP2 + "，耗时：" + (et - st) + "ms，n=" + n);
  }

  /**
   * 动态规划——状态转移表法实现最短路径
   *
   * @param matrix
   * @param n
   * @return
   */
  public static int minDistDP(int[][] matrix, int n) {
    // 定义状态表
    int[][] states = new int[n][n];

    int sum = 0;
    for (int i = 0; i < n; i++) {
      // 初始化第一行数据
      sum += matrix[0][i];
      states[0][i] = sum;
    }

    sum = 0;
    for (int i = 0; i < n; i++) {
      // 初始化第一行数据
      sum += matrix[i][0];
      states[i][0] = sum;
    }

    for (int i = 1; i < n; i++) {
      // 动态规划
      for (int j = 1; j < n; j++) {
        states[i][j] = Math.min(states[i - 1][j] + matrix[i][j], states[i][j - 1] + matrix[i][j]);
      }
    }

    return states[n - 1][n - 1];
  }

  /**
   * 动态规划——状态转移方程方式实现最短路径
   *
   * <p>方式：minDist(i, j) = matrix[i][j] + min(minDist(i-1, j), minDist(i, j-1));
   *
   * <p>调用方式：minDistDP2(n-1, n-1);
   *
   * @param i 第 i 行
   * @param j 第 j 列
   * @return
   */
  public int minDistDP2(int i, int j) {

    if (i == 0 && j == 0) {
      return matrix[0][0];
    }
    if (mem[i][j] > 0) {
      return mem[i][j];
    }

    // 左
    int minLeft = Integer.MAX_VALUE;
    if (j > 0) {
      minLeft = minDistDP2(i, j - 1);
    }

    int minUp = Integer.MAX_VALUE;
    if (i > 0) {
      minUp = minDistDP2(i - 1, j);
    }

    int minDist = matrix[i][j] + Math.min(minLeft, minUp);

    // 设置状态值
    mem[i][j] = minDist;

    return minDist;
  }

  /**
   * 回溯算法求解最短路径
   */
  static class MinDistBT {

    // 矩阵行、列数
    private final int n;

    // 路径值
    private final int[][] matrix;

    // 备忘录
    private final int[][] mem;

    private int count;

    private int count2;

    private int minDist = Integer.MAX_VALUE; // 全局变量或者成员变量

    public MinDistBT(int n, int[][] matrix) {
      this.n = n;
      this.matrix = matrix;
      mem = new int[n][n];
    }

    /**
     * 求最短路径
     */
    public int getMinDist() {
      minDistBTMem(0, 0, 0);
      System.out.println("minDist方法一共执行了 " + count + " 次");
      return mem[n - 1][n - 1];
    }

    /**
     * 求最短路径
     */
    public int getMinDist2() {
      minDistBT(0, 0, 0);
      System.out.println("minDistBT方法一共执行了 " + count2 + " 次");
      return minDist;
    }

    /**
     * 回溯+备忘录
     *
     * <p>调用方式：minDistBTMem(0, 0, 0);
     *
     * @param i    第 i 行
     * @param j    第 j 列
     * @param dist 当前路径值
     */
    private void minDistBTMem(int i, int j, int dist) {
      count++;

      // 备忘录
      int newDist = dist + matrix[i][j];
      if (mem[i][j] > 0 && mem[i][j] < newDist) {
        return;
      }

      // 赋值状态 i，j
      mem[i][j] = newDist;

      if (j < n - 1) {
        // 向右移动
        minDistBTMem(i, j + 1, newDist);
      }

      if (i < n - 1) {
        // 向下移动
        minDistBTMem(i + 1, j, newDist);
      }
    }

    /**
     * 回溯
     *
     * <p>调用方式：minDistBT(0, 0, 0);
     *
     * @param i    第 i 行
     * @param j    第 j 列
     * @param dist 当前路径值
     */
    public void minDistBT(int i, int j, int dist) {
      count2++;

      int newDist = dist + matrix[i][j];

      // 到达了n-1, n-1这个位置了，这里看着有点奇怪哈，你自己举个例子看下
      if (i == n - 1 && j == n - 1) {
        if (newDist < minDist) {
          minDist = newDist;
        }
        return;
      }
      if (i < n - 1) {
        // 往下走，更新i=i+1, j=j
        minDistBT(i + 1, j, newDist);
      }

      if (j < n - 1) {
        // 往右走，更新i=i, j=j+1
        minDistBT(i, j + 1, newDist);
      }
    }

  }

}
