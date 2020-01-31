package com.patinousward.demo.test;

import org.junit.Test;

public class Test0 {
    private boolean sdf;
    @Test
    public void test01(){
        System.out.println(true && get());
    }

    public boolean get(){
        return sdf;
    }
}
