package com.patinousward.demo.reactor;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;


public class ReactorTest03 {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

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

    @Test
    public void test05() throws InterruptedException {
        final Flux<Integer> flux = Flux.<Integer> create(fluxSink -> {
            //NOTE sink:class reactor.core.publisher.FluxCreate$SerializedSink
            LOGGER.info("sink:{}",fluxSink.getClass());
            while (true) {
                LOGGER.info("sink next");
                fluxSink.next(ThreadLocalRandom.current().nextInt(10));
            }
        }, FluxSink.OverflowStrategy.BUFFER);

        //NOTE flux:class reactor.core.publisher.FluxCreate,prefetch:-1
        LOGGER.info("flux:{},prefetch:{}",flux.getClass(),flux.getPrefetch());

        flux.subscribe(e -> {
            LOGGER.info("subscribe:{}",e);
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        });

        TimeUnit.MINUTES.sleep(20);
    }


    @Test
    public void test06() throws InterruptedException {
        FluxSinkListener fluxSinkListener = new FluxSinkListener<String>(){
            private FluxSink<String> fluxSink = null;
            @Override
            public void setFluxSink(FluxSink<String> fluxSink) {
                this.fluxSink = fluxSink;
            }
            @Override
            public void next(String webSocketMessage) {
                if(fluxSink != null) fluxSink.next(webSocketMessage);

            }
            @Override
            public void complete() {
                if(fluxSink != null) fluxSink.complete();
            }
        };

        Flux<String> receives = Flux.create(sink -> {
            fluxSinkListener.setFluxSink(sink);
        });
        //先订阅,否则listener中的fluxSink为null
        receives.subscribe(Utils::println);
        while (true){
            fluxSinkListener.next("haha");
            Thread.sleep(1000);
        }

    }

}


interface FluxSinkListener<T> {
    void setFluxSink(FluxSink<T> fluxSink);
    void next(T t);
    void complete();
}
