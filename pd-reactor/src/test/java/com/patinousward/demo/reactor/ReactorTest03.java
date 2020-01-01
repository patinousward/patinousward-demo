package com.patinousward.demo.reactor;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;


public class ReactorTest03 {

    @Test
    public void test01(){
        Flux.generate(()->1,(value,sink)->{
            if(value == 5){
                sink.complete();
            }else{
                sink.next("the value is " + value);  //这里相当于int转了string
            }
            return value + 1;
        }).subscribe(Utils::println);
    }

    @Test
    /**单线程异步执行  这里输出同一的线程名
     *
     */
    public void test02() throws InterruptedException {
        //当前线程执行 main（非异步）
        //Flux.range(0,10).publishOn(Schedulers.immediate()).subscribe(Utils::println);
        //单线程不是main线程，是异步的某个线程
        Flux.range(0,10).publishOn(Schedulers.single()).subscribe(Utils::println);
        //强制让主线程执行完毕,main线程阻塞直到副线程执行完毕
        Thread.currentThread().join(1 * 1000L);
    }
    @Test
    /**
     * 弹性线程池执行  这里输出还是同一的线程名，线程数为无限
     * @throws InterruptedException
     */
    public void test03() throws InterruptedException {
        Flux.range(0,10).publishOn(Schedulers.elastic()).subscribe(Utils::println);
        Thread.currentThread().join(1 * 1000L);
    }

    @Test
    /**
     * 并行线程池执行 这里输出还是同一的线程名
     * 并行数为Runtime.getRuntime()
     * 			       .availableProcessors()
     * 			       当前节点cpu的核数
     *
     * 			       和ForkJoinPook 中的是同一个东西
     * 			       并行（2个cpu核处理2个task）和并发（1个cpu核处理多个task）
     * @throws InterruptedException
     */
    public void test04() throws InterruptedException {
        Flux.range(0,10).publishOn(Schedulers.parallel()).subscribe(Utils::println);
        Thread.currentThread().join();
    }

}
