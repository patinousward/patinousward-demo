package com.patinousward.demo.reactor;

import org.junit.After;
import org.junit.Test;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import static java.lang.Thread.sleep;

public class ReactorTest01 {
        @After
    public void after() throws InterruptedException {
        sleep(30_000);
    }

    @Test
    public void testJust() {
        Flux.just("hello", "world")
                .subscribe(System.out::println);
    }

    @Test
    public void testList() {
        List<String> words = Arrays.asList(
                "hello",
                "reactive",
                "world"
        );

        Flux.fromIterable(words)
                .subscribe(System.out::println);
    }

    @Test
    public void testRange() {
        Flux.range(1, 10)
                .subscribe(System.out::println);
    }

    @Test
    public void testInterval() {
        Flux.interval(Duration.ofSeconds(1))
                .subscribe(System.out::println);
        System.out.println(555);//555会先打印出来，说明有异步
    }

    @Test
    public void testDoOnNext() {
            //doOnNext  在next方法前调用，每次next方法强
        Flux.interval(Duration.ofSeconds(1)).doOnNext(x-> System.out.println("haha"))
                .subscribe(System.out::println);
    }
}