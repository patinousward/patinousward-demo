package com.patinousward.demo.reactor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaLog {
    private static final Logger LOGGER = LoggerFactory.getLogger(JavaLog.class);

    public static void info(String message){
        LOGGER.info(message);
    }

    public static void error(String error,Throwable e){
        LOGGER.error(error,e);
    }
}
