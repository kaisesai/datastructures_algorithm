package com.liukai.datastructure.ch_35_trie;

import java.util.*;

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

  /**
   * Trie 树子节点存储方式
   */
  private final TrieNodeSTOREType storeType;

  public Trie(TrieNodeSTOREType storeType) {
    this.storeType = storeType;
  }

  public static void main(String[] args) {
    Trie trie = new Trie(TrieNodeSTOREType.HASH_TABLE);
    trie.insert("hello".toCharArray());
    trie.insert("ho".toCharArray());
    trie.insert("hi".toCharArray());
    trie.insert("hel".toCharArray());
    trie.insert("so".toCharArray());
    trie.insert("see".toCharArray());

    System.out.println(trie.find("ho".toCharArray()));

    System.out.println(trie.findNotice("h".toCharArray()));

  }

  /**
   * 插入字符串
   *
   * @param text 字符数组
   */
  public void insert(char[] text) {
    TrieNode p = root;
    for (char c : text) {
      TrieNode children = p.getChild(c);
      if (children == null) {
        p.addChildren(c);
        // p.children[index] = new TrieNode(text[i]);
      }
      p = p.getChild(c);
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
      TrieNode children = p.getChild(pattern[i]);
      // int index = pattern[i] - 'a';
      if (children == null) {
        // 没有找到
        return false;
      }
      p = children;
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
    for (char c : pattern) {
      TrieNode children = p.getChild(c);
      // int index = pattern[i] - 'a';
      if (children == null) {
        // 没有找到
        return null;
      }
      p = children;
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
        for (TrieNode children : node.getChildrenForArray()) {
          if (children != null) {
            queue.add(children);
            result.add(prefixStr + children.getData());
          }
        }

        // for (int i = 0; i < node.children.length; i++) {
        //   if (node.children[i] != null) {
        //     queue.add(node.children[i]);
        //     result.add(prefixStr + node.children[i].data);
        //   }
        // }
      }
    }
    return result;

  }

  /**
   * Trie 树子节点存储类型枚举
   */
  enum TrieNodeSTOREType {
    ARRAY,// 数组映射
    HASH_TABLE, // 散列表
    RED_BLACK_TREE// 红黑树
  }

  class TrieNode {

    char data;

    /**
     * 数组，字符与数组下标映射
     * <p>
     * 通过 字符- 'a' 的 ASCII 码值与数组下标映射
     */
    TrieNode[] childrenForArray = new TrieNode[26];

    // 使用散列表存储
    HashMap<Character, TrieNode> childrenForHashTable = new HashMap<>();

    // 使用红黑树存储
    TreeMap<Character, TrieNode> childrenForRedBlackTree = new TreeMap<>();

    boolean isEndingChar = false;

    public TrieNode(char data) {
      this.data = data;
    }

    public char getData() {
      return data;
    }

    public void addChildren(char c) {
      switch (Trie.this.storeType) {
        case ARRAY:
          // 使用数组映射存储
          childrenForArray[c - 'a'] = new TrieNode(c);
          return;
        case HASH_TABLE:
          // 使用散列表存储
          childrenForHashTable.put(c, new TrieNode(c));
          return;
        case RED_BLACK_TREE:
          // 使用红黑树存储
          childrenForRedBlackTree.put(c, new TrieNode(c));
      }
    }

    public TrieNode getChild(char c) {
      switch (Trie.this.storeType) {
        case ARRAY:
          // 使用数组映射存储
          return childrenForArray[c - 'a'];
        case HASH_TABLE:
          // 使用散列表存储
          return childrenForHashTable.get(c);
        case RED_BLACK_TREE:
          // 使用红黑树存储
          return childrenForRedBlackTree.get(c);
        default:
          return null;
      }
    }

    public Collection<TrieNode> getChildrenForArray() {
      switch (Trie.this.storeType) {
        case ARRAY:
          // 使用数组映射存储
          return Arrays.asList(childrenForArray);
        case HASH_TABLE:
          // 使用散列表存储
          return childrenForHashTable.values();
        case RED_BLACK_TREE:
          // 使用红黑树存储
          return childrenForRedBlackTree.values();
        default:
          return null;
      }
    }

  }

}
