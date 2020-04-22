package com.liukai.datastructure.ch_24_binarytree;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 24-1 二叉查找树
 * <p>
 * 规则：
 * 1. 节点的所有左子树的节点值都小于该节点值
 *
 * <p>
 * 特点：
 * 1. 支持快速的删除、插入、查找操作
 * 2. 时间复杂度最好情况是 O(logn)，最坏情况是 O(n)。分别对应退化为链表的二叉查找树和完全二叉查找树
 */
public class BinarySearchTree {

  private Node root;

  public static void main(String[] args) {
    BinarySearchTree tree = new BinarySearchTree();
    // 插入操作
    tree.insert(10);
    tree.insert(5);
    tree.insert(6);
    tree.insert(7);
    tree.insert(1);
    tree.insert(2);
    tree.insert(3);

    printTreeForInOrder(tree);

    // 删除操作
    tree.delete(5);
    tree.delete(10);
    printTreeForInOrder(tree);

    // 递归删除
    tree.deleteForRecursion(tree.root, 1);
    tree.deleteForRecursion(tree.root, 2);
    printTreeForInOrder(tree);

    // 查找操作
    System.out.println(tree.find(1));
    System.out.println("二叉查找树的最小值：" + tree.findMin());
    System.out.println("二叉查找树的最大值：" + tree.findMax());

    System.out.println("二叉查找树的最大深度：depth=" + tree.maxDepthBSF(tree.root));
    System.out.println("二叉查找树的最大深度：depth2=" + tree.maxDepthForBSF());

  }

  /**
   * 中序遍历
   *
   * @param tree
   */
  private static void printTreeForInOrder(BinarySearchTree tree) {
    System.out.print("中序遍历：");
    inOrder(tree.root);
    System.out.println();
  }

  public static void inOrder(Node node) {
    if (node == null) {
      return;
    }
    inOrder(node.left);
    printTreeNode(node);
    inOrder(node.right);
  }

  private static void printTreeNode(Node node) {
    System.out.print(node.data + " ");
  }

  /**
   * 力扣 104 题：二叉树的最大深度
   * 深度优先递归法：求树的高度
   * <p>
   * 思路：递归求当前节点的左右子树的高度的最大值
   *
   * @param node
   * @return
   */
  public int maxDepthBSF(Node node) {
    if (node == null) {
      return 0;
    }
    return Math.max(maxDepthBSF(node.left), maxDepthBSF(node.right)) + 1;
  }

  /**
   * 广度优先搜索：采用层次遍历法求树的深度
   *
   * @return
   */
  public int maxDepthForBSF() {
    // 使用队列记录节点
    Queue<Node> queue = new LinkedList<>();
    queue.add(root);
    // 记录队头指针
    int front = 0;
    // 记录队尾指针
    int rear = queue.size();
    // 记录层数
    int floor = 0;
    while (!queue.isEmpty()) {
      Node node = queue.poll();
      front++;
      if (node.left != null) {
        queue.offer(node.left);
      }

      if (node.right != null) {
        queue.offer(node.right);
      }

      // 当前层已经结束，开始进入下一层
      if (front == rear) {
        front = 0;
        rear = queue.size();
        floor++;
      }
    }

    return floor;
  }

  /**
   * 查找元素
   *
   * @param data 数据
   * @return 节点
   */
  public Node find(int data) {
    Node p = root;
    while (p != null && p.data != data) {
      if (p.data < data) {
        p = p.right;
      } else {
        p = p.left;
      }
    }
    return p;
  }

  /**
   * 添加元素
   *
   * @param data 数据
   */
  public void insert(int data) {
    // 根节点为空
    if (root == null) {
      root = new Node(data);
      return;
    }

    // 根节点不为空
    Node p = root;
    while (true) {
      if (p.data > data) {
        if (p.left == null) {
          p.left = new Node(data);
          return;
        }
        p = p.left;
      } else {
        if (p.right == null) {
          p.right = new Node(data);
          return;
        }
        p = p.right;
      }
    }
  }

  /**
   * 力扣 450 题 删除二叉搜索树中的节点
   * <p>
   * 删除节点
   *
   * @param data
   */
  public void delete(int data) {
    // 从树根节点开始查找
    Node p = root;
    // 要删除节点的父节点
    Node pp = null;
    while (p != null && p.data != data) {
      pp = p;
      if (p.data < data) {
        p = p.right;
      } else {
        p = p.left;
      }
    }
    // 没有找到要删除的节点
    if (p == null) {
      return;
    }

    // 存在左右子节点的情况
    if (p.left != null && p.right != null) {
      // 查找节点的右子树中最小节点
      Node minP = p.right;
      Node minPP = p;
      while (minP.left != null) {
        minPP = minP;
        // 查找节点的左子节点
        minP = minP.left;
      }
      // 将节点的右子树的最小节点值替换到删除节点的位置
      p.data = minP.data;
      // 剩下就删除最小节点的值了
      p = minP;
      pp = minPP;
    }

    // 删除节点是叶子节点或仅剩下一个子节点
    Node child;
    if (p.left != null) {
      child = p.left;
    } else if (p.right != null) {
      child = p.right;
    } else {
      child = null;
    }

    if (pp == null) {
      // 删除的是根节点
      root = child;
    } else if (pp.right == p) {
      pp.right = child;
    } else {
      pp.left = child;
    }
  }

  /**
   * 递归的方法删除节点：删除搜索二叉树中的key对应的节点，并保证二叉搜索树的性质不变
   *
   * @param node 二叉搜索树的根节点root
   * @param key  和一个值key
   * @return 新二叉搜索的根节点的引用
   * @see <a href="https://leetcode-cn.com/problems/delete-node-in-a-bst/solution/shan-chu-er-cha-sou-suo-shu-zhong-de-jie-dian-by-l/">力扣 450. 删除二叉搜索树中的节点</a>
   */
  public Node deleteForRecursion(Node node, int key) {
    if (node == null) {
      return null;
    }

    // 查找指定的节点
    if (node.data > key) {
      // 删除节点在左子树
      node.left = deleteForRecursion(node.left, key);
    } else if (node.data < key) {
      // 删除节点在右子树
      node.right = deleteForRecursion(node.right, key);
    } else {
      // 删除节点等于当前节点
      // 当前节点是叶子节点
      if (node.left == null && node.right == null) {
        node = null;
      } else if (node.right != null) {
        // 当前节点有右子树
        // 寻找它的后继节点值代替它，并且删除
        node.data = successor(node);
        node.right = deleteForRecursion(node.right, node.data);
      } else {
        // 当前节点只有左子树
        // 寻找它的前驱节点值代替它，并且删除
        node.data = predecessor(node);
        node.left = deleteForRecursion(node.left, node.data);
      }
    }
    return node;
  }

  /**
   * 查找前驱节点
   * <p>
   * 节点的左子树中最大的一个节点
   *
   * @param node 节点
   * @return 后继节点
   */
  private int predecessor(Node node) {
    Node predecessor = node.left;
    while (predecessor.right != null) {
      predecessor = predecessor.right;
    }
    return predecessor.data;
  }

  /**
   * 查找后继节点
   * <p>
   * 节点的右子树中最小的一个节点
   *
   * @param node 节点
   * @return 后继节点
   */
  private int successor(Node node) {
    Node successor = node.right;
    while (successor.left != null) {
      successor = successor.left;
    }
    return successor.data;
  }

  /**
   * 查找最小节点
   */
  public Node findMin() {
    if (root == null) {
      return null;
    }

    Node p = root;
    while (p.left != null) {
      p = p.left;
    }
    return p;
  }

  /**
   * 查找最大节点
   */
  public Node findMax() {
    if (root == null) {
      return null;
    }
    Node p = root;
    while (p.right != null) {
      p = p.right;
    }
    return p;
  }

  static class Node {

    int data;

    Node left;

    Node right;

    public Node(int data) {
      this.data = data;
    }

    @Override
    public String toString() {
      return "Node{" + "data=" + data + '}';
    }

  }

}
