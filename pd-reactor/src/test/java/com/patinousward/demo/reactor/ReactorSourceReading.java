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

    @Test
    public void testDoOnNext() {
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

}
