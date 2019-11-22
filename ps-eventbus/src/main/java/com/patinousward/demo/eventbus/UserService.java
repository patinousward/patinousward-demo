package com.patinousward.demo.eventbus;

import org.greenrobot.eventbus.Subscribe;

public class UserService {
    public String getEmailAddress(Integer userId) {
        return "foo@bar.com";
    }

    @Subscribe
    public void registerPayment(PayEvent payEvent) {
        // Register payment in database...
    }
}
