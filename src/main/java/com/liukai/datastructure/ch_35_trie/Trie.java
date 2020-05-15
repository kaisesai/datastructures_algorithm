package com.liukai.datastructure.ch_35_trie;

import java.util.*;

/**
 * 35-1 Trie 树
 *
 * <p>描述：Trie 树是一个字典树，是一种用于处理字符串匹配的数据结构。
 *
 * <p>核心原理：利用字符串之间的相同公共前缀，将重复的前缀合并在一起。
 *
 * <p>主要操作： 1. 插入字符串 2. 查找字符串
 *
 * <p>特点： 1. 构建 Trie 树的时间复杂度是 O(n)，n 为所有的字符串个数的长度之和。 2. 查询的时间复杂度是 O(k)，k 为要查询字符串的长度
 *
 * <p>适用场景： 1. 与红黑树、散列表相比不适合用于字符串匹配 2. 适用于字符串搜索提示
 */
public class Trie {

  /**
   * Trie 树子节点存储方式
   */
  private final TrieNodeSTOREType storeType;

  /** 根节点，存储无意义的字符 */
  private final TrieNode root;

  public Trie(TrieNodeSTOREType storeType) {
    this.storeType = storeType;
    // 初始化根节点
    root = createTrieNode('/');
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
        for (TrieNode children : node.getChildren()) {
          if (children != null) {
            queue.add(children);
            result.add(prefixStr + children.getData());
          }
        }
      }
    }
    return result;
  }

  private TrieNode createTrieNode(char c) {
    switch (this.storeType) {
      case ARRAY:
        // 使用数组映射存储
        return new TrieNodeForArray(c);
      case HASH_TABLE:
        // 使用散列表存储
        return new TrieNodeForHashTable(c);
      case RED_BLACK_TREE:
        // 使用红黑树存储
        return new TrieNodeForRedBlackTree(c);
      default:
        throw new IllegalStateException("Trie 树子节点存储类型 storeType 不存在！");
    }
  }

  /**
   * Trie 树子节点存储类型枚举
   */
  enum TrieNodeSTOREType {
    ARRAY, // 数组映射
    HASH_TABLE, // 散列表
    RED_BLACK_TREE // 红黑树
  }

  abstract static class TrieNode {

    char data;

    boolean isEndingChar = false;

    public TrieNode(char data) {
      this.data = data;
    }

    public char getData() {
      return data;
    }

    public abstract void addChildren(char c);

    public abstract TrieNode getChild(char c);

    public abstract Collection<TrieNode> getChildren();

  }

  /** 红黑树实现的 Trie 树 */
  static class TrieNodeForRedBlackTree extends TrieNode {

    // 使用红黑树存储
    TreeMap<Character, TrieNode> children = new TreeMap<>();

    public TrieNodeForRedBlackTree(char data) {
      super(data);
    }

    @Override
    public void addChildren(char c) {
      children.put(c, new TrieNodeForRedBlackTree(c));
    }

    @Override
    public TrieNode getChild(char c) {
      return children.get(c);
    }

    @Override
    public Collection<TrieNode> getChildren() {
      return children.values();
    }

  }

  /** 数组映射表实现的 Trie 树 */
  static class TrieNodeForArray extends TrieNode {

    /**
     * 数组，字符与数组下标映射
     *
     * <p>通过 字符- 'a' 的 ASCII 码值与数组下标映射
     */
    TrieNode[] childrenForArray = new TrieNodeForArray[26];

    public TrieNodeForArray(char data) {
      super(data);
    }

    @Override
    public void addChildren(char c) {
      childrenForArray[c - 'a'] = new TrieNodeForArray(c);
    }

    @Override
    public TrieNode getChild(char c) {
      return childrenForArray[c - 'a'];
    }

    @Override
    public Collection<TrieNode> getChildren() {
      return Arrays.asList(childrenForArray);
    }

  }

  /** 散列表实现的 Trie 树 */
  static class TrieNodeForHashTable extends TrieNode {

    // 使用散列表存储
    HashMap<Character, TrieNode> children = new HashMap<>();

    public TrieNodeForHashTable(char data) {
      super(data);
    }

    @Override
    public void addChildren(char c) {
      children.put(c, new TrieNodeForHashTable(c));
    }

    @Override
    public TrieNode getChild(char c) {
      return children.get(c);
    }

    @Override
    public Collection<TrieNode> getChildren() {
      return children.values();
    }
  }
}
