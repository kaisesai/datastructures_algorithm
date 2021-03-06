package com.kaige.datastructure.ch_39_backtrack;

import java.util.Arrays;

/**
 * 39-3 回溯算法经典应用——正则表达式
 *
 * <p>描述：正则表达式中，最重要的就是通配符，通配符结合在一起可以表达非常丰富的语义。这里我们假设正则表达式中只包含"*"和"?"两种通配符，
 * 并且对这两个通配符的语义稍微做些改变。其中"*"匹配任意多个任意字符，"?"匹配 0 个或 1 个任意字符。
 *
 * <p>基于以上背景，我们利用回溯算法，来判断一个给定的文本，能否跟给定的正则表达式匹配。
 *
 * <p>我们依次考察正则表达式中每一个字符，当它是非通配符的时候，我们就直接跟文本的字符进行匹配，如果相同，则继续往下处理；如果不同，则回溯。
 *
 * <p>当遇到特殊字符的时候，我们就有多种模式方式，也就是所谓的岔路口。比如"*"有多种匹配方案，可以匹配任意文本中的字符，我们就先随意的选择一种匹配方案，
 * 然后继续考察剩下的字符。如果中途发现无法继续匹配下去，我们就回到这个路口，重新选择一条匹配方案，然后再继续匹配剩下的字符。
 */
public class BacktrackPattern {

  /**
   * 正则表达式
   */
  private final char[] pattern;
  
  /**
   * 正则表达式的长度
   */
  private final int plen;
  
  /**
   * 匹配结果
   */
  private boolean matched = false;
  
  private BacktrackPattern(char[] pattern, int plen) {
    this.pattern = pattern;
    this.plen = plen;
  }
  
  public static void main(String[] args) {
    String patternStr = "abc*de?*a";
    BacktrackPattern pattern = new BacktrackPattern(patternStr.toCharArray(), patternStr.length());
    
    String textStr = "abcadefbga";
    
    boolean match = pattern.match(textStr.toCharArray(), textStr.length());

    System.out.println("文本串：" + textStr + ", 正则表达式：" + patternStr + "，是否匹配：" + match);

    /*
     匹配过程，文本串：[a]，模式串：[a]
     匹配过程，文本串：[a, b]，模式串：[a, b]
     匹配过程，文本串：[a, b, c]，模式串：[a, b, c]
     匹配过程，文本串：[a, b, c, a]，模式串：[a, b, c, *]
     匹配过程，文本串：[a, b, c, a]，模式串：[a, b, c, *, d]
     匹配过程，文本串：[a, b, c, a, d]，模式串：[a, b, c, *, d]
     匹配过程，文本串：[a, b, c, a, d, e]，模式串：[a, b, c, *, d, e]
     匹配过程，文本串：[a, b, c, a, d, e, f]，模式串：[a, b, c, *, d, e, ?]
     匹配过程，文本串：[a, b, c, a, d, e, f]，模式串：[a, b, c, *, d, e, ?, *]
     匹配过程，文本串：[a, b, c, a, d, e, f]，模式串：[a, b, c, *, d, e, ?, *, a]
     匹配过程，文本串：[a, b, c, a, d, e, f, b]，模式串：[a, b, c, *, d, e, ?, *, a]
     匹配过程，文本串：[a, b, c, a, d, e, f, b, g]，模式串：[a, b, c, *, d, e, ?, *, a]
     匹配过程，文本串：[a, b, c, a, d, e, f, b, g, a]，模式串：[a, b, c, *, d, e, ?, *, a]
     匹配过程，文本串：[a, b, c, a, d, e, f, b, g, a,  ]，模式串：[a, b, c, *, d, e, ?, *, a,  ]
     匹配过程，文本串：[a, b, c, a, d, e, f, b]，模式串：[a, b, c, *, d, e, ?, *]
     匹配过程，文本串：[a, b, c, a, d, e]，模式串：[a, b, c, *, d]
     匹配过程，文本串：[a, b, c, a, d, e, f]，模式串：[a, b, c, *, d]
     匹配过程，文本串：[a, b, c, a, d, e, f, b]，模式串：[a, b, c, *, d]
     匹配过程，文本串：[a, b, c, a, d, e, f, b, g]，模式串：[a, b, c, *, d]
     匹配过程，文本串：[a, b, c, a, d, e, f, b, g, a]，模式串：[a, b, c, *, d]
     文本串：abcadefbga, 正则表达式：abc*de?*a，是否匹配：true
    */
  }
  
  /**
   * 匹配字符串
   *
   * @param text 文本串
   * @param tlen 文本串的长度
   * @return 是否匹配正则表达式
   */
  private boolean match(char[] text, int tlen) {
    matched = false;
    rmatch(0, 0, text, tlen);
    return matched;
  }

  /**
   * 匹配模式
   *
   * @param ti   当前匹配文本串的第几个字符
   * @param pj   当前匹配正则表达式的第几个字符
   * @param text 文本串
   * @param tlen 文本串的长度
   */
  private void rmatch(int ti, int pj, char[] text, int tlen) {

    System.out.println(String
                         .format("匹配过程，文本串：%s，模式串：%s", Arrays.toString(Arrays.copyOf(text, ti + 1)),
                                 Arrays.toString(Arrays.copyOf(pattern, pj + 1))));

    if (matched) {
      // 如果已经匹配就不用在匹配了
      return;
    }

    if (pj == plen) {
      // 正则表达式到结尾了
      if (ti == tlen) {
        // 文本串也到结尾了
        matched = true;
      }
      return;
    }

    if (pattern[pj] == '*') {
      // * 匹配任意字符
      for (int i = 0; i < (tlen - ti); i++) {
        // 遍历剩余的（tlen - ti）个字符的次数，与表达式中当前 * 的下一个字符进行匹配
        rmatch(ti + i, pj + 1, text, tlen);
      }
    } else if (pattern[pj] == '?') {
      // ? 匹配 0 个或 1 个任意字符
      rmatch(ti, pj + 1, text, tlen);
      rmatch(ti + 1, pj + 1, text, tlen);
    } else if (ti < tlen && pattern[pj] == text[ti]) {
      // 纯字符字符匹配，则继续下一个字符匹配
      rmatch(ti + 1, pj + 1, text, tlen);
    }
  }

}
