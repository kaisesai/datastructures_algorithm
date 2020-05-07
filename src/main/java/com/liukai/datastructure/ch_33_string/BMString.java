package com.liukai.datastructure.ch_33_string;

import java.util.Arrays;

/**
 * 33-1 BM 字符串匹配算法
 * <p>
 * 核心思想：将模式串与主串的匹配过程，看作模式串在主串中不停的往后滑动，当遇到不匹配的字符时往后滑动一定的位数。
 * 这个一定的位数，就是 BM 算法所寻找的规律。
 * <p>
 * BM 算法利用两种规则来计算模式串往后滑动的位数，来进行比较的。
 * 1. 坏字符规则：将模式串从后往前与主串的字符进行匹配，当遇到不匹配的字符时，这个字符就是坏字符。
 * 然后查找坏字符在模式串中查找其所出现的位置，计算模式串往后滑动的位数。
 * 2. 好后缀规则：我们把模式串与主串匹配过程中，默认相同的的字符串叫做好后缀，并记作{u}，我们拿它在模式串中查找。
 * 如果找到了另一个跟{u}相匹配的子串{u*}，我们就将模式串移动到子串{u*}与主串中{u}对齐的位置
 * <p>
 * 坏字符规则的实现：
 * 1. 借助ASCII 码值构建模式串中字符的散列表
 *
 * <p>
 * 好后缀规则的实现：
 * 1. 借助数组 int[] suffix 预处理模式串中公共后缀子串
 * 2. 借助数组 boolean[] prefix 记录公共后缀子串与模式串前缀子串匹配的信息
 */
public class BMString {

  /**
   * 模式串字符散列表的容量
   */
  private static final int SIZE = 256;

  public static void main(String[] args) {
    String master = "jkjfkdfvowemfiowmiocmkdmffd";
    String b = "ffd";

    BMString bmString = new BMString();
    int bm = bmString.bm(master.toCharArray(), master.length(), b.toCharArray(), b.length());
    System.out.println(bm);
  }

  /**
   * BM 字符串匹配算法
   *
   * @param a 主串
   * @param n 主创长度
   * @param b 模式串
   * @param m 模式串长度
   * @return 模式串在主串中的索引，如果没有找到则返回 -1
   */
  public int bm(char[] a, int n, char[] b, int m) {
    // 记录模式串中每个字符最后出现的位置
    int[] bc = new int[SIZE];
    // 构建模式串散列表
    generateBC(b, m, bc);

    // 预处理公共后缀子串
    int[] suffix = new int[m];
    boolean[] prefix = new boolean[m];
    generateGS(b, m, suffix, prefix);

    // 模式串与主串对齐的第一个字符
    int i = 0;
    while (i <= n - m) {
      // 坏字符出现在模式串中的位置
      int j;
      for (j = m - 1; j >= 0; j--) {
        // 模式串从后往前匹配
        if (a[i + j] != b[j]) {
          break;
        }
      }

      if (j < 0) {
        // 匹配成功，返回主串与模式串第一个匹配的字符的位置
        return i;
      }
      // 坏字符规则将模式串往后滑动 j - bc[a[i+j]] 位
      int x = j - bc[a[i + j]];
      // 好后缀规则移动位数
      int y = 0;
      if (j < m - 1) {
        y = moveByGS(j, m, suffix, prefix);
      }
      i = i + Math.max(x, y);
    }
    return -1;
  }

  /**
   * 通过好后缀规则移动的次数
   *
   * @param j      坏字符对应的模式串中的字符下标
   * @param m      模式串的长度
   * @param suffix 后缀子串数组
   * @param prefix 前缀子串数组
   * @return 模式串往后滑动位数
   */
  private int moveByGS(int j, int m, int[] suffix, boolean[] prefix) {
    // 好后缀的长度
    int k = m - 1 - j;
    if (suffix[k] != -1) {
      return j - suffix[k] + 1;
    }

    // r 为好后缀子串的子串，j 为坏字符在模式串的位置，j+1 为好后缀的位置，所以，j+2 为好后缀子串的子串
    for (int r = j + 2; r < m - 1; r++) {
      if (prefix[m - r]) {
        return r;
      }
    }
    return m;
  }

  /**
   * 预处理好后缀子串
   *
   * @param b      模式串
   * @param m      模式串长度
   * @param suffix 后缀子串数组
   * @param prefix 前缀子串数组
   */
  private void generateGS(char[] b, int m, int[] suffix, boolean[] prefix) {
    // 初始化
    for (int i = 0; i < m; i++) {
      suffix[i] = -1;
      prefix[i] = false;
    }
    // b[0,i]
    for (int i = 0; i < m - 1; i++) {
      int j = i;
      // 公共后缀子串的长度
      int k = 0;
      // 与 b[0,m-1] 求公共后缀子串
      // 从模式串的最后一个字符往前匹配
      while (j >= 0 && b[j] == b[m - 1 - k]) {
        --j;
        ++k;
        // j + 1 表示公共后缀子串在 b[0,i] 中的起始下标
        suffix[k] = j + 1;
      }
      if (j == -1) {
        // 如果公共后缀子串也是模式串的前缀子串
        prefix[k] = true;
      }
    }

  }

  /**
   * 构建模式串的散列表
   *
   * @param b  模式串
   * @param m  模式串长度
   * @param bc 散列表
   */
  private void generateBC(char[] b, int m, int[] bc) {
    // 初始化散列表
    Arrays.fill(bc, -1);
    for (int i = 0; i < m; i++) {
      // 计算 b[i] 的 ASCII 码值
      int ascii = b[i];
      bc[ascii] = i;
    }
  }

}
