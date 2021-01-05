package com.kaige.datastructure.ch_39_backtrack;

/**
 * 39-1 回溯算法——八皇后问题
 *
 * <p>回溯的思想：类似于枚举搜索。我们枚举所有的解，找到满足期望的解。为了有规律的枚举所有可能的解，避免遗漏和重复，
 * 我们把问题求解的过程分成多个阶段。每个阶段，我们都会面临一个岔路口，我们先随意选择一条路走，当发现这条路走不通的时候 （不符合期望的解），就回退到上一个岔路口，另选择一种走法继续走。
 *
 * <p>描述：我们有一个 8*8 的棋盘，希望往里面放 8 个棋子（皇后），每个棋子所在的行、列、对角线都不能有另一个棋子。求出期望找到满足这种要求的放棋子的方式。 * *
 *
 * <p>解题思路：把这个问题划分为 8 个节点，依次将 8 个棋子放到第一行、第二行、第三行......第八行。在放置的过程中，我们不停的检查当前的放置法，是否满足要求。 *
 * 如果满足，则跳到下一行继续放置棋子；如果不满足，那就再换一种放置方法，继续尝试。
 *
 * <p>经典应用：八皇后问题、0-1 背包问题、正则表达式、图的着色、旅行商问题、全排列等等。
 */
public class Backtrack8Queens {

  /**
   * 八皇后问题的结果
   *
   * <p>下标表示行号，值表示放置的哪一列
   */
  private final int[] queenResult = new int[8];

  /**
   * 解题方法个数
   */
  private int solutionNum = 0;

  public static void main(String[] args) {
    Backtrack8Queens queens = new Backtrack8Queens();
    queens.cal8Queens(0);
  }
  
  /**
   * 八皇后问题
   *
   * @param row 行
   */
  private void cal8Queens(int row) {
    if (row == 8) {
      solutionNum++;
      printQueens();
      return;
    }
    
    // 每一行有 8 列，即 8 种不同的放置方法
    for (int column = 0; column < 8; column++) {
      if (isOk(row, column)) {
        // 是否满足要求
        // 第 row 行的棋子放到了 column 列
        queenResult[row] = column;
        // 考察下一行
        cal8Queens(row + 1);
      }
    }
  }

  /**
   * 是否满足要求
   *
   * @param row    行
   * @param column 列
   * @return 是否满足要求
   */
  private boolean isOk(int row, int column) {
    // 左上角元素下标
    int leftup = column - 1;
    // 右上角元素下标
    int rightup = column + 1;
    // 逐行往上检查每一行
    for (int i = row - 1; i >= 0; i--) {

      // 上一行的 column 列有棋子
      if (queenResult[i] == column) {
        return false;
      }

      // 考察左对角线：第 i 行，leftup 列有棋子
      if (leftup >= 0 && queenResult[i] == leftup) {
        return false;
      }

      // 考察右对角线：第 i 行，rightup 列有棋子
      if (rightup < 8 && queenResult[i] == rightup) {
        return false;
      }
      // 左对角线减一
      leftup--;
      // 右对角线加一
      rightup++;
    }
    return true;
  }

  /**
   * 打印八皇后的结果
   *
   * <p>描述：打印一个二维数组
   */
  private void printQueens() {
    System.out.println("第" + solutionNum + "种解法");
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        if (queenResult[i] == j) {
          System.out.print("Q ");
        } else {
          System.out.print("* ");
        }
      }
      System.out.println();
    }
    System.out.println();
  }

}
