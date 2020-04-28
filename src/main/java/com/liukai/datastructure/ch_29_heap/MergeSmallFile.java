package com.liukai.datastructure.ch_29_heap;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 29-1 堆的应用一：优先级队列之合并小文件
 * <p>
 * 描述：
 * 将多个存有有序的小文件中的字符串数据合并成一个含有有序的数据的大文件。
 * <p>
 * 思路：
 * 1. 依次读取每个小文件，维护一个小顶堆，将文件数据放入到堆中，堆顶的元素就是最小的数据
 * 2. 删除堆顶的元素，并将元素写入到一个文件中，直到删除完堆中的数据。
 * <p>
 * 实现方式：
 * 1. 这里当小文件数据量不大时，可以选择一次性把所有小文件的数据都放入小顶堆中，如果文件的数据量很大，会导致内存溢出。这样做的好处是速度快。缺点是消耗内存。
 * 2. 如果每个小文件数据量都比较大，那就创建一个容量不是很大的小顶堆，比如容量是 100，然后开启文件数量个流读取文件，遍历流，每个流读取一次数据，并放入堆中。遍历完一遍流之后，
 * 堆顶的元素就是本次遍历读取各个文件中最小的元素，然后将堆顶数据写入流，并删除。然后继续下次依次遍历读取各个流，重复上述动作，直到所有的流都读取完毕，以及将堆中的数据都写入大文件。
 * <p>
 * 这里我们采用方式二实现。
 */
public class MergeSmallFile {

  /**
   * 堆的默认容量
   */
  public static final int HEAP_INITIAL_CAPACITY = 10;

  /**
   * 使用优先级队列，采用默认的自然排序——小顶堆
   */
  private final PriorityQueue<Integer> heap = new PriorityQueue<>(HEAP_INITIAL_CAPACITY);

  public static void main(String[] args) {

    File directory = new File(
      "/Users/liukai/IdeaProjects/datastructures_algorithm/src/main/java/com/liukai/datastructure/ch_29_heap/resources");
    if (!directory.exists() || !directory.isDirectory()) {
      System.out.println("无效的目录路径");
      return;
    }

    File[] files = directory.listFiles();
    MergeSmallFile mergeSmallFile = new MergeSmallFile();
    String newFile
      = "/Users/liukai/IdeaProjects/datastructures_algorithm/src/main/java/com/liukai/datastructure/ch_29_heap/resources/newFile.txt";
    mergeSmallFile.mergeFile(files, newFile);

  }

  /**
   * 将多个包含有序数据的文件的按照顺序合并到一个文件中
   *
   * @param files   有序数据的文件
   * @param newFile 新的文件
   */
  public void mergeFile(File[] files, String newFile) {
    try (
      PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(new File(newFile))))
    ) {
      List<BufferedReader> bufferedReaders = readFiles(files);
      try {
        // 使用散列表标识流是否读取完毕
        Map<BufferedReader, Boolean> readerBooleanMap = bufferedReaders.stream()
          .collect(Collectors.toMap(reader -> reader, reader -> false));
        // 遍历文件流从中读取数据到堆
        int notEndedReaderSize = readerBooleanMap.size();
        while (notEndedReaderSize > 0) {
          for (Map.Entry<BufferedReader, Boolean> readerBooleanEntry : readerBooleanMap
            .entrySet()) {
            if (readerBooleanEntry.getValue()) {
              continue;
            }
            try {
              String line = readerBooleanEntry.getKey().readLine();
              if (line == null) {
                // 标记流已经读取完毕
                readerBooleanEntry.setValue(true);
                // 可用的流数量减一
                notEndedReaderSize--;
                continue;
              }
              Integer value = Integer.valueOf(line);
              heap.add(value);
            } catch (IOException e) {
              e.printStackTrace();
            }
          }
          // 将堆顶数据写入大文件
          writeHeapTopDataToFile(writer);
        }

        // 将堆数据写入大文件
        writeHeapDataToFile(writer);
      } finally {
        // 关闭文件
        closeReaders(bufferedReaders);
      }

    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  /**
   * 将堆顶的数据写入流, 并且删除堆顶数据
   *
   * @param writer 输出流
   */
  private void writeHeapTopDataToFile(PrintWriter writer) {
    if (!heap.isEmpty()) {
      writer.println(heap.poll());
    }
  }

  /**
   * 将堆中的数据写入流, 并且删除堆中数据
   *
   * @param writer 输出流
   */
  private void writeHeapDataToFile(PrintWriter writer) {
    // 这的时间复杂度是 nlogn
    while (!heap.isEmpty()) {
      writer.println(heap.poll());
    }
    writer.flush();
  }

  /**
   * 根据文件路径读取文件
   *
   * @param files 文件路径
   * @return 文件流
   */
  public List<BufferedReader> readFiles(File[] files) {
    if (files == null || files.length == 0) {
      return Collections.emptyList();
    }
    return Arrays.stream(files).filter(File::exists).map(file -> {
      BufferedReader reader = null;
      try {
        reader = new BufferedReader(new FileReader(file));
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }
      return reader;

    }).collect(Collectors.toList());
  }

  /**
   * 关闭流
   *
   * @param bufferedReaders 流
   */
  public void closeReaders(List<BufferedReader> bufferedReaders) {
    if (bufferedReaders == null || bufferedReaders.isEmpty()) {
      return;
    }
    for (BufferedReader bufferedReader : bufferedReaders) {
      try {
        bufferedReader.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

}
