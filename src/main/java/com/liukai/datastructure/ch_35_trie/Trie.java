package com.liukai.datastructure.ch_35_trie;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * 35-1 Trie 树
 * <p>
 * 描述：Trie 树是一个字典树，是一种用于处理字符串匹配的数据结构。
 * <p>
 * 核心原理：利用字符串之间的相同公共前缀，将重复的前缀合并在一起。
 * <p>
 * 主要操作：
 * 1. 插入字符串
 * 2. 查找字符串
 * <p>
 * 特点：
 * 1. 构建 Trie 树的时间复杂度是 O(n)，n 为所有的字符串个数的长度之和。
 * 2. 查询的时间复杂度是 O(k)，k 为要查询字符串的长度
 * <p>
 * 适用场景：
 * 1. 与红黑树、散列表相比不适合用于字符串匹配
 * 2. 适用于字符串搜索提示
 */
public class Trie {

  /**
   * 根节点，存储无意义的字符
   */
  private final TrieNode root = new TrieNode('/');

  public static void main(String[] args) {
    Trie trie = new Trie();
    trie.insert("hello".toCharArray());
    trie.insert("ho".toCharArray());
    trie.insert("hi".toCharArray());
    trie.insert("hel".toCharArray());
    trie.insert("so".toCharArray());
    trie.insert("see".toCharArray());

    System.out.println(trie.find("he".toCharArray()));

    System.out.println(trie.findNotice("h".toCharArray()));

  }

  /**
   * 插入字符串
   *
   * @param text 字符数组
   */
  public void insert(char[] text) {
    TrieNode p = root;
    for (int i = 0; i < text.length; i++) {
      // ASCII 码
      int index = text[i] - 'a';
      if (p.children[index] == null) {
        p.children[index] = new TrieNode(text[i]);
      }
      p = p.children[index];
    }
    p.isEndingChar = true;
  }

  /**
   * 查找模式串
   *
   * @param pattern 模式串
   * @return 是否存在模式串
   */
  public boolean find(char[] pattern) {
    TrieNode p = root;
    for (int i = 0; i < pattern.length; i++) {
      int index = pattern[i] - 'a';
      if (p.children[index] == null) {
        // 没有找到
        return false;
      }
      p = p.children[index];
    }
    // 结尾自字符是否为叶子节点
    return p.isEndingChar;
  }

  /**
   * 查找前缀为模式串的字符串
   *
   * @param pattern 模式串
   * @return 匹配的字符串
   */
  public List<String> findNotice(char[] pattern) {
    TrieNode p = root;
    for (int i = 0; i < pattern.length; i++) {
      int index = pattern[i] - 'a';
      if (p.children[index] == null) {
        // 没有找到
        return null;
      }
      p = p.children[index];
    }

    // 结尾自字符是否为叶子节点
    if (p.isEndingChar) {
      return null;
    }
    List<String> result = new ArrayList<>();
    String prefixStr = String.copyValueOf(pattern);

    // 按照层来遍历树——广度优先搜索遍历
    Queue<TrieNode> queue = new LinkedList<>();
    queue.add(p);
    while (!queue.isEmpty()) {
      TrieNode node = queue.poll();
      if (!node.isEndingChar) {
        for (int i = 0; i < node.children.length; i++) {
          if (node.children[i] != null) {
            queue.add(node.children[i]);
            result.add(prefixStr + node.children[i].data);
          }
        }
      }
    }
    return result;

  }

  static class TrieNode {

    char data;

    /**
     * 数组，字符与数组下标映射
     * <p>
     * 通过 字符- 'a' 的 ASCII 码值与数组下标映射
     */
    TrieNode[] children = new TrieNode[26];

    boolean isEndingChar = false;

    public TrieNode(char data) {
      this.data = data;
    }

  }

}
