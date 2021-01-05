package com.kaige.datastructure.ch_42_dynamicprogramming;

/**
 * 42-1 动态规划实战
 *
 * <p>字符串编辑距离：指将一个字符串转化成另一个字符串，需要的最少编辑操作次数（比如增加一个字符、删除一个字符、替换一个字符）。编辑距离越大，说明两个字符串的相似程度越小；
 * 相反，编辑距离越小，就说明两个字符串的相似程度就越大。
 *
 * <p>根据所包含的编辑操作的种类不同，编辑距离有多种不同的计算方式，比较著名的有莱文斯坦距离和最长公共子串长度。
 *
 * <p>莱文斯坦距离允许增加、删除、替换字符这三个编辑操作，最长公共子串长度只允许增加、删除字符这两个操作
 */
public class StringPattern {

  public static char[] a = "mitcmu".toCharArray();

  public static char[] b = "mtacnu".toCharArray();

  static int min = Integer.MAX_VALUE;

  public static void main(String[] args) {
    lwstBT(0, 0, a.length, b.length, 0);
    System.out.println("lwstBT = " + min);

    int lwstDP = lwstDP(a, a.length, b, b.length);
    System.out.println("lwstDP = " + lwstDP);

    int lcs = lcs(a, a.length, b, b.length);
    System.out.println("lcs = " + lcs);
  }

  /**
   * 莱文斯坦距离——动态规划求解
   *
   * @param a 第一个字符串
   * @param n 第一个字符串的长度
   * @param b 第二个字符串
   * @param m 第二个字符串的长度
   * @return 莱文斯坦距离
   */
  public static int lwstDP(char[] a, int n, char[] b, int m) {
    int[][] minDist = new int[n][m];

    for (int j = 0; j < m; j++) {
      // 初始化第 0 行与第 j 列的编辑距离
      if (a[0] == b[j]) {
        minDist[0][j] = j;
      } else if (j != 0) {
        minDist[0][j] = minDist[0][j - 1] + 1;
      } else {
        minDist[0][j] = 1;
      }
    }

    for (int i = 0; i < n; i++) {
      // 初始化第 i 行与第 0 列的编辑距离
      if (a[i] == b[0]) {
        minDist[i][0] = i;
      } else if (i != 0) {
        minDist[i][0] = minDist[i - 1][0] + 1;
      } else {
        minDist[i][0] = 1;
      }
    }

    for (int i = 1; i < n; i++) {
      // 按行填表
      for (int j = 1; j < m; j++) {
        if (a[i] == b[j]) {
          minDist[i][j] = min(minDist[i - 1][j] + 1, minDist[i][j - 1] + 1, minDist[i - 1][j - 1]);
        } else {
          minDist[i][j] = min(minDist[i - 1][j] + 1, minDist[i][j - 1] + 1,
                              minDist[i - 1][j - 1] + 1);
        }
      }
    }
    return minDist[n - 1][m - 1];
  }

  /**
   * 莱文斯坦距离——回溯算法求解
   *
   * <p>对比两个字符串，利用回溯算法，递归寻找所有情况
   *
   * <p>当 a[i] 与 b[j] 相等的情况下， 需要对 i 或者 j 增加或者删除，替换
   *
   * <p>动态转移方程：f(i,j) = min(f(i))
   *
   * <p>当 a[i] 与 b[j] 不相等的情况下， 动态转移方程：f(i,j) = min(f(i))
   */
  public static void lwstBT(int i, int j, int n, int m, int dist) {

    if (i == n || j == m) {
      if (i < n) {
        dist += (n - i);
      }
      if (j < m) {
        dist += (m - j);
      }
      if (dist < min) {
        min = dist;
      }
      return;
    }

    // if (mem[i][j]) {
    //   return;
    // }
    // mem[i][j] = true;

    if (a[i] == b[j]) {
      // 对 i 和 j 加一
      lwstBT(i + 1, j + 1, n, m, dist);
      // return;

    } else {
      // 对 i 加一
      lwstBT(i + 1, j, n, m, dist + 1);
      // 对 j 加一
      lwstBT(i, j + 1, n, m, dist + 1);
      // 对 i 和 j 加一
      lwstBT(i + 1, j + 1, n, m, dist + 1);
      // 求三个最小值
      // int min = min(min1, min2, min3);
    }
  }

  private static int min(int x, int y, int z) {
    int min = Integer.MAX_VALUE;
    if (x < min) {
      min = x;
    }
    if (y < min) {
      min = y;
    }
    if (z < min) {
      min = z;
    }
    return min;
  }

  /**
   * 最长公共子串——动态规划求解
   *
   * @param a 第一个字符串
   * @param n 第一个字符串的长度
   * @param b 第二个字符串
   * @param m 第二个字符串的长度
   * @return 最长公共子串
   */
  public static int lcs(char[] a, int n, char[] b, int m) {
    int[][] maxlcs = new int[n][m];
    for (int j = 0; j < m; ++j) { // 初始化第0行：a[0..0]与b[0..j]的maxlcs
      if (a[0] == b[j]) {
        maxlcs[0][j] = 1;
      } else if (j != 0) {
        maxlcs[0][j] = maxlcs[0][j - 1];
      } else {
        maxlcs[0][j] = 0;
      }
    }
    for (int i = 0; i < n; ++i) { // 初始化第0列：a[0..i]与b[0..0]的maxlcs
      if (a[i] == b[0]) {
        maxlcs[i][0] = 1;
      } else if (i != 0) {
        maxlcs[i][0] = maxlcs[i - 1][0];
      } else {
        maxlcs[i][0] = 0;
      }
    }
    for (int i = 1; i < n; ++i) { // 填表
      for (int j = 1; j < m; ++j) {
        if (a[i] == b[j]) {
          maxlcs[i][j] = max(maxlcs[i - 1][j], maxlcs[i][j - 1], maxlcs[i - 1][j - 1] + 1);
        } else {
          maxlcs[i][j] = max(maxlcs[i - 1][j], maxlcs[i][j - 1], maxlcs[i - 1][j - 1]);
        }
      }
    }
    return maxlcs[n - 1][m - 1];
  }

  private static int max(int x, int y, int z) {
    int maxv = Integer.MIN_VALUE;
    if (x > maxv) {
      maxv = x;
    }
    if (y > maxv) {
      maxv = y;
    }
    if (z > maxv) {
      maxv = z;
    }
    return maxv;
  }

}
