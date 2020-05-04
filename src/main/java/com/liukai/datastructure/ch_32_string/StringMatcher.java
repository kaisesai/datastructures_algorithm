package com.liukai.datastructure.ch_32_string;

/**
 * 31-1 字符串匹配算法
 * <p>
 * 描述：
 * 1. BF 暴力匹配算法。将模式串与主串中的 n-m+1 个长度为 m 的子串进行比较
 * 2. RK 利用哈希算法对 BF 算法的改进版本。利用模式串的哈希值与子串的哈希值进行比较
 */
public class StringMatcher {

  public static void main(String[] args) {
    String srcStr = "aaaaaabbbbbbccccc";
    String searchStr = "bc";

    int matchToBF = matchStrToBF(srcStr, searchStr);
    System.out.println("BF 暴力搜索搜索算法：" + searchStr + " 在" + " " + srcStr + " 中索引位置：" + matchToBF);

    int matchToRK = matchStrToRK(srcStr, searchStr);
    System.out.println("RK 搜索算法：" + searchStr + " 在" + " " + srcStr + " 中索引位置：" + matchToRK);

  }

  /**
   * 搜索字符串——RK 算法
   * <p>
   * 描述：利用哈希函数对子串与模式串哈希，通过哈希值比较字符串
   *
   * @param srcStr    主串
   * @param searchStr 模式串
   * @return 模式串在主串中首次出现的索引值
   */
  public static int matchStrToRK(String srcStr, String searchStr) {
    // 主串的字符集
    char[] srcStrChars = srcStr.toCharArray();
    // 模式串字符集
    char[] searchStrChars = searchStr.toCharArray();

    // 对字符串进行哈希
    long strHashCode = hashForStrOfRK(searchStrChars, 0, searchStrChars.length);

    // 子串数量
    int subStrSize = srcStrChars.length - searchStrChars.length + 1;

    // 遍历子串数量
    for (int i = 0; i < subStrSize; i++) {
      // 子字符串哈希值
      int subStrHash = hashForStrOfRK(srcStrChars, i, i + searchStrChars.length);

      // 哈希值相同，并且字符匹配
      if (subStrHash == strHashCode && matchChars(srcStrChars, searchStrChars, i)) {
        return i;
      }
    }
    return -1;

  }

  /**
   * RK 算法的字符串哈希算法
   *
   * @param strChars 字符数组
   * @param s        字符数组开始比较的索引
   * @param t        字符数组结束比较的索引
   * @return 哈希值
   */
  public static int hashForStrOfRK(char[] strChars, int s, int t) {
    int hash = 0;
    for (int i = s; i < t; i++) {
      hash += strChars[s];
    }
    return hash;
  }

  /**
   * 搜索字符串——BF 暴力匹配搜索算法
   *
   * @param srcStr    主串
   * @param searchStr 模式串
   * @return 返回模式串在主串中出现的第一个索引地址
   */
  public static int matchStrToBF(String srcStr, String searchStr) {

    // 主串的字符集
    char[] srcStrChars = srcStr.toCharArray();
    // 模式串字符集
    char[] searchStrChars = searchStr.toCharArray();

    // 子串数量
    int subStrSize = srcStrChars.length - searchStrChars.length + 1;

    // 模式串与子串进行比较
    for (int i = 0; i < subStrSize; i++) {
      // 从索引 i 开始，直到 n-m 结束，一共比较 n-m+1 个长度为 m 的子串
      boolean flag = matchChars(srcStrChars, searchStrChars, i);
      if (flag) {
        return i;
      }
    }
    return -1;
  }

  /**
   * 子字符数组是否包含模式字符数组
   *
   * @param srcStrChars    子串字符数组
   * @param searchStrChars 模式串字符数组
   * @param i              从子串字符数组搜索的索引
   * @return 是否包含
   */
  private static boolean matchChars(char[] srcStrChars, char[] searchStrChars, int i) {
    int n = i;
    for (char searchStrChar : searchStrChars) {
      if (srcStrChars[n] == searchStrChar) {
        n++;
      } else {
        return false;
      }
    }
    return true;
  }

}
