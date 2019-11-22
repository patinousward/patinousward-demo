package com.patinousward.demo.eventbus;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;

public class OrderPage {
    public static EventBus eventBus = new EventBus();

    public void submitOrder() {
        Integer userId = 1;
        BigDecimal amount = BigDecimal.TEN;

        eventBus.post(new PayEvent(userId, amount));
    }
}
