package com.liukai.datastructure.ch_23_tree;

import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * 23-1 二叉树
 * <p>
 * 存储方式：<br/>
 * 1. 链式存储法<br/>
 * 2. 数组顺序存储法
 * <p>
 * 遍历方式：<br/>
 * 1. 前序遍历<br/>
 * 2. 中序遍历<br/>
 * 3. 后序遍历<br/>
 * 4. 按照层遍历
 */
public class BinaryTree {

  public static void main(String[] args) {
    TreeNode<Integer> node = new TreeNode<>(10, null, null);
    node.left = new TreeNode<>(11, null, null);
    node.right = new TreeNode<>(21, null, null);
    node.left.left = new TreeNode<>(12, null, null);
    node.left.right = new TreeNode<>(13, null, null);
    node.right.left = new TreeNode<>(22, null, null);
    node.right.right = new TreeNode<>(23, null, null);

    // 前序遍历
    System.out.print("前序遍历：");
    preOrder(node);
    System.out.println();
    // 中序遍历
    System.out.print("中序遍历：");
    inOrder(node);
    System.out.println();
    // 后序遍历
    System.out.print("后序遍历：");
    postOrder(node);
    System.out.println();
    // 层遍历
    System.out.print("按层遍历：");
    tierOrder(node);

  }

  /**
   * 前序遍历
   * <p>
   * 先打印当前节点
   * ，再打印左子树，最后打印右子树
   *
   * @param node
   */
  public static <T> void preOrder(TreeNode<T> node) {
    if (node == null) {
      return;
    }
    printTreeNode(node);
    preOrder(node.left);
    preOrder(node.right);
  }

  /**
   * 中序遍历
   * <p>
   * 先打印左子树，再打印当前节点
   * ，最后打印右子树
   *
   * @param node
   */
  public static <T> void inOrder(TreeNode<T> node) {
    if (node == null) {
      return;
    }
    inOrder(node.left);
    printTreeNode(node);
    inOrder(node.right);
  }

  /**
   * 后序遍历
   * <p>
   * 先打印左子树，再打印右子树，最后打印当前节点
   *
   * @param node
   */
  public static <T> void postOrder(TreeNode<T> node) {
    if (node == null) {
      return;
    }
    postOrder(node.left);
    postOrder(node.right);
    printTreeNode(node);
  }

  /**
   * 按照层来遍历
   *
   * @param node
   */
  public static <T> void tierOrder(TreeNode<T> node) {
    if (node == null) {
      return;
    }

    List<T> result = new ArrayList<>();
    // 通过队列来保存有子节点的节点
    Queue<TreeNode<T>> queue = new LinkedList<>();
    queue.offer(node);

    while (!queue.isEmpty()) {
      // 从队列头部取出一个节点
      TreeNode<T> treeNode = queue.poll();
      // 将节点数据放入结果集
      result.add(treeNode.data);
      // 如果节点有子节点则放入队列中
      if (treeNode.left != null) {
        queue.offer(treeNode.left);
      }
      if (treeNode.right != null) {
        queue.offer(treeNode.right);
      }
    }

    System.out.println(result);
  }

  private static <T> void printTreeNode(TreeNode<T> node) {
    System.out.print(node.data + " ");
  }

  @AllArgsConstructor
  static class TreeNode<T> {

    T data;

    TreeNode<T> left;

    TreeNode<T> right;

  }

}
