package com.patinousward.demo.reactor;

import org.junit.Test;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class ReactorSourceReading {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * 这两个案例是最简单的，只是将数组用一个死循环打出,而且都在主线程中，没有设计异步概念
     */

    @Test
    public void test01() {
        Flux.just(1, 2, 3, 4, 5)
                .subscribe(new CoreSubscriber<Integer>() {//这里传入CoreSubscriber对象作为订阅者
                    @Override
                    public void onSubscribe(Subscription s) {
                        log.info("onSubscribe, {}", s.getClass());
                        s.request(5);
                    }
                    @Override
                    public void onNext(Integer integer) {
                        log.info("onNext： {}", integer);
                    }
                    @Override
                    public void onError(Throwable t) {
                    }
                    @Override
                    public void onComplete() {
                        log.info("onComplete");
                    }
                });
    }

    @Test
    public void test02(){
            Flux.just(1, 2, 3, 4, 5)
                    .map(i -> i * i)
                    .subscribe(new CoreSubscriber<Integer>() {
                        @Override
                        public void onSubscribe(Subscription s) {
                            log.info("onSubscribe, {}", s.getClass());
                            s.request(5);
                        }
                        @Override
                        public void onNext(Integer integer) {
                            log.info("onNext： {}", integer);
                        }
                        @Override
                        public void onError(Throwable t) {
                        }
                        @Override
                        public void onComplete() {
                            log.info("onComplete");
                        }
                    });
        System.out.println("-----------");

    }


    @Test
    public void test03() throws InterruptedException {
        //这个test
        Flux<Long> haha = Flux.interval(Duration.ofSeconds(1)).doOnNext(x -> System.out.println("haha"));
        Thread.sleep(3000);
        haha.subscribe(System.out::println);

    }

}
