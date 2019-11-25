package com.patinousward.demo.scalamacros

object Test extends App {
  @ToString class A(var name:String ="jftang",var age:Int = 100)
  class C(var name:String ="jftang",var age:Int = 100)
  println(new C)
  println(new A)
  val b = new B
  println(b)
  //@clean val aaa = "hello"
}