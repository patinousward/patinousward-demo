package com.patinousward.interview;

/**
 * synchonized 和  锁
 * https://zhuanlan.zhihu.com/p/112285618
 */
public class SynchonizedInterview07 {

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
        synchronized (SynchonizedInterview07.class) {
        }
       test00();
 /*       {
  public com.patinousward.interview.SynchonizedInterview07();
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
            0: ldc           #2                  // class com/patinousward/interview/SynchonizedInterview07
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
            locals = [ class com/patinousward/interview/SynchonizedInterview07, class java/lang/Object ]
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

}
