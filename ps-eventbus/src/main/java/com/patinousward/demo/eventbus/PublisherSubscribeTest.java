package com.patinousward.demo.eventbus;

public class PublisherSubscribeTest {
    public static void main(String[] args) {
        PaymentService paymentService=new PaymentService();
        UserService userService=new UserService();

        OrderPage orderPage=new OrderPage();

        orderPage.eventBus.register(paymentService);
        orderPage.eventBus.register(userService);
    }
}
