package com.liukai.datastructure.ch_40_dynamicprogramming;

import org.apache.commons.lang3.RandomUtils;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

/**
 * 40-3 动态规划——改造的杨辉三角
 *
 * <p>问题：“杨辉三角”不知道你听说过吗？我们现在对它进行一些改造。每个位置的数字可以随意填写，经过某个数字只能到达下面一层相邻的两个数字。
 * 假设你站在第一层，往下移动，我们把移动到最底层所经过的所有数字之和，定义为路径的长度。请你编程求出从最高层移动到最底层的最短路径长度。
 */
public class YangHuiSanJiao {

  public static void main(String[] args) {
    int level = 5;
    // 生成杨辉三角树
    Node node = generateNode(level);
    // 将树转换为一个二维数组
    System.out.println("树节点信息：");
    int[][] matrix = toMatrixForNode(node, level);
    // 打印二维数组的信息
    printDoubleArray(matrix);
    System.out.println();

    // 求最短
    int minDist = minDist(matrix);
    System.out.println();
    System.out.println("最短路径 = " + minDist);

    /*
    树节点信息：
    67
    28 17
    97 63 2
    70 63 37 33
    16 40 69 51 96

    状态表信息：
    67(67)
    95(28) 84(17)
    192(97) 147(63) 86(2)
    262(70) 210(63) 123(37) 119(33)
    278(16) 250(40) 192(69) 170(51) 215(96)

    最短路径 = 170
    */

  }

  /**
   * int[][] matrix = {{5}, {7,8}, {2,3,4}, {4,9,6,1}, {2,7,9,4,5}};
   *
   * @param matrix 节点信息
   * @return 最短路径值
   */
  public static int minDist(int[][] matrix) {
    // 状态数组，值为第 i 行，第 j 次决策最短路径
    int[][] states = new int[matrix.length][matrix.length];
    // 初始化第一行数据
    states[0][0] = matrix[0][0];

    for (int i = 1; i < matrix.length; i++) {
      // 动态规划
      for (int j = 0; j < matrix[i].length; j++) {

        // 状态存在，则返回
        if (states[i][j] > 0) {
          continue;
        }

        // 父节点，有可能是 [i-1][j] 或者 [i-1][j+1]
        if (j == 0) {
          // 判断当前遍历的 j 的位置：如果 j=0，则父节点为 [i-1][j]；
          states[i][j] = states[i - 1][j] + matrix[i][j];
        } else if (j == (matrix[i].length - 1)) {
          // j=  matrix[i-1].length-1，则父节点为 [i-1][j]；
          states[i][j] = states[i - 1][j - 1] + matrix[i][j];
        } else {
          // 0 < j < matrix[i-1].length-1，则 父节点为两个，[i-1][j-1]，[i-1][j]，需要处理两次，比较两次路径的大小，选择最小的记录下来。
          int distLeft = states[i - 1][j - 1];
          int distRight = states[i - 1][j];
          states[i][j] = matrix[i][j] + Math.min(distLeft, distRight);
        }
      }
    }

    // 打印出路径状态表信息
    printStates(matrix, states);

    // 求最后一行的中总路径的最小值
    int minDist = Integer.MAX_VALUE;
    for (int i = 0; i < matrix.length; i++) {
      if (states[matrix.length - 1][i] < minDist) {
        minDist = states[matrix.length - 1][i];
      }
    }

    return minDist;
  }

  private static void printStates(int[][] matrix, int[][] states) {
    System.out.println("状态表信息：");
    StringBuilder sb;
    for (int i = 0; i < matrix.length; i++) {
      // 行
      sb = new StringBuilder();
      for (int j = 0; j < matrix[i].length; j++) {
        // 列
        sb.append(states[i][j]).append("(").append(matrix[i][j]).append(")").append(' ');
      }
      System.out.println(sb);
    }
  }

  private static void printDoubleArray(int[][] matrix) {
    StringBuilder sb;
    for (int i = 0; i < matrix.length; i++) {
      // 行
      sb = new StringBuilder();
      for (int j = 0; j < matrix[i].length; j++) {
        // 列
        sb.append(matrix[i][j]).append(' ');
      }
      System.out.println(sb);
    }
  }

  /**
   * 遍历一棵去除重复节点的杨辉三角树
   *
   * @param node  树根节点
   * @param level 树层数
   */
  public static int[][] toMatrixForNode(Node node, int level) {
    Queue<Node> queue = new LinkedList<>();
    queue.offer(node);

    Set<Node> set = new HashSet<>();

    /*
     求行号。当前行号 h 0，当前元素 i 0，
     元素 0，在 0 行；             规律是：h=i-1
     元素 1，2，在 1 行；
     元素 3，4，5，在 2 行；
     元素 6，7，8，9，在 3 行；
    */

    int[][] matrix = new int[level][level];

    int h = 0;
    // int i = 0;
    int index = 0;
    while (!queue.isEmpty()) {
      Node n1 = queue.poll();
      if (!set.contains(n1)) {
        set.add(n1);
        matrix[h][index] = n1.value;
        // i++;
        index++;

        if (h == index - 1) {
          // 第 h 行元素已经满了，恢复索引值
          index = 0;
          h++;
        }
      }
      if (n1.left != null && !set.contains(n1.left)) {
        queue.offer(n1.left);
      }
      if (n1.right != null && !set.contains(n1.right)) {
        queue.offer(n1.right);
      }
    }

    // 返回一个新的数组
    int[][] result = new int[level][];
    for (int i = 0; i < matrix.length; i++) {
      // 初始化 result 一维数组
      for (int j = 0; j < matrix[i].length; j++) {
        if (matrix[i][j] == 0) {
          result[i] = new int[j];
          break;
        } else if (j == matrix[i].length - 1) {
          result[i] = new int[j + 1];
          break;
        }
      }
      // 填充数组
      System.arraycopy(matrix[i], 0, result[i], 0, result[i].length);
    }

    return result;
  }

  /**
   * 生成一棵改造之后“的杨辉三角”树。
   *
   * <p>树的特点：每个位置的数字可以随意填写，经过某个数字只能到达下面一层相邻的两个数字。假设你站在第一层，往下移动，我们把移动到最底层所经过的所有数字之和，定义为路径的长度。
   *
   * <p>请你编程求出从最高层移动到最底层的最短路径长度。
   *
   * @param n 树的层数
   * @return 树的顶点
   */
  public static Node generateNode(int n) {
    // 根节点
    Node root = new Node(RandomUtils.nextInt(1, 100));
    Queue<Node> queue = new LinkedList<>();
    queue.add(root);

    // n 表示层数
    for (int i = 0; i < n - 1; i++) {
      // 元素个数

      Node lastRight = null;
      for (int i1 = 0; i1 < i + 1; i1++) {
        if (queue.isEmpty()) {
          break;
        }

        Node node = queue.poll();

        // 左节点
        Node c1;
        if (lastRight != null) {
          c1 = lastRight;
        } else {
          c1 = new Node(RandomUtils.nextInt(0, 100));
          // 放入队列
          if (i < n - 1) {
            queue.offer(c1);
          }
        }
        node.left = c1;

        // 右节点
        Node c2 = new Node(RandomUtils.nextInt(0, 100));
        node.right = c2;
        if (i < n - 1) {
          queue.offer(c2);
        }

        if (i1 != i) {
          lastRight = c2;
        }
      }
    }

    return root;
  }

  static class Node {

    int value;

    Node left;

    Node right;

    public Node(int value) {
      this.value = value;
    }

  }

}
