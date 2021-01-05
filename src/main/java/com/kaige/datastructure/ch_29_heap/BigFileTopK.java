package com.kaige.datastructure.ch_29_heap;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 29-5 堆应用之：求大文件中出现次数多的 Top K 个数据
 * <p>
 * 描述：一个大文件中有 10 亿条数据，其中包含重复的数据，要求求 10 亿数据中出现重复次数多的 Top K 数据
 * <p>
 * 思路：
 * 1. 要求数据重重复次数多的 Top K 数据，第一步需要先去重，并统计每个数据的重复次数，可以使用散列表来存储这些数据。
 * 2. 创建一个容量大小为 K 的大顶堆，将散列表中的数据放入小顶堆中，当堆满之后，重复次数大于堆顶元素的重复次数，则删除堆顶
 * 数据，将该数据插入堆中；否则不做处理。
 * 3. 当遍历完散列表中的全部数据，小顶堆中的数据就是重复次数多的 Top K 数据。
 * 4. 考虑到大文件中的数据量很大，无法一次性把数据都加在到内存。需要将大文件拆分成若干个小文件。
 * 5. 因为相同数据的哈希值是一样的，哈希值相同的数据都会被分配到同一个文件。
 * 6. 把大文件中的每个数据计算哈希值，然后和小文件数量取模操作，得出来的值就是被分配到的小文件编号，
 * 7. 对每个小文件进行上述的 1-3 的操作，这样得出每一个小文件的 Top K 的数据。
 * 8. 然后在利用一个小顶堆，通过各个小文件的 Top K 数据，得出这个所有数据中重复次数最多的 Top K 数据。
 */
public class BigFileTopK {
  
  /**
   * 小文件的临时路径
   */
  public static final String SMALL_FILE_PATH
    = "/Users/kaige/IdeaProjects/datastructures_algorithm/src/main/java/com/kaige/datastructure/ch_29_heap/resources/";

  public static void main(String[] args) {
  
    String bigFile
      = "/Users/kaige/IdeaProjects/datastructures_algorithm/src/main/java/com/kaige/datastructure/ch_29_heap/resources/BigFile";
    // 生成大文件
    generatorBigFile(bigFile);

    BigFileTopK topK = new BigFileTopK();
    PriorityQueue<ValueCount> valueCounts = topK.topK(new File(bigFile), 10);
    System.out.println("大文件中数据重复次数 Top 10 ：" + valueCounts);

  }

  /**
   * 生成大文件
   */
  private static void generatorBigFile(String bigFile) {
    try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(bigFile)))) {

      Random random = new Random();
      for (int i = 0; i < 10000; i++) {
        writer.println(random.nextInt(1000));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * 求各个文件中重复次数的 Top N
   *
   * @param bigFile 大数据文件
   * @param n       Top N 数值
   * @return 文件的 Top N 数据
   */
  public PriorityQueue<ValueCount> topK(File bigFile, int n) {
    // 文件分片
    List<File> smallFiles = bigFileSplit(bigFile, n);
    if (CollectionUtils.isEmpty(smallFiles)) {
      System.out.println("大文件分割小文件数量为 0");
      return null;
    }
    System.out.println(smallFiles);

    // 求各个文件数据 Top N 数据
    List<PriorityQueue<ValueCount>> priorityQueues = toFilesTopN(smallFiles, n);
    if (CollectionUtils.isEmpty(priorityQueues)) {
      System.out.println("各个小文件 Top N 数据集合为 0");
      return null;
    }
    // 求总数据的出现次数 Top N 数据
    PriorityQueue<ValueCount> valueCounts = toValueTopN(n, priorityQueues);
    return valueCounts;
  }

  private PriorityQueue<ValueCount> toValueTopN(int n,
                                                List<PriorityQueue<ValueCount>> smallTopHeaps) {
    PriorityQueue<ValueCount> smallTopHeap = new PriorityQueue<>(n, Comparator
      .comparingInt(ValueCount::getCount));
    smallTopHeaps.forEach(fileSmallTopHeap -> {
      while (!fileSmallTopHeap.isEmpty()) {
        ValueCount valueCount = fileSmallTopHeap.poll();
        if (smallTopHeap.size() < n && valueCount != null) {
          // 堆未满
          smallTopHeap.add(valueCount);
        } else if (smallTopHeap.peek() != null && valueCount != null
          && smallTopHeap.peek().getCount() < valueCount.getCount()) {
          // 堆满后，新加入的数据的重复次数大于小顶堆堆堆顶的数据的重复次数
          // 删除堆顶元素
          smallTopHeap.poll();
          // 加入数据到堆顶
          smallTopHeap.add(valueCount);
        }
      }
    });
    return smallTopHeap;
  }

  /**
   * 生成文件数据出现次数的 Top N 大数据
   *
   * @param smallFiles 文件
   * @param n          Top N 的数量
   * @return 文件数据出现次数的 Top N 大数据
   */
  private List<PriorityQueue<ValueCount>> toFilesTopN(List<File> smallFiles, int n) {
    return smallFiles.stream().map(file -> {
      // 映射成输输入流
      try (LineNumberReader lineNumberReader = new LineNumberReader(new FileReader(file))) {
        // 用户记录文件中的数据以及其出现的次数
        Map<String, Integer> valueCountMap = buildValueCountMap(lineNumberReader);
        // 使用一个小顶堆记录数据重复出现次数的 Top K 数据
        return buildValueTopN(n, valueCountMap);
      } catch (IOException e) {
        e.printStackTrace();
        return null;
      }
    }).filter(Objects::nonNull).collect(Collectors.toList());
  }

  /**
   * 构建数据重复次数的 Top N 大数据
   *
   * @param n             Top N 的数量
   * @param valueCountMap 数据以及其出现次数的映射关系
   * @return 数据重复次数的 Top N 大数据
   */
  private PriorityQueue<ValueCount> buildValueTopN(int n, Map<String, Integer> valueCountMap) {
    PriorityQueue<ValueCount> smallTopHeap = new PriorityQueue<>(n, Comparator
      .comparingInt(ValueCount::getCount));
    valueCountMap.forEach((k, v) -> {
      if (smallTopHeap.size() < n) {
        // 堆未满
        smallTopHeap.add(new ValueCount(k, v));
      } else if (smallTopHeap.peek() != null && v > smallTopHeap.peek().getCount()) {
        // 堆满后，新加入的数据的重复次数大于小顶堆堆堆顶的数据的重复次数
        // 删除堆顶元素
        smallTopHeap.poll();
        // 加入数据到堆顶
        smallTopHeap.add(new ValueCount(k, v));
      }
    });
    return smallTopHeap;
  }

  /**
   * 读取数据并构建数据与重复次数的映射关系
   *
   * @param lineNumberReader 输入流
   * @return 数据与重复次数的映射关系
   * @throws IOException
   */
  private Map<String, Integer> buildValueCountMap(LineNumberReader lineNumberReader)
    throws IOException {
    Map<String, Integer> hashMap = new HashMap<>();
    String line;
    while ((line = lineNumberReader.readLine()) != null) {
      Integer mapOrDefault = hashMap.getOrDefault(line, 0);
      hashMap.put(line, mapOrDefault + 1);
    }
    return hashMap;
  }

  /**
   * 将大文件分割成小文件
   *
   * @return 分割的小文件
   */
  private List<File> bigFileSplit(File bigFile, int needSplitFileNum) {
    List<File> smallFiles = buildSmallFiles(bigFile, needSplitFileNum);
    // 计算每个数据的哈希值，对于小文件进行取模
    try (LineNumberReader lineNumberReader = new LineNumberReader(new FileReader(bigFile))) {
      List<PrintWriter> writers = null;
      try {
        writers = buildPrintWriters(smallFiles);
        // 分配数据到小文件
        String line;
        while ((line = lineNumberReader.readLine()) != null) {
          // 计算哈希以及取模操作
          int fileNum = line.hashCode() % writers.size();
          // 写入对应的流
          writers.get(fileNum).println(line);
        }
      } finally {
        // 关闭流
        closeResources(writers);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return smallFiles;
  }

  /**
   * 生成多个文件
   *
   * @param bigFile          原始文件
   * @param needSplitFileNum 需要分割的文件数量
   * @return 多个文件
   */
  private List<File> buildSmallFiles(File bigFile, int needSplitFileNum) {
    List<File> smallFiles = Lists.newArrayListWithCapacity(needSplitFileNum);
    // 读取大文件数据
    for (int i = 0; i < needSplitFileNum; i++) {
      smallFiles.add(new File(generatorSmallFilename(bigFile, i)));
    }
    return smallFiles;
  }

  /**
   * 构建输出流
   *
   * @param smallFiles 文件
   * @return 文件流
   */
  private List<PrintWriter> buildPrintWriters(List<File> smallFiles) {
    // 创建文件输出流
    return smallFiles.stream().map(file -> {
      try {
        return new PrintWriter(new BufferedWriter(new FileWriter(file)));
      } catch (IOException e) {
        e.printStackTrace();
      }
      return null;
    }).filter(Objects::nonNull).collect(Collectors.toList());
  }

  /**
   * 关闭资源
   *
   * @param resources 资源
   */
  private void closeResources(List<? extends Closeable> resources) {
    if (resources == null || resources.isEmpty()) {
      return;
    }
    for (Closeable reader : resources) {
      try {
        reader.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * 生成小文件的名称
   * <p>
   * 格式：{临时文件目录/原始文件名称_当前时间格式_文件编号}
   *
   * @param bigFile 原始文件
   * @param fileNum 编号
   * @return 小文件的名称
   */
  private String generatorSmallFilename(File bigFile, int fileNum) {
    return SMALL_FILE_PATH + DateTimeFormatter.ISO_DATE.format(LocalDateTime.now()) + "-" + bigFile
      .getName() + "_" + fileNum;
  }

  @Data
  @AllArgsConstructor
  static class ValueCount {

    String value;

    int count;

  }

}
