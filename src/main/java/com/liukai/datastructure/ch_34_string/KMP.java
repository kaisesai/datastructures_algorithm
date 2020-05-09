package com.liukai.datastructure.ch_34_string;

/**
 * 34-1 KMP 字符串匹配算法
 * <p>
 * 描述：KMP 算法是字符串匹配算法中最知名的一种算法，也是最难理解的算法。
 * <p>
 * 核心原理：模式串与主串匹配过程中，遇到不匹配的字符叫作坏字符，假设坏字符之前的字符串是已经匹配的，那就叫作好前缀。模式串需要完后滑动一定的位数，继续与主串进行匹配。
 * 在匹配过程中，其实是比较好前缀的所有前缀子串与好前缀的所有后缀子串中，最长的那个相等的字符串，称为最长可匹配前缀子串，以及最长可匹配后缀子串。模式串需要往后滑动的位数就是，
 * 坏字符在模式串中的位置 j，减去最长可匹配前缀子串的长度 k 的位置，即 j = j -k。
 * <p>
 * 规则：与 BM 算法的坏字符和好后缀规则相似，KMP 的规则是好前缀规则。
 * <p>
 * 代码实现：
 * 预处理模式串中的所有前缀子串的最长可匹配前缀子串。借助一个 next[] 数组，其中下标为模式串的所有前缀子串下标，值为其前缀子串对应的最长可匹配前缀子串的末尾字符位置。
 */
public class KMP {

  public static void main(String[] args) {
    String a = "12312312312awdnviefe2323223eddddddd22222aaeqwd";
    String b = "223edd";

    System.out.println(kmp(a.toCharArray(), a.length(), b.toCharArray(), b.length()));

  }

  /**
   * KMP 算法
   *
   * @param a 主串
   * @param n 主串长度
   * @param b 模式串
   * @param m 模式串长度
   * @return 模式串在主串中首次出现的索引，没有则返回 -1
   */
  public static int kmp(char[] a, int n, char[] b, int m) {
    // 构建失效函数 next 数组
    int[] next = generateNexts(b, m);

    // 在模式串中移动的变量
    int j = 0;
    // 在主串中移动的变量 i
    for (int i = 0; i < n; i++) {

      // 当模式串与主串匹配过程中出现坏字符，计算滑动的次数
      while (j > 0 && a[i] != b[j]) {
        j = next[j - 1] + 1;
      }

      // 字符匹配，则模式串也滑动一位
      if (a[i] == b[j]) {
        j++;
      }

      if (j == m) {
        // 找到匹配的模式串了
        return i - m + 1;
      }
    }

    return -1;
  }

  /**
   * 构建失效函数
   * <p>
   * 时间复杂度是 O(m)
   *
   * @param b 模式串
   * @param m 模式串长度
   * @return 数组
   */
  private static int[] generateNexts(char[] b, int m) {
    int[] next = new int[m];
    next[0] = -1;
    // 前缀子串中出现的最长可匹配前缀子串的位置
    int k = -1;
    for (int i = 1; i < m; i++) {
      // 递减的寻找次长可匹配前缀子串，它的下一个字符是否等于 b[i]
      while (k != -1 && b[k + 1] != b[i]) {
        // 最长可匹配前缀子串的最长可匹配的前缀子串
        k = next[k];
      }

      // 可匹配前缀子串的下一个字符等于 b[i]，则是 k 就是 b[0,i] 的最长可匹配前缀/后缀子串
      if (b[k + 1] == b[i]) {
        k++;
      }
      next[i] = k;
    }

    return next;
  }

  /**
   * 构建失效函数
   * <p>
   * 描述：b 的所有前缀子串的末尾字符在它的前缀子串的前缀子串中出现的最后一个位置
   * <p>
   * 最坏情况下时间复杂度是 O(m^2)
   *
   * @param b 模式串
   * @param m 模式串长度
   * @return 数组
   */
  private static int[] generateNexts2(char[] b, int m) {
    int[] next = new int[m];
    next[0] = -1;
    // 前缀子串中出现的最长可匹配前缀子串的位置

    for (int i = 1; i < m; i++) {
      int k = -1;
      // 子串的最后一个字符
      char last = b[i];
      // 最后一个字符在前缀子串的前缀子串中最后出现得位置
      for (int j = i - 1; j >= 0; j--) {
        if (last == b[j]) {
          k = j;
          break;
        }
      }
      next[i] = k;
    }

    return next;
  }

}
