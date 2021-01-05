package com.kaige.datastructure.ch_07_linkedlist;

import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TestThread {
  
  //2、编写代码，使用3个线程，1个线程打印X，一个线程打印Y，一个线程打印Z，同时执行连续打印10次"XYZ"
  
  public static void main(String[] args) {
    print();
  }
  
  public static void print() {
  	/*
    	要求：
    	1. 三个线程
        2. 打印 x、y、z
        3. 三个线程连续打印
        4. 打印 10 次
    */
    // 创建
    Lock lock = new ReentrantLock();
    Condition c1 = lock.newCondition();
    Condition c2 = lock.newCondition();
    Condition c3 = lock.newCondition();
    
    // Object obj = new Object();
    // 定义一个数，或者一个队列之类，存储各个线程的消费数
    ArrayList<String> list = new ArrayList<>();
    list.add("X");
    
    // 线程打印 x
    Thread t1 = new Thread(() -> {
      // 基本功能，打印 10次
      // 要求三个线程顺序执行，需要借助同步工具类。
      // 使数自增？，然后取模，各自线程需要计算自己的位
      // t1线程判断 0 打印 X，否则进行睡眠
      // t2线程判断 1 打印 Y，否则进行睡眠
      // t3线程判断 2 打印 Z，否则进行睡眠
      // 某个线程打印完毕之后，需要唤醒其他线程（可以使用 condition，或者）
      
      lock.lock();
      try {
        for (int i = 0; i < 10; i++) {
          if (list.contains("X")) {
            System.out.println(Thread.currentThread().getName() + ": X " + i);
            list.remove(0);
            list.add("Y");
            c2.signal();
            if (i == 9) {
              return;
            }
          }
          c1.await();
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      } finally {
        lock.unlock();
      }
      
    }, "t1");
    
    // 线程打印 y
    Thread t2 = new Thread(() -> {
      // 基本功能，打印
      
      lock.lock();
      try {
        for (int i = 0; i < 10; i++) {
          if (list.contains("Y")) {
            System.out.println(Thread.currentThread().getName() + ": Y " + i);
            list.remove(0);
            list.add("Z");
            c3.signal();
            if (i == 9) {
              
              return;
            }
          }
          c2.await();
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      } finally {
        lock.unlock();
      }
      
    }, "t2");
    
    // 线程打印 z
    Thread t3 = new Thread(() -> {
      // 基本功能，打印
      lock.lock();
      try {
        for (int i = 0; i < 10; i++) {
          if (list.contains("Z")) {
            System.out.println(Thread.currentThread().getName() + ": Z " + i);
            list.remove(0);
            list.add("X");
            c1.signal();
            if (i == 9) {
              
              return;
            }
          }
          c3.await();
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      } finally {
        lock.unlock();
      }
      
    }, "t3");
    
    t1.start();
    t2.start();
    t3.start();
    
  }
  
}
