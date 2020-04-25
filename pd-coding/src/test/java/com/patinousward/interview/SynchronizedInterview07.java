package com.patinousward.interview;

/**
 * synchronized 和  锁
 * https://zhuanlan.zhihu.com/p/112285618
 */
public class SynchronizedInterview07 {

    /**
     * javac xxx.java
     * 1. javap -v xxx.class
     * public class SynchronizedDemo {
     *     public static void main(String[] args) {
     *         synchronized (SynchronizedDemo.class) {
     *         }
     *         method();
     *     }
     *
     *     private static void method() {
     *     }
     * }
     *
     */
    public void test00(){

    }


    public void test01(){
        synchronized (SynchronizedInterview07.class) {
        }
       test00();
 /*       {
  public com.patinousward.interview.SynchronizedInterview07();
            descriptor: ()V
            flags: ACC_PUBLIC
            Code:
            stack=1, locals=1, args_size=1
            0: aload_0
            1: invokespecial #1                  // Method java/lang/Object."<init>":()V
            4: return
                LineNumberTable:
            line 4: 0

            public void test00();
            descriptor: ()V
            flags: ACC_PUBLIC
            Code:
            stack=0, locals=1, args_size=1
            0: return
                LineNumberTable:
            line 22: 0

            public void test01();
            descriptor: ()V
            flags: ACC_PUBLIC
            Code:
            stack=2, locals=3, args_size=1
            0: ldc           #2                  // class com/patinousward/interview/SynchronizedInterview07
            2: dup
            3: astore_1
            4: monitorenter             //进入锁
            5: aload_1
            6: monitorexit             //退出锁
            7: goto          15
            10: astore_2
            11: aload_1
            12: monitorexit           //再次退出锁
            13: aload_2
            14: athrow
            15: aload_0
            16: invokevirtual #3                  // Method test00:()V
            19: return
                Exception table:
        from    to  target type
            5     7    10   any
            10    13    10   any
            LineNumberTable:
            line 26: 0
            line 27: 5
            line 28: 15
            line 29: 19
            StackMapTable: number_of_entries = 2
            frame_type = 255 *//* full_frame *//*
            offset_delta = 10
            locals = [ class com/patinousward/interview/SynchronizedInterview07, class java/lang/Object ]
            stack = [ class java/lang/Throwable ]
            frame_type = 250 *//* chop *//*
            offset_delta = 4
        }
*/


        /**
         * 关键就是必须要对对象的监视器monitor进行获取，当线程获取monitor后才能继续往下执行，
         * 否则就只能等待。而这个获取的过程是互斥的，即同一时刻只有一个线程能够获取到monitor。
         * 上面的demo中在执行完同步代码块之后紧接着再会去执行一个静态同步方法，而这个方法锁的对象依然就这个类对象，
         * 那么这个正在执行的线程还需要获取该锁吗？
         * 答案是不必的，从上图中就可以看出来，执行静态同步方法的时候就只有一条monitorexit指令，
         * 并没有monitorenter获取锁的指令。
         * 这就是锁的重入性，即在同一锁程中，线程不需要再次获取同一把锁。
         * Synchronized先天具有重入性。每个对象拥有一个计数器，当线程获取该对象锁后，计数器就会加一，释放锁后就会将计数器减一。
         *
         * 任意一个对象都拥有自己的监视器，
         * 当这个对象由同步块或者这个对象的同步方法调用时，
         * 执行方法的线程必须先获取该对象的监视器才能进入同步块和同步方法，
         * 如果没有获取到监视器的线程将会被阻塞在同步块和同步方法的入口处，进入到BLOCKED状态


         该图可以看出，任意线程对Object的访问，首先要获得Object的监视器，
         如果获取失败，该线程就进入同步状态，线程状态变为BLOCKED，
         当Object的监视器占有者释放后，在同步队列中得线程就会有机会重新获取该监视器。
         */
    }

    /**
     * 锁的状态
     */
    public void test02(){
        /**
         *
         * Java对象头里的Mark Word里默认的存放的对象的Hashcode,分代年龄和锁标记位。
         *
         * Java SE 1.6中，锁一共有4种状态，级别从低到高依次是：
         * 无锁状态、偏向锁状态、轻量级锁状态和重量级锁状态，
         * 这几个状态会随着竞争情况逐渐升级。锁可以升级但不能降级，意
         * 味着偏向锁升级成轻量级锁后不能降级成偏向锁。这种锁升级却不能降级的策略，目的是为了提高获得锁和释放锁的效率。
         *
         */
    }

    /**
     * 3.偏向锁
     */
    public void test03() {
        /**
         * 很多情况下，一个锁对象并不会发生被多个线程访问得情况，更多是被同一个线程进行访问，
         * 如果一个锁对象每次都被同一个线程访问，根本没有发生并发，但是每次都进行加锁，那岂不是非常耗费性能。所以偏向锁就被设计出来了。
         *
         * 偏向，也可以理解为偏心。当锁对象第一次被某个线程访问时，它会在其对象头的markOop中记录该线程ID，
         * 那么下次该线程再次访问它时，就不需要进行加锁了。
         *
         * 获取：
         * 当一个线程访问同步块并获取锁时，会在对象头和栈帧中的锁记录里存储锁偏向的线程ID，
         * 以后该线程在进入和退出同步块时不需要进行CAS操作来加锁和解锁，
         * 只需简单地测试一下对象头的Mark Word里是否存储着指向当前线程的偏向锁。
         * 如果测试成功，表示线程已经获得了锁。
         * 如果测试失败，则需要再测试一下Mark Word中偏向锁的标识是否设置成1（表示当前是偏向锁）：
         * 如果没有设置，则使用CAS竞争锁；如果设置了，则尝试使用CAS将对象头的偏向锁指向当前线程
         *
         * 撤销：--指竞争不赢取消（就算CAS失败）
         *
         * 偏向锁的撤销，需要等待全局安全点（在这个时间点上没有正在执行的字节码）----就是说，不可能处于执行同步代码块中。
         * 它会首先暂停拥有偏向锁的线程，然后检查持有偏向锁的线程是否活着，如果线程不处于活动状态，则将对象头设置成无锁状态；
         * 如果线程仍然活着，拥有偏向锁的栈会被执行，遍历偏向对象的锁记录，栈中的锁记录和对象头的Mark Word要么重新偏向于其他线程，
         * 要么恢复到无锁或者标记对象不适合作为偏向锁，最后唤醒暂停的线程。
         *

         */
    }


    /***
     * 4.轻量级锁
     */
    public void test04(){
        /**
         * 1.加锁
         * 线程在执行同步块之前，JVM会先在当前线程的栈桢中创建用于存储锁记录的空间，
         * 并将对象头中的Mark Word复制到锁记录中，官方称为Displaced Mark Word。
         * 然后线程尝试使用CAS将对象头中的Mark Word替换为指向锁记录的指针。
         * 如果成功，当前线程获得锁，如果失败，表示其他线程竞争锁，当前线程便尝试使用自旋来获取锁。
         *
         * 2.解锁
         * 轻量级解锁时，会使用原子的CAS操作将Displaced Mark Word替换回到对象头，
         * 如果成功，则表示没有竞争发生。如果失败，表示当前锁存在竞争，锁就会膨胀成重量级锁。
         * 下图是两个线程同时争夺锁，导致锁膨胀的流程图。
         */
    }

    /**
     * 重量锁
     */
    public void test05(){
        /**
         * 重量级锁，就直接使用互斥量mutex阻塞当前线程
         */

    }
}
