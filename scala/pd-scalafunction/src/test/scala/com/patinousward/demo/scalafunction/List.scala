package scala.com.patinousward.demo.scalafunction



sealed trait List[+A]
case object Nil extends List[Nothing]
case class Cons[+A](head:A,tail:List[A]) extends List[A]

object List {
  def apply[A](as:A*): List[A] =
    if (as.isEmpty) Nil
    else Cons(as.head,apply(as.tail:_*))//这里的head和tail是可变形参自己的方法

  def product(ds:List[Double]):Double = ds match {
    case Nil => 1.0
    case Cons(0.0,_) =>0.0
    case Cons(x,xs) =>x * product(xs) //这里match能成功是因为ds本身传入参数是Cons..这种结构的
  }
  //练习3.2
  def tail[A](list:List[A]):List[A] ={
    list match {
      case Nil =>Nil
      case Cons(head,tail) =>tail
    }
  }
  //练习3.3
  def setHead[A](list:List[A],replace:A):List[A] ={
    list match {
      case Nil =>Nil
      case Cons(head,tail) =>Cons(replace,tail)
    }
  }
  //练习3.4
  def drop[A](list:List[A],n:Int):List[A] ={
    list match {
      case Nil =>Nil
      case Cons(head, tail) => if(n >= 0)drop(tail,n -1) else tail
    }
  }


}
