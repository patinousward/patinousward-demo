package com.patinousward.demo.test.springcir;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class J {
    @Autowired
    private K k;

    public K getK() {
        return k;
    }
}
