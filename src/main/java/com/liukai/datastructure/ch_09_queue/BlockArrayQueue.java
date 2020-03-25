package com.liukai.datastructure.ch_09_queue;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 9-3 基于数组的阻塞队列
 */
public class BlockArrayQueue<T> extends ArrayQueue<T> {

  /**
   * 锁
   */
  private ReentrantLock lock = new ReentrantLock();

  /**
   * 未满条件
   */
  private Condition notFull = lock.newCondition();

  /**
   * 非空条件
   */
  private Condition notEmpty = lock.newCondition();

  public void put(T e) throws InterruptedException {
    final ReentrantLock lock = this.lock;
    lock.lockInterruptibly();
    try {
      // 阻塞直到队列未满
      while (this.isFull()) {
        // 队列已经满了，则通知非满条件等待
        notFull.await();
      }
      enqueue(e);
    } finally {
      lock.unlock();
    }

  }

  /**
   * 阻塞的入队操作
   *
   * @param item 元素
   * @return 入队是否成功
   */
  @Override
  public boolean enqueue(T item) {
    items[this.tail] = item;
    // 这个操作相比判断是否等于 head 值更加简单
    this.tail = ++this.tail % this.capacity;
    this.count++;
    // 通知非空条件
    this.notEmpty.signal();
    return true;
  }

  /**
   * 阻塞的出队操作
   *
   * @return 队头的元素
   * @throws InterruptedException
   */
  public T task() throws InterruptedException {
    final ReentrantLock lock = this.lock;
    lock.lockInterruptibly();
    try {
      while (this.isEmpty()) {
        // 非空条件等待
        notEmpty.await();
      }
      return dequeue();
    } finally {
      lock.unlock();
    }
  }

  @Override
  public T dequeue() {
    T item = this.items[this.head];
    this.head = ++this.head % this.capacity;
    this.count--;
    // 通知非满条件
    this.notFull.signal();
    return item;
  }

}
