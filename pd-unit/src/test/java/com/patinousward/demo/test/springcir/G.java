package com.patinousward.demo.test.springcir;

import org.springframework.beans.factory.annotation.Autowired;

public class G {
    @Autowired
    private H h;

    public H getH() {
        return h;
    }
}
