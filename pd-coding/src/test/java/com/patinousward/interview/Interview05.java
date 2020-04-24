package com.patinousward.interview;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * volatile关键字
 * https://juejin.im/post/5a2b53b7f265da432a7b821c
 */
public class Interview05 {

    @Test
    public void test01() {
        AtomicInteger atomicInteger = new AtomicInteger(-1);
        int i = atomicInteger.incrementAndGet();//先+1 再获取
        int i2 = atomicInteger.getAndIncrement(); //先获取，再+1
        System.out.println(i);//0
        System.out.println(i2);//0

        //private volatile int value;

        //
    }

    /**
     * 2.作用
     */
    public void test02() {
        /**
         * volatile作用
         * 1 . 保证了不同线程对该变量操作的内存可见性;
         * 2 . 禁止指令重排序
         */
    }

    /**
     * 内存可见性，什么又是重排序
     */
    public void test03() {
        /**
         *          * Java虚拟机规范试图定义一种Java内存模型（JMM）,来屏蔽掉各种硬件和操作系统的内存访问差异，让Java程序在各种平台上都能达到一致的内存访问效果。简单来说，由于CPU执行指令的速度是很快的，但是内存访问的速度就慢了很多，相差的不是一个数量级，所以搞处理器的那群大佬们又在CPU里加了好几层高速缓存。
         *          * 在Java内存模型里，对上述的优化又进行了一波抽象。JMM规定所有变量都是存在主存中的，类似于上面提到的普通内存，每个线程又包含自己的工作内存，方便理解就可以看成CPU上的寄存器或者高速缓存。所以线程的操作都是以工作内存为主，它们只能访问自己的工作内存，且工作前后都要把值在同步回主内存。
         *
         *
         * 在线程执行时，首先会从主存中read变量值，再load到工作内存中的副本中，然后再传给处理器执行，执行完毕后再给工作内存中的副本赋值，随后工作内存再把值传回给主存，主存中的值才更新。
         */

    }

    /**
     * 三大特征：原子，可见，有序
     *
     */
    public void test04(){
        /**
         * JMM主要就是围绕着如何在并发过程中如何处理原子性、可见性和有序性这3个特征来建立的，通过解决这三个问题，可以解除缓存不一致的问题。而volatile跟可见性和有序性都有关。
         *
         *1.原子性
         * i = 2;
         * j = i;
         * i++;
         * i = i + 1；
         * 复制代码上面4个操作中，i=2是读取操作，必定是原子性操作，j=i你以为是原子性操作，
         * 其实吧，分为两步，一是读取i的值，然后再赋值给j,这就是2步操作了，称不上原子操作，
         * i++和i = i + 1其实是等效的，读取i的值，加1，再写回主存，那就是3步操作了。
         * 所以上面的举例中，最后的值可能出现多种情况，就是因为满足不了原子性。
         * 2.可见性
         *  当一个变量被volatile修饰时，那么对它的修改会立刻刷新到主存，
         *  当其它线程需要读取该变量时，会去内存中读取新值。而普通变量则不能保证这一点。
         * 3.顺序性
         *JMM是允许编译器和处理器对指令重排序的，但是规定了as-if-serial语义，即不管怎么重排序，程序的执行结果不能改变。比如下面的程序段：
         * double pi = 3.14;    //A
         * double r = 1;        //B
         * double s= pi * r * r;//C
         * 复制代码上面的语句，可以按照A->B->C执行，结果为3.14,但是也可以按照B->A->C的顺序执行，因为A、B是两句独立的语句，而C则依赖于A、B，所以A、B可以重排序，但是C却不能排到A、B的前面。JMM保证了重排序不会影响到单线程的执行，但是在多线程中却容易出问题。
         *
         *
         * 第1条规则程序顺序规则是说在一个线程里，所有的操作都是按顺序的，但是在JMM里其实只要执行结果一样，是允许重排序的，
         * 这边的happens-before强调的重点也是单线程执行结果的正确性，但是无法保证多线程也是如此。
         *
         * volatile不保证原子性
         *
         * 有人可能会说volatile不是保证了可见性啊，一个线程对inc的修改，另外一个线程应该立刻看到啊！可是这里的操作inc++是个复合操作啊，包括读取inc的值，对其自增，然后再写回主存。
         * 假设线程A，读取了inc的值为10，这时候被阻塞了，因为没有对变量进行修改，触发不了volatile规则。
         * 线程B此时也读读inc的值，主存里inc的值依旧为10，做自增，然后立刻就被写回主存了，为11。
         * 此时又轮到线程A执行，由于工作内存里保存的是10，所以继续做自增，再写回主存，11又被写了一遍。所以虽然两个线程执行了两次increase()，结果却只加了一次。
         *
         *
         */
    }

    int a = 0;
    boolean flag = false;

    public void write() {
        a = 2;              //1
        flag = true;        //2
    }

    /**假设前提：并不考虑，线程2先执行multily的情况
     * (线程1先执行write，随后线程2再执行multiply，最后ret的值一定是4吗)
     *
     * 如图所示，write方法里的1和2做了重排序，线程1先对flag赋值为true，
     * 随后执行到线程2，ret直接计算出结果，再到线程1，这时候a才赋值为2,很明显迟了一步。
     *
     * 这段代码不仅仅受到重排序的困扰，即使1、2没有重排序。3也不会那么顺利的执行的。假设还是线程1先执行write操作，线程2再执行multiply操作，由于线程1是在工作内存里把flag赋值为1，不一定立刻写回主存，所以线程2执行时，multiply再从主存读flag值，仍然可能为false，那么括号里的语句将不会执行。
     *
     */

    public void multiply() {
        if (flag) {         //3 volatile 关键子能保证，在读flag的时候，
            int ret = a * a;//4
        }

    }

    /**
     * 那么线程1先执行write,线程2再执行multiply。根据happens-before原则，这个过程会满足以下3类规则：
     *
     * 程序顺序规则：1 happens-before 2; 3 happens-before 4; (volatile限制了指令重排序，所以1 在2 之前执行)
     * （1 happens-before 2 意思是1 比2先执行）
     * volatile规则：2 happens-before 3
     * 传递性规则：1 happens-before 4
     *
     * 当写一个volatile变量时，JMM会把该线程对应的本地内存中的共享变量刷新到主内存
     *
     * 当读一个volatile变量时，JMM会把该线程对应的本地内存置为无效，线程接下来将从主内存中读取共享变量。
     *
     */

    public void test05(){
        /**
         * volatile关键字的代码会多出一个lock前缀指令。
         * lock前缀指令实际相当于一个内存屏障，内存屏障提供了以下功能：
         *
         * 1 . 重排序时不能把后面的指令重排序到内存屏障之前的位置
         * 2 . 使得本CPU的Cache写入内存
         * 3 . 写入动作也会引起别的CPU或者别的内核无效化其Cache，相当于让新写入的值对别的线程可见。
         *
         */
    }

    /**
     * 有没使用到：
     * 懒汉单例，防止初始化指令重排
     *memory = allocate();　　// 1：分配对象的内存空间
     * ctorInstance(memory);　// 2：初始化对象
     * instance = memory;　　// 3：设置instance指向刚分配的内存地址
     *
     * 重排：
     * memory = allocate();　　// 1：分配对象的内存空间
     * instance = memory;　　// 3：设置instance指向刚分配的内存地址
     * // 注意，此时对象还没有被初始化！
     * ctorInstance(memory);　// 2：初始化对象
     *
     * B线程会看到一个还没有被初始化的对象。
     *
     */

    /**
     * 进阶
     * https://blog.csdn.net/dd864140130/article/details/56494925
     */

    /**
     * 没用volatile 前，A线程修改变量a，因为还没写入主存，这时B读取还是老值，
     * 用了volatile 后，A线程修改a，就算还没写入主存，这时候B读取就是新值
     * 当一个变量被volatile修饰时，那么对它的修改会立刻刷新到主存（主要是省略了写入缓存的那一步）
     */

}
