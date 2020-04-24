package com.patinousward.interview;

/**
 * ThradLocal
 */
public class Interview02 {
    /**
     * 1.Api与数据结构
     */
    public void test01() {
        ThreadLocal<String> localName = new ThreadLocal();
        localName.set("占小狼");
        String name = localName.get();
        /**
         * public void set(T value) {
         *         Thread t = Thread.currentThread();
         *         ThreadLocalMap map = getMap(t);
         *         if (map != null)
         *             map.set(this, value);
         *         else
         *             createMap(t, value);
         *     }
         *
         *     ThreadLocal.ThreadLocalMap threadLocals = null; Thread中含有ThreadLocal.ThreadLocalMap 这个属性，key是new ThreadLocal()
         *     对象，说明一个线程中可能有多个ThreadLocal 对象组成一个ThreadLocal.ThreadLocalMap
         */

        /**
         * Thread t = Thread.currentThread();
         *         ThreadLocalMap map = getMap(t);
         *         if (map != null) {
         *             ThreadLocalMap.Entry e = map.getEntry(this);
         *             if (e != null) {
         *                 @SuppressWarnings("unchecked")
         *                 T result = (T)e.value;
         *                 return result;
         *             }
         *         }
         *         return setInitialValue();
         */
    }

    /**
     * 2.数据结构哦
     */
    public void test02() {
        //在ThreadLoalMap中，也是初始化一个大小16的Entry数组，Entry对象用来保存每一个key-value键值对
        //key是ThreadLocal对象
        //这里需要注意的是，ThreadLoalMap的Entry是继承WeakReference，和HashMap很大的区别是，Entry中没有next字段，所以就不存在链表的情况了。
        //private static class Entry<K,V> extends WeakReference<Object> implements Map.Entry<K,V> {
        //上面Entry是WeakHashMap 中的内部类
        //和HashMap很大的区别是，Entry中没有next字段，所以就不存在链表的情况了。
    }

    /**
     * 3.hash冲突
     */
    public void test03() {
        /**
         * 在插入过程中，根据ThreadLocal对象的hash值，定位到table中的位置i，过程如下：
         * 1、如果当前位置是空的，那么正好，就初始化一个Entry对象放在位置i上；
         * 2、不巧，位置i已经有Entry对象了，如果这个Entry对象的key正好是即将设置的key，那么重新设置Entry中的value；
         * 3、很不巧，位置i的Entry对象，和即将设置的key没关系，那么只能找下一个空位置；
         *
         *
         * 这样的话，在get的时候，也会根据ThreadLocal对象的hash值，定位到table中的位置，然后判断该位置Entry对象中的key是否和get的key一致，如果不一致，就判断下一个位置
         *
         * 下一个位置是指？：((i + 1 < len) ? i + 1 : 0); 下一个位置指的就是i + 1的index，是个for循环，如果下个位置还冲突，继续下一个
         * 越界返回0，扩容呢？目前0没看到有扩容
         */
    }

    /**
     * 4.内存泄漏
     */
    public void test04(){
        /**
         * 这就导致了一个问题，ThreadLocal在没有外部强引用时，发生GC时会被回收，
         * 如果创建ThreadLocal的线程一直持续运行，那么这个Entry对象中的value就有可能一直得不到回收，发生内存泄露。
         * 使用完ThreadLocal之后，记得调用remove方法。(Aop中的后置方法)
         * https://juejin.im/post/5aeeb3e8518825672f19c52c
         * 主要看下这篇文章
         */
    }
}
