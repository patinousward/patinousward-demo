package com.patinousward.interview;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * CAS算法
 * https://note.youdao.com/yws/api/personal/file/A20767FA292E40ACAB795875AC03FA77?method=download&shareKey=f6b769572d6156b923e0d2f576abfff7
 */
public class Interview06 {
    /**
     * 1。CAS算法
     */
    public void test01() {
        /**
         *CAS具体包括三个参数：当前内存值V、旧的预期值A、即将更新的值B，当且仅当预期值A和内存值V相同时，将内存值修改为B并返回true，否则什么都不做，并返回false。
         */

        AtomicInteger atomicInteger = new AtomicInteger(-1);
        int i = atomicInteger.incrementAndGet();//i=0
        /**
         * private static final Unsafe unsafe = Unsafe.getUnsafe();
         * private volatile int value;
         * private static final long valueOffset;
         *
         * unsafe.getAndAddInt(this, valueOffset, 1) + 1;
         *
         * var1:AtomicInteger对象
         * var2：valueOffset
         * var4：1
         * var5应该是当前计算机中的真实值
         *
         * public final int getAndAddInt(Object var1, long var2, int var4) {
         *         int var5;
         *         do {
         *         //var5应该是当前计算机中的真实值
         *             var5 = this.getIntVolatile(var1, var2);
         *         } while(!this.compareAndSwapInt(var1, var2, var5, var5 + var4));
         *
         *         return var5;
         *     }
         *
         *
         */
    }

    public void test02() {
        /**
         * inline jint     Atomic::cmpxchg    (jint     exchange_value, volatile jint*     dest, jint     compare_value, cmpxchg_memory_order order) {
         *   int mp = os::is_MP();
         *   __asm__ volatile (LOCK_IF_MP(%4) "cmpxchgl %1,(%3)"
         *                     : "=a" (exchange_value)
         *                     : "r" (exchange_value), "a" (compare_value), "r" (dest), "r" (mp)
         *                     : "cc", "memory");
         *   return exchange_value;
         * }
         *
         */

        /**
         * compareAndSwapInt
         *
         * __asm__ 的意思是这个是一段内嵌汇编代码。也就是在 C 语言中使用汇编代码。
         * 这里的 volatile和 JAVA 有一点类似，但不是为了内存的可见性，而是告诉编译器对访问该变量的代码就不再进行优化。
         * LOCK_IF_MP(%4) 的意思就比较简单，就是如果操作系统是多线程的，那就增加一个 LOCK。
         * cmpxchgl 就是汇编版的“比较并交换”。但是我们知道比较并交换，有三个步骤，不是原子的。所以在多核情况下加一个 LOCK，由CPU硬件保证他的原子性。
         * 我们再看看 LOCK 是怎么实现的呢？我们去Intel的官网上看看，可以知道LOCK在的早期实现是直接将 cup 的总线阻塞，这样的实现可见效率是很低下的。
         * 后来优化为X86 cpu 有锁定一个特定内存地址的能力，当这个特定内存地址被锁定后，它就可以阻止其他的系统总线读取或修改这个内存地址。
         *
         */
    }


    /**
     * 存在问题
     */
    public void test03() {
        /**
         * CAS虽然很高效的解决了原子操作问题，但是CAS仍然存在三大问题。
         *
         * 【1】循环时间长、开销很大。
         *
         * 当某一方法比如：getAndAddInt执行时，如果CAS失败，会一直进行尝试。如果CAS长时间尝试但是一直不成功，可能会给CPU带来很大的开销。
         * 【2】只能保证一个共享变量的原子操作。
         *
         * 当操作1个共享变量时，我们可以使用循环CAS的方式来保证原子操作，但是操作多个共享变量时，循环CAS就无法保证操作的原子性，这个时候就需要用锁来保证原子性。
         * 【3】存在ABA问题
         *
         * 引入版本概念：AtomicStampedReference 邮戳
         */
        AtomicStampedReference<Integer> atomicStampedRef =
                new AtomicStampedReference<>(1, 0);
        Thread main = new Thread(() -> {
            System.out.println("操作线程" + Thread.currentThread() + ",初始值 a = " + atomicStampedRef.getReference());
            int stamp = atomicStampedRef.getStamp(); //获取当前标识别
            try {
                Thread.sleep(1000); //等待1秒 ，以便让干扰线程执行
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            boolean isCASSuccess = atomicStampedRef.compareAndSet(1, 2, stamp, stamp + 1);  //此时expectedReference未发生改变，但是stamp已经被修改了,所以CAS失败
            System.out.println("操作线程" + Thread.currentThread() + ",CAS操作结果: " + isCASSuccess);
        }, "主操作线程");

        Thread other = new Thread(() -> {
            Thread.yield(); // 确保thread-main 优先执行
            atomicStampedRef.compareAndSet(1, 2, atomicStampedRef.getStamp(), atomicStampedRef.getStamp() + 1);
            System.out.println("操作线程" + Thread.currentThread() + ",【increment】 ,值 = " + atomicStampedRef.getReference());
            atomicStampedRef.compareAndSet(2, 1, atomicStampedRef.getStamp(), atomicStampedRef.getStamp() + 1);
            System.out.println("操作线程" + Thread.currentThread() + ",【decrement】 ,值 = " + atomicStampedRef.getReference());
        }, "干扰线程");

        main.start();
        other.start();
        //输出操作结果False
    }


    public void test04() {
        AtomicReference<Thread> stringAtomicReference = new AtomicReference<>();//默认值为null

        //1.自旋锁（要sleep下吧。。）
/*        public class SpinLock {

            private AtomicReference<Thread> sign =new AtomicReference<>();

            public void lock(){
                Thread current = Thread.currentThread();
                while(!sign .compareAndSet(null, current)){
                }
            }

            public void unlock (){
                Thread current = Thread.currentThread();
                sign .compareAndSet(current, null);
            }
        }*/
        // 2.AtomicInteger 的 incrementAndGet()
        //3/令牌桶限流

    }
}

class RateLimiter {

    private final long rateToMsConversion;

    private final AtomicInteger consumedTokens = new AtomicInteger();
    private final AtomicLong lastRefillTime = new AtomicLong(0);

    @Deprecated
    public RateLimiter() {
        this(TimeUnit.SECONDS);
    }

    public RateLimiter(TimeUnit averageRateUnit) {
        switch (averageRateUnit) {
            case SECONDS:
                rateToMsConversion = 1000;
                break;
            case MINUTES:
                rateToMsConversion = 60 * 1000;
                break;
            default:
                throw new IllegalArgumentException("TimeUnit of " + averageRateUnit + " is not supported");
        }
    }

    //提供给外界获取 token 的方法
    public boolean acquire(int burstSize, long averageRate) {
        return acquire(burstSize, averageRate, System.currentTimeMillis());
    }

    public boolean acquire(int burstSize, long averageRate, long currentTimeMillis) {
        if (burstSize <= 0 || averageRate <= 0) { // Instead of throwing exception, we just let all the traffic go
            return true;
        }

        //添加token
        refillToken(burstSize, averageRate, currentTimeMillis);

        //消费token
        return consumeToken(burstSize);
    }

    private void refillToken(int burstSize, long averageRate, long currentTimeMillis) {
        long refillTime = lastRefillTime.get();
        long timeDelta = currentTimeMillis - refillTime;

        //根据频率计算需要增加多少 token
        long newTokens = timeDelta * averageRate / rateToMsConversion;
        if (newTokens > 0) {
            long newRefillTime = refillTime == 0
                    ? currentTimeMillis
                    : refillTime + newTokens * rateToMsConversion / averageRate;

            // CAS 保证有且仅有一个线程进入填充
            if (lastRefillTime.compareAndSet(refillTime, newRefillTime)) {
                while (true) {
                    int currentLevel = consumedTokens.get();
                    int adjustedLevel = Math.min(currentLevel, burstSize); // In case burstSize decreased
                    int newLevel = (int) Math.max(0, adjustedLevel - newTokens);
                    // while true 直到更新成功为止
                    if (consumedTokens.compareAndSet(currentLevel, newLevel)) {
                        return;
                    }
                }
            }
        }
    }

    private boolean consumeToken(int burstSize) {
        while (true) {
            int currentLevel = consumedTokens.get();
            if (currentLevel >= burstSize) {
                return false;
            }

            // while true 直到没有token 或者 获取到为止
            if (consumedTokens.compareAndSet(currentLevel, currentLevel + 1)) {
                return true;
            }
        }
    }

    public void reset() {
        consumedTokens.set(0);
        lastRefillTime.set(0);
    }
}

