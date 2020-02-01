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

    @Test//使用property + ref 进行注入
    public void test03(){//<property ref = "e"> 需要set方法 set注入并不会有循环依赖的问题
        //没有set方法，注入的时候会报错
        ClassPathXmlApplicationContext app= new ClassPathXmlApplicationContext("spring-test03.xml");
        final Object a = app.getBean("D");
        D realy = (D)a;
        System.out.println(realy.getE());//非null
    }

    @Test//使用autowire byName进行注入
    //byName的话，D中的属性名需要和bean中name属性一样，或者和id属性一样，不一样的话，还是null
    //同时还是需要set方法，但是这里没有的话不会报错，只会返回null
    //没有循环依赖的问题
    public void test04(){
        ClassPathXmlApplicationContext app= new ClassPathXmlApplicationContext("spring-test04.xml");
        final Object a = app.getBean("D");
        D realy = (D)a;
        System.out.println(realy.getE());
    }


    @Test//使用autowire byType进行注入
    //byType的话，不用指定name必须和D中的属性一致，但是如果有2个类型，会报错
    //同时还是需要set方法，但是这里没有的话不会报错，只会返回null
    //没有循环依赖的问题
    public void test05(){
        ClassPathXmlApplicationContext app= new ClassPathXmlApplicationContext("spring-test05.xml");
        final Object a = app.getBean("D");
        D realy = (D)a;
        System.out.println(realy.getE());
    }

    @Test//@Autowired方法：https://www.cnblogs.com/leiOOlei/p/3713989.html
    //有2种用法，一种是不适用compoment  xml种配置bean，然后使用autowire注解
    //另外一种是直接使用compoment和autowire的注解，这时候xml中不需要配置bean
    public void test06(){
        //之类展示的是用法1 需要开启
        ClassPathXmlApplicationContext app= new ClassPathXmlApplicationContext("spring-test06.xml");
        final Object a = app.getBean("G");
        G realy = (G)a;
        System.out.println(realy.getH());//不为null
    }

    @Test//这里展示Autowire的第二种用法 不需要在xml中配置bean了
    public void test07(){
        //之类展示的是用法1
        ClassPathXmlApplicationContext app= new ClassPathXmlApplicationContext("spring-test07.xml");
        final Object a = app.getBean("j");//这里需要小写，因为@Component注册后name默认是类名的小写
        J realy = (J)a;
        System.out.println(realy.getK());//不为null
    }
    @Test
    public void test072(){
        //但是autowire 是按照类型进行注入的
        ClassPathXmlApplicationContext app= new ClassPathXmlApplicationContext("spring-test07.xml");
        final Object a = app.getBean("j");//这里需要小写，因为@Component注册后name默认是类名的小写
        J realy = (J)a;
        System.out.println(realy.getK().getClass());
        //这个案例的假设是：ParentK  有2个子类K 和subK ,他们都标有@component ，J中@AutoWire 的属性是ParentK parentK
        //这时候会报错:expected single matching bean but found 2: k,subK，除非使用@qualifier和@Autowire叠加
        //但是如果把属性名parentK 修改为k 或者subK 那么就会注入相对应的bean 多个bean没啥问题
    }

}
