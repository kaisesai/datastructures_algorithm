// package com.kaige.datastructure.ch_09_queue;
//
// import sun.misc.Unsafe;
//
// import java.lang.reflect.Field;
//
// /**
//  * 9-4 基于数组的并发队列
//  */
// public class ConcurrentArrayQueue<T> {
//
//   private static final sun.misc.Unsafe UNSAFE;
//
//   private static final long headOffset;
//
//   private static final long tailOffset;
//
//   static {
//     try {
//       Class<?> unsafeClass = Unsafe.class;
//       Field theUnsafeField = unsafeClass.getDeclaredFields()[0];
//       theUnsafeField.setAccessible(true);
//       UNSAFE = (Unsafe) theUnsafeField.get(null);
//       Class<?> k = ConcurrentArrayQueue.class;
//       headOffset = UNSAFE.objectFieldOffset(k.getDeclaredField("head"));
//       tailOffset = UNSAFE.objectFieldOffset(k.getDeclaredField("tail"));
//     } catch (Exception e) {
//       throw new Error(e);
//     }
//   }
//
//   private transient volatile Node<T> head;
//
//   private transient volatile Node<T> tail;
//
//   public ConcurrentArrayQueue() {
//     head = tail = new Node<>(null, null);
//   }
//
//   private static void checkNotNull(Object v) {
//     if (v == null) {
//       throw new NullPointerException();
//     }
//   }
//
//   // Unsafe mechanics
//
//   public boolean offer(T e) {
//     Node<T> newNode = new Node<>(e, null);
//
//     // 定义两个结点 t、p
//     for (Node<T> t = tail, p = t; ; ) {
//       // 获取尾结点的下一个结点
//       Node<T> q = p.next;
//       if (q == null) {
//         // p 是最后一个结点
//         if (p.casNext(null, newNode)) {// 比较并交换设置末尾结点
//           if (p != t) {// 如果发现此时有别的线程操作了，两个变量不一样了
//             casTail(t, newNode);// 设置新节点作为尾结点，允许失败
//           }
//           return true;
//         }
//       } else if (p == q) {
//         p = (t != (t = tail)) ? t : head;
//       } else {
//         p = (p != t && t != (t = tail)) ? t : q;
//       }
//     }
//
//   }
//
//   private boolean casTail(Node<T> cmp, Node<T> val) {
//     return UNSAFE.compareAndSwapObject(this, tailOffset, cmp, val);
//   }
//
//   private boolean casHead(Node<T> cmp, Node<T> val) {
//     return UNSAFE.compareAndSwapObject(this, headOffset, cmp, val);
//   }
//
//   private static class Node<T> {
//
//     private static final sun.misc.Unsafe UNSAFE;
//
//     private static final long itemOffset;
//
//     private static final long nextOffset;
//
//     static {
//       try {
//         // 通过反射获取 Unsafe 的实例
//         Class<?> unsafeClass = Unsafe.class;
//         Field theUnsafeField = unsafeClass.getDeclaredFields()[0];
//         theUnsafeField.setAccessible(true);
//         UNSAFE = (Unsafe) theUnsafeField.get(null);
//         Class<?> k = Node.class;
//         itemOffset = UNSAFE.objectFieldOffset(k.getDeclaredField("item"));
//         nextOffset = UNSAFE.objectFieldOffset(k.getDeclaredField("next"));
//       } catch (Exception e) {
//         throw new Error(e);
//       }
//     }
//
//     volatile T item;
//
//     volatile Node<T> next;
//
//     // Unsafe mechanics
//
//     public Node(T item, Node<T> next) {
//       this.item = item;
//       this.next = next;
//     }
//
//     boolean casItem(T cmp, T val) {
//       return UNSAFE.compareAndSwapObject(this, itemOffset, cmp, val);
//     }
//
//     void lazySetNext(Node<T> val) {
//       UNSAFE.putOrderedObject(this, nextOffset, val);
//     }
//
//     boolean casNext(Node<T> cmp, Node<T> val) {
//       return UNSAFE.compareAndSwapObject(this, nextOffset, cmp, val);
//     }
//
//   }
//
// }
