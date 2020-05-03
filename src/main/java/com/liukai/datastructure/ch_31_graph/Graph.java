package com.liukai.datastructure.ch_31_graph;

import java.util.*;

/**
 * 30-1 图
 * <p>
 * 描述：它是一种非线程的数据结构。
 * <p>
 * 概念：
 * 1. 图中的元素叫作顶点
 * 2. 顶点与顶点之间的关系叫作边
 * 3. 有方向的边的图叫做有向图，无方向的边的图叫作无向图
 * 4. 顶点的边的个数叫作度
 * 5. 有方向的边叫作入度和出度，顶点指向其他顶点的边叫作出度，其他顶点指向该顶点的边叫作入度
 * 6. 在边上加上权重构成的图，就是带权图。
 * <p>
 * 存储方式：
 * 1. 邻接矩阵，基于二维数组实现。优点是简单、计算高效，缺点是浪费空间
 * <p>
 * 2. 邻接表，类似于散列表，有数组+链表的方式构建，链表还可以升级为红黑树、跳表、散列表等数据结构提高查找效率。
 */
public class Graph {

  /**
   * 图的大小
   */
  private final int v;

  // 基于邻接表实现
  private final LinkedList<Integer>[] adj;

  public Graph(int v) {
    this.v = v;
    adj = new LinkedList[v];

    for (int i = 0; i < v; i++) {
      adj[i] = new LinkedList<>();
    }
  }

  public static void main(String[] args) {
    Graph graph = new Graph(100);
    graph.addEdge(1, 2);
    graph.addEdge(1, 3);
    graph.addEdge(2, 4);
    graph.addEdge(2, 5);
    graph.addEdge(2, 6);
    graph.addEdge(3, 7);
    graph.addEdge(3, 8);
    graph.addEdge(4, 9);
    graph.addEdge(4, 10);
    graph.addEdge(10, 11);
    graph.addEdge(10, 12);

    // 广度优先搜索
    System.out.print("广度优先搜索：");
    graph.bfs(1, 11);
    System.out.println();
    // 深度优先搜索
    System.out.print("深度优先搜索：");
    graph.dfs(1, 11);
    System.out.println();
    // 寻找三度好友
    List<Integer> threeDegreeRelation = graph.threeDegreeRelationDfs(1);
    System.out.println("深度优先搜索 1 的三度好友为：" + threeDegreeRelation);
    // 寻找三度好友
    List<Integer> threeDegreeRelationBfs = graph.threeDegreeRelationBfs(1);
    System.out.println("广度优先搜索 1 的三度好友为：" + threeDegreeRelationBfs);

  }

  /**
   * 三度好友关系——广度优先搜索
   *
   * @param s 起始顶点
   * @return
   */
  public List<Integer> threeDegreeRelationBfs(int s) {
    // 记录顶点是否被访问
    boolean[] visited = new boolean[v];
    visited[s] = true;

    // 记录访问的顶点
    Queue<Integer> queue = new LinkedList<>();
    queue.add(s);

    List<Integer> result = new ArrayList<>();

    // 记录每个顶点到起始顶点的距离
    int[] degree = new int[v];

    // 记录总共遍历的次数
    int cycleNum = 0;

    // 广度优先搜索，遍历整张图
    // 从这个顶点的所有全部信息
    while (!queue.isEmpty()) {
      Integer w = queue.poll();

      // 顶点离起始顶点距离超过 3 时则退出
      if (degree[w] == 3) {
        continue;
      }

      // 遍历其关注的顶点
      for (Integer q : adj[w]) {
        // 没有访问过的顶点进行记录
        if (!visited[q]) {
          visited[q] = true;
          // 添加到队列
          queue.add(q);
          // 记录当前顶点到起始顶点的距离，查找其前驱顶点的距离
          degree[q] = degree[w] + 1;
          cycleNum++;
          // 记录结果
          result.add(q);
        }
      }
    }
    System.out.println("广度优先搜索三度好友关系一共遍历的次数：" + cycleNum);
    return result;
  }

  /**
   * 三度好友关系——深度优先搜索
   * <p>
   * 描述：求顶点的好友的好友的好友
   *
   * @param s 起始顶点
   */
  public List<Integer> threeDegreeRelationDfs(int s) {
    // 寻找好友的好友的好友
    // 采用广度优先搜索算法
    boolean[] visited = new boolean[v];
    visited[s] = true;
    List<Integer> relations = new ArrayList<>();
    // 递归寻找顶点的关系
    int cycleNum = findDegreeRelation(visited, relations, s, 0);
    System.out.println("深度优先搜索三度好友关系一共遍历的次数：" + cycleNum);
    return relations;
  }

  /**
   * 递归寻找顶点关系——深度优先搜索
   *
   * @param visited
   * @param relations
   * @param p
   * @param level
   * @return
   */
  private int findDegreeRelation(boolean[] visited, List<Integer> relations, Integer p, int level) {
    if (level == 3) {
      return 1;
    }
    int cycleNum = 0;
    for (int h = 0; h < adj[p].size(); h++) {
      Integer t = adj[p].get(h);
      if (!visited[t]) {
        relations.add(t);
        visited[p] = true;
        cycleNum += findDegreeRelation(visited, relations, t, level + 1);
      }
      cycleNum++;
    }
    return cycleNum;
  }

  /**
   * 无向图中存边
   *
   * @param s 起始顶点
   * @param z 终止顶点
   */
  public void addEdge(int s, int z) {
    adj[s].add(z);
    adj[z].add(s);
  }

  /**
   * 深度优先搜索
   * <p>
   * 描述：利用回溯思想
   *
   * @param s 起始顶点
   * @param t 终止顶点
   */
  public void dfs(int s, int t) {

    boolean[] visited = new boolean[v];
    visited[s] = true;

    int[] prev = new int[v];
    Arrays.fill(prev, -1);

    recurDfs(prev, visited, s, t);
    print(prev, s, t);

  }

  /**
   * 递归深度优先搜索
   *
   * @param prev    记录顶点前向顶点
   * @param visited 记录顶点是否已被访问
   * @param w       查询开始顶点
   * @param t       目标顶点
   * @return
   */
  private boolean recurDfs(int[] prev, boolean[] visited, int w, int t) {
    visited[w] = true;
    if (w == t) {
      return true;
    }
    boolean found;
    for (int i = 0; i < adj[w].size(); i++) {
      Integer q = adj[w].get(i);
      if (!visited[q]) {
        prev[q] = w;
        found = recurDfs(prev, visited, q, t);
        if (found) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * 广度优先搜索算法
   * <p>
   * 描述：从一个顶点开始，依次从最近的层开始搜索，层层推进，直到找到目标顶点。最终找到的路径是顶点 s 到顶点 t 之间最短路径。
   *
   * @param s 顶点
   * @param t 顶点
   */
  public void bfs(int s, int t) {
    // 记录顶点是否被访问
    boolean[] visited = new boolean[v];
    visited[s] = true;

    // 记录顶点的前驱顶点
    int[] prev = new int[v];
    Arrays.fill(prev, -1);

    // 记录访问的顶点
    Queue<Integer> queue = new LinkedList<>();
    queue.add(s);

    while (!queue.isEmpty()) {
      Integer w = queue.poll();

      for (int i = 0; i < adj[w].size(); i++) {
        Integer q = adj[w].get(i);
        // 先判断是否访问过
        if (!visited[q]) {
          prev[q] = w;
          if (q == t) {
            print(prev, s, t);
            return;
          }
          visited[q] = true;
          queue.add(q);
        }
      }
    }
  }

  /**
   * 递归打印 s->t 的路径
   *
   * @param prev 顶点的访问记录，数组下标对应，值对应访问该顶点的前一个顶点
   * @param s    顶点
   * @param t    顶点
   */
  private void print(int[] prev, int s, int t) {
    if (prev[t] != -1 && s != t) {
      print(prev, s, prev[t]);
    }
    System.out.print(t + " ");
  }

}
