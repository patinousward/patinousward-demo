package com.patinousward.demo.scalafunction

import org.junit.Test

import scala.annotation.tailrec
import scala.runtime.Nothing$

/**
  * Created by Administrator on 2019/11/14.
  */
class FunctionTest01 {

  @Test
  def test00(): Unit ={
  }

  @Test//List.fill()
  def test01(): Unit ={
    val fill: (Nothing) => List[Nothing] = List.fill(10)
    val fill1: List[String] = List.fill(10)("aa")
    print(fill1)//List(aa, aa, aa, aa, aa, aa, aa, aa, aa, aa)
  }
  @Test//groupby
  def test02(): Unit ={
    case class Person(depart:String,name:String)
    val a = List(Person("A部门","zhangsan"),Person("B部门","lisi"),Person("B部门","wangwu"))
    val by: Map[String, List[Person]] = a.groupBy(_.depart)//map的key是部门，类型是部门类型，value则是同一部门的Person的集合
    // TODO: 另一种写法
    a.groupBy(_ depart)

  }

  @Test//zip unzip
  def test03(): Unit ={
    case class A(name:String)
    case class B(name:String)
    val a = List(A("a"),A("b"))
    val b = List(B("a"),B("b"))
    val zip: List[(A, B)] = a.zip(b)
    val unzip: (List[A], List[B]) = zip.unzip
  }

  @Test//reduce
  def test04(): Unit ={
    case class A(age:Int){
      def combine(a:A):A=A(this.age + a.age)
    }
    val a = List(A(18),A(16),A(24),A(35))
    val reduce: A = a.reduce((a1,a2)=>a1.combine(a2))//返回得出的是数组中的类型
    // TODO:  另一种写法
    val reduce2: A = a.reduce(_ combine _)//返回得出的是数组中的类型
    print(reduce)//A(93)
  }
  @Test//练习2.1factorial 阶乘 斐波那契数(answer)
  def test05():Unit={
    def fib(n: Int): Int = {
      @annotation.tailrec
      def loop(n: Int, prev: Int, cur: Int): Int =
        if (n == 0) prev
        else loop(n - 1, cur, prev + cur)
      loop(n, 0, 1)
    }
    print(fib(6))
  }

  @Test//练习2.2
  def test06(): Unit ={
    def isSorted[A](as:Array[A],ordered:(A,A)=>Boolean):Boolean={
      @tailrec
      def loop(n:Int):Boolean ={
        if(n >=as.length -1) true
        else if(!ordered(as(n),as(n+1))) false
        else loop(n +1)
      }
      loop(0)
    }

    print(isSorted(Array(1,2,3,8,5),(a1:Int,a2:Int)=>a1< a2))
  }

  @Test
  def test07(): Unit ={
    def partiall[A,B,C](a:A,f:(A,B)=>C):B=>C={
      b:B =>f(a,b)
        // TODO:
      //e:B =>f(a,e)  小b只是B传入参数的一种代表，a变量固定死了，B是可以任意的
        //分析这个方法的入参可以知道，传入的只有一个参数a和函数f，通过分析这个方法的返回值知道
        //返回值是一个传入B类型，返回C的函数，就是说，这个返回函数调用的时候，必然会传入参数B，
        //所以这个方法能使用的参数其实有2个，一个是a，这个不能修改，一个是返回函数以后会调用的B，用b代表这个参数，当然也可以使用别的代替
    }

    def cc(a:Int,f:(Int,String)=>Boolean):String=>Boolean ={
      b:String =>f(a,b)
    }

    val stringToBoolean = cc(100,(a:Int,b:String)=>b.toInt == a)
    print(stringToBoolean("100"))
  }

  @Test//练习2.3 柯里化
  def test08(): Unit ={
    def curry[A,B,C](f:(A,B)=>C):A =>(B =>C)={
      a:A => b:B =>f(a,b)
    }
    // a代表返回函数将来调用时的入参数，b代表返回函数的返回函数将来调用时的入参,但是这个b参数只能在a的返回参数中调用
    //就是说，要在a:A => 后面才能使用到这个参数，否则变量的范围是有问题的
  }
  @Test//练习2.4  反柯里化
  def test09(): Unit ={
    def uncurry[A,B,C](f:A=>B=>C):(A,B)=>C={
      (a:A,b:B) =>f(a)(b)
        //f(a)  返回B =>C  这时候再使用()，传入b进行调用即可
    }
  }

  @Test//练习2.5
  def test10(): Unit ={
    def compose[A,B,C](f:B=>C,g:A=>B):A =>C ={
      a:A =>f(g(a))
    }
  }

  @Test//练习3.1 过于简单，忽略
  def test11(): Unit ={

    //sealed 不能在类定义的文件之外定义任何新的子类
    //Nothing是所有类的子类，是一个类。Nothing没有对象，但是可以用来定义类型。
    //case object Nil extends List[Nothin]  又因为nothing是所有类的子类，所以Nil extends List[所有类型]
    //val ex1:List[Double] = Nil  因为Nil是Nothing的子类，Nothing 是Double的子类，而且有协变存在，父类引用指向子类的实体 是ok的

  }

}
