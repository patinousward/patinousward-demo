package com.patinousward.demo.reactor;

public class Utils extends JavaLog {

    public static void println(Object message){
        info(Thread.currentThread().getName() + "\n" + String.valueOf(message));
    }

}
