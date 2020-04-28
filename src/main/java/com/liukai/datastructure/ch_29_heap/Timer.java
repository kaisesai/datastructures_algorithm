package com.liukai.datastructure.ch_29_heap;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 29-2 堆应用一：优先级队列之高性能定时器
 * <p>
 * 描述：
 * 假设我们有一个定时器，其中维护了很多定时任务，每个定时任务都设定一个触发时间。我们每隔一个很小时间（比如 1 秒）
 * 遍历所有的任务，判断它们的执行时间是否已经到达，达到则触发执行。
 * <p>
 * 这样的做法很低效，
 * <p>
 * 第一，当任务的执行时间离当前时间很长时，定时器在这段时间内很多次扫描都是无用的；
 * <p>
 * 第二，当任务量很大时，扫描是比较耗时的。
 * <p>
 * 优化：
 * 1. 我们维护一个小顶堆（优先级队列）保存任务，堆顶的任务执行的时间离当前时间越近，优先级越高，先执行。
 * 2. 定时器从小顶堆取堆顶任务，判断它的执行时间与当前时间差，如果时间已经到达则执行，如果没有达到则线程阻塞到任务执行时间
 * 3. 直接使用 JDK 的 ScheduledExecutorService 就可以。
 * 4. 不借助堆，在 MyTask 类的 run 方法中，直接设置睡眠延迟时间
 * 5. 借助线程池，多个任务启动多个线程延迟执行（太鸡肋！）
 */
public class Timer {

  /**
   * 优先级队列：通过任务延迟时间对比优先级
   */
  private static final PriorityBlockingQueue<MyTask> QUEUE = new PriorityBlockingQueue<>(1000,
                                                                                         (o1, o2) -> (int) (
                                                                                           o2.time
                                                                                             - o1.time));

  private static final ThreadPoolExecutor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(100,
                                                                                        Integer.MAX_VALUE,
                                                                                        0,
                                                                                        TimeUnit.NANOSECONDS,
                                                                                        new LinkedBlockingQueue<>());

  private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat(
    "yyyy-MM-dd HH:mm:ss");

  private static long getDelayTime(MyTask task) {
    return task.time - System.currentTimeMillis();
  }

  public static void main(String[] args) throws InterruptedException {
    Timer timer = new Timer();

    Random random = new Random();
    for (int i = 0; i < 10; i++) {
      timer.addTask(new MyTask(() -> {
        System.out.println("你好啊！");
      }, System.currentTimeMillis() + random.nextInt(10) * 1000, "任务" + i));
    }

    System.out.println(Arrays.asList(QUEUE.toArray()));

    // 堆中没有任务时，结束线程池
    while (!QUEUE.isEmpty()) {
      Thread.sleep(5000);
    }
    THREAD_POOL_EXECUTOR.shutdown();
  }

  /**
   * 添加一个任务
   *
   * @param myTask 任务
   */
  public void addTask(MyTask myTask) {
    System.out.println("添加任务：" + myTask);
    // THREAD_POOL_EXECUTOR.execute(myTask);

    // 执行任务
    QUEUE.add(myTask);
    executeTask();
  }

  /**
   * 执行任务
   */
  private void executeTask() {
    THREAD_POOL_EXECUTOR.execute(() -> {
      // 从优先级队列中取出队首任务
      try {
        MyTask task = QUEUE.take();
        long delayTime = getDelayTime(task);
        if (delayTime > 0) {
          // 任务的延迟时间大于0，则睡眠
          Thread.sleep(delayTime);
        }
        task.run();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    });
  }

  /**
   * 任务对象
   */
  static class MyTask implements Runnable {

    private final Runnable runnable;

    /**
     * 任务执行时间戳（单位 ms）
     */
    private final long time;

    private final String taskName;

    public MyTask(Runnable runnable, long time, String taskName) {
      this.runnable = runnable;
      this.time = time;
      this.taskName = taskName;
    }

    @Override
    public void run() {
      // 这里可以不借助堆，直接让当前线程 sleep 延迟时间
      long delayTime = getDelayTime(this);
      if (delayTime > 0) {
        try {
          Thread.sleep(delayTime);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }

      System.out.println("开始执行任务：" + this);
      runnable.run();
    }

    @Override
    public String toString() {
      return "MyTask{" + "time=" + SIMPLE_DATE_FORMAT.format(new Date(time)) + ", taskName='"
        + taskName + '\'' + '}';
    }

  }

}
