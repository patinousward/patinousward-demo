package com.patinousward.interview;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Interview01 {
    /**
     * 1.几种线程池及使用场景
     * newFixedThreadPool (固定数目线程的线程池)
     * newCachedThreadPool(可缓存线程的线程池)
     * newSingleThreadExecutor(单线程的线程池)
     * newScheduledThreadPool(定时及周期执行的线程池)
     */
    public void test01() {
        //1.种类  ThreadPoolExecutor 是ExecutorService 的子类
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        /**
         * return new ThreadPoolExecutor(0, Integer.MAX_VALUE,
         *                                       60L, TimeUnit.SECONDS,
         *                                       new SynchronousQueue<Runnable>());
         */
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);
        /**
         * return new ThreadPoolExecutor(nThreads, nThreads,
         *                                       0L, TimeUnit.MILLISECONDS,
         *                                       new LinkedBlockingQueue<Runnable>());
         */
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        /**
         * return new FinalizableDelegatedExecutorService
         *             (new ThreadPoolExecutor(1, 1,
         *                                     0L, TimeUnit.MILLISECONDS,
         *                                     new LinkedBlockingQueue<Runnable>()));
         */
        //ScheduledExecutorService 是ExecutorService 的子类
        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(10);
        /**
         * return new ScheduledThreadPoolExecutor(corePoolSize);
         * super(corePoolSize, Integer.MAX_VALUE, 0, NANOSECONDS,
         *               new DelayedWorkQueue());
         *
         * scheduleAtFixedRate方法:
         * 1.若任务耗时超过周期间隔，则需要等待上个任务完成下个任务才能执行
         * 2.若任务耗时小于周期间隔，则下个任务按周期间隔执行任务
         * scheduleWithFixedDelay方法:
         * 1.下任务等到上个任务执行完成+周期间隔之后才执行任务
         *
         */

        //2.使用场景
        /**
         * 使用的是自定义的线程池，因为需要自定义参数，核心模块scheduler，consumerManager从队列中poll出事件进行消费
         *
         * core 和max 一样 ，使用new LinkedBlockingQueue[Runnable](10 * threadNum 超时时间120s
         *
         * allowCoreThreadTimeOut(true)//连核心线程都会回收，同样达到keepAliveTime后
         * https://segmentfault.com/a/1190000004989413
         * https://blog.csdn.net/qq_24082175/article/details/81019490
         *
         * keepAliveTime when the number of threads is greater than
         *      *        the core, this is the maximum time that excess idle threads
         *      *        will wait for new tasks before terminating.
         *      0的时候直接退出：https://www.cnblogs.com/zxporz/p/10940251.html
         * new ThreadFactory {  用来显示线程名和
         *       val num = new AtomicInteger(0)
         *       override def newThread(r: Runnable): Thread = {
         *         val t = new Thread(r)
         *         t.setDaemon(isDaemon)
         *         t.setName(threadName + num.incrementAndGet())
         *         t
         *       }
         *     }
         *
         *
         * val scheduler = new ScheduledThreadPoolExecutor(20, threadFactory("BDP-Default-Scheduler-Thread-", true))
         *     scheduler.setMaximumPoolSize(20)
         *     scheduler.setKeepAliveTime(5, TimeUnit.MINUTES)
         *     scheduler）
         *
         *
         * scheduleAtFixedRate（定时任务），比如清除缓存，服务之间的心跳，预热（从注册中心拉取服务信息集合
         *
         *
         *
         */
    }

    /**
     * 2.各个参数之间的意义
     */
    public void test02() {
        /**
         * corePoolSize： 线程池核心线程数最大值
         * maximumPoolSize： 线程池最大线程数大小
         * keepAliveTime： 线程池中非核心线程空闲的存活时间大小
         * unit： 线程空闲存活时间单位
         * workQueue： 存放任务的阻塞队列
         * threadFactory： 用于设置创建线程的工厂，可以给创建的线程设置有意义的名字，可方便排查问题。
         * handler：  线城池的饱和策略事件，主要有四种类型。
         *
         */
    }

    /**
     * 3.执行流程
     */
    public void test03() {
        /**
         * 提交一个任务，线程池里存活的核心线程数小于线程数corePoolSize时，线程池会创建一个核心线程去处理提交的任务。
         * 如果线程池核心线程数已满，即线程数已经等于corePoolSize，一个新提交的任务，会被放进任务队列workQueue排队等待执行。
         * 当线程池里面存活的线程数已经等于corePoolSize了,并且任务队列workQueue也满，判断线程数是否达到maximumPoolSize，即最大线程数是否已满，如果没到达，创建一个非核心线程执行提交的任务。
         * 如果当前的线程数达到了maximumPoolSize，还有新的任务过来的话，直接采用拒绝策略处理。
         *
         */
    }

    /**
     * 4.拒绝策略
     */
    public void test04() {
        /**
         * AbortPolicy(抛出一个异常，默认的)
         * DiscardPolicy(直接丢弃任务)
         * DiscardOldestPolicy（丢弃队列里最老的任务，将当前这个任务继续提交给线程池）
         * CallerRunsPolicy（交给线程池调用所在的线程进行处理)
         */
    }

    /**
     * 5.异常处理
     */
    @Test
    public void test05() throws InterruptedException {
        /**
         * 5.1 try catch
         * 5.2 futrue.get 阻塞（如果有异常，这个方法会抛出异常。比如超时，执行异常等等，所以需要trycatch）
         */
        //5.3  setUncaughtExceptionHandler  自定义threadFactory
        ExecutorService threadPool = Executors.newFixedThreadPool(1, r -> {
            Thread t = new Thread(r);
            t.setUncaughtExceptionHandler(
                    (t1, e) -> {
                        System.out.println(t1.getName() + "线程抛出的异常" + e);
                    });
            return t;
        });
        threadPool.execute(() -> {
            Object object = null;
            System.out.print("result## " + object.toString());
        });
        Thread.sleep(1000);//主要为了看打印效果

        //5.4 重写ThreadPoolExecutor的afterExecute方法，处理传递的异常引用(原本是空方法，留给子类实现)
        /**
         * class ExtendedExecutor extends ThreadPoolExecutor {
         *     // 这可是jdk文档里面给的例子。。
         *     protected void afterExecute(Runnable r, Throwable t) {
         *         super.afterExecute(r, t);
         *         if (t == null && r instanceof Future<?>) {
         *             try {
         *                 Object result = ((Future<?>) r).get();
         *             } catch (CancellationException ce) {
         *                 t = ce;
         *             } catch (ExecutionException ee) {
         *                 t = ee.getCause();
         *             } catch (InterruptedException ie) {
         *                 Thread.currentThread().interrupt(); // ignore/reset
         *             }
         *         }
         *         if (t != null)
         *             System.out.println(t);
         *     }
         * }}
         */
    }

    /**
     * 6.工作队列
     */
    public void test06() {
        /**
         * ArrayBlockingQueue（读写同一锁，数组）
         * ArrayBlockingQueue（有界队列）是一个用数组实现的有界阻塞队列，按FIFO排序量。
         * LinkedBlockingQueue（读锁和写锁分开，链表）
         * LinkedBlockingQueue（可设置容量队列）基于链表结构的阻塞队列，按FIFO排序任务，容量可以选择进行设置，不设置的话，将是一个无边界的阻塞队列，最大长度为Integer.MAX_VALUE，吞吐量通常要高于ArrayBlockingQuene；newFixedThreadPool线程池使用了这个队列
         * DelayQueue
         * DelayQueue（延迟队列）是一个任务定时周期的延迟执行的队列。根据指定的执行时间从小到大排序，否则根据插入到队列的先后排序。newScheduledThreadPool线程池使用了这个队列。
         * PriorityBlockingQueue
         * PriorityBlockingQueue（优先级队列）是具有优先级的无界阻塞队列；
         * SynchronousQueue（可能是因为不用放入数组或者链表存储，吞吐量毕竟大）
         * SynchronousQueue（同步队列）一个不存储元素的阻塞队列，每个插入操作必须等到另一个线程调用移除操作，否则插入操作一直处于阻塞状态，吞吐量通常要高于LinkedBlockingQuene，newCachedThreadPool线程池使用了这个队列。
         * 针对面试题：线程池都有哪几种工作队列？ 我觉得，回答以上几种ArrayBlockingQueue，LinkedBlockingQueue，SynchronousQueue等，说出它们的特点，并结合使用到对应队列的常用线程池(如newFixedThreadPool线程池使用LinkedBlockingQueue)，进行展开阐述， 就可以啦。
         *
         */

        //无界队列会导致内存飙升，maxpoolsize会导致

        /**
         *   FixedThreadPool 适用于处理CPU密集型的任务，确保CPU在长期被工作线程使用的情况下，尽可能的少的分配线程，即适用执行长期的任务。
         *   用于并发执行大量短期的小任务。（同步队列，cache）
         *   适用于串行执行任务的场景，一个任务一个任务地执行。 单线程线程池
         *   周期性执行任务的场景，需要限制线程数量的场景
         */

        /**
         * 如果是CPU密集型的任务，我们应该设置数目较小的线程数，比如CPU数目加1。如果是IO密集型的任务，则应该设置可能多的线程数，由于IO操作不占用CPU，所以，不能让CPU闲下来
         *
         * 好处是，如果设置0-threadNum 这种。。要先经过队列，因为corethreadsize是0 ，所以吞吐量不如。。
         *
         */
    }

    /**
     * 有界队列和无界队列
     * https://blog.csdn.net/u012240455/article/details/81844007
     */
    public void test07() {
        /**有界
         * ArrayBlockingQueue
         * LinkedBlockingQueue
         * SynchronousQueue
         * put take 操作都是阻塞的
         * offer poll 操作不是阻塞的，offer 队列满了会返回false不会阻塞，poll 队列为空时会返回null不会阻塞
         */
        /**
         *
         * ConcurrentLinkedQueue 无锁队列，底层使用CAS操作，通常具有较高吞吐量，但是具有读性能的不确定性，弱一致性——不存在如ArrayList等集合类的并发修改异常，通俗的说就是遍历时修改不会抛异常
         * PriorityBlockingQueue 具有优先级的阻塞队列
         * DelayedQueue 延时队列，使用场景
         * put 操作永远都不会阻塞，空间限制来源于系统资源的限制
         * 底层都使用CAS无锁编程
         *
         *
         */
    }
}
