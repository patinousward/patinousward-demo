package com.patinousward.demo.reactor;

import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;

public class Java9Flow {
    public static void main(String[] args) throws InterruptedException {
        //打印结果可看出线程是ForkJoinPool下的commonPool，这个也是根据cpu最大核数确定的
        try(SubmissionPublisher<String> publisher = new SubmissionPublisher<>()) {
            publisher.subscribe(new StringSubscriber("A"));
            publisher.subscribe(new StringSubscriber("B"));
            publisher.subscribe(new StringSubscriber("C"));
            publisher.submit("hello reactor");
        }
        //复线程会一直在跑，就算调用了complete，所以join如果不加时间，会一直阻塞
        Thread.currentThread().join(20 * 1000);
    }


    private static class StringSubscriber extends JavaLog implements Flow.Subscriber<String>{

        private String name;

        private Flow.Subscription subscription;


        public StringSubscriber(String name) {
            this.name = name;
        }

        @Override
        public void onSubscribe(Flow.Subscription subscription) {
            info(Thread.currentThread().getName() + "/" + name + " is onSubscribe");
            subscription.request(1); //直接走下面的next方法  这就叫背压？向服务器方向请求
            //背压  响应式编程是数据源推送给订阅者的，这里订阅者主动请求数据，就是反
            this.subscription = subscription;
        }

        @Override
        public void onNext(String s) {
            info(Thread.currentThread().getName() + "/" + s);
            subscription.cancel();//取消掉后。onComplete 和onError就不会调用了
        }

        @Override
        public void onError(Throwable throwable) {

        }

        @Override
        public void onComplete() {

        }
    }
}
