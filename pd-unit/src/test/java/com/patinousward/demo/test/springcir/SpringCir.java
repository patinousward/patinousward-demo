package com.patinousward.demo.test.springcir;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * spring的循环依赖测试
 */
public class SpringCir {

    @Test//构造器循环依赖问题 spring无法解决,filed属性循环依赖，或者说setting方式的循环依赖，则通过三级缓存解决
    public void test01(){
        ClassPathXmlApplicationContext app= new ClassPathXmlApplicationContext("spring-test01.xml");
        final Object a = app.getBean("A");
        A realy = (A)a;
        System.out.println(realy);
    }
    @Test//depend-on标签的循环依赖
    public void test02(){
        ClassPathXmlApplicationContext app= new ClassPathXmlApplicationContext("spring-test02.xml");
        final Object a = app.getBean("D");
        D realy = (D)a;
        System.out.println(realy.getE());//null
    }

}
