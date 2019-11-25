package com.patinousward.demo.scalafunction

import org.junit.Test

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
    val reduce2: A = a.reduce(_ combine _)//返回得出的是数组中的类型
    print(reduce)//A(93)
  }
  @Test//factorial 阶乘 斐波那契数
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

}
