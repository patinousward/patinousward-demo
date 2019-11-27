package scala.com.patinousward.demo.scalafunction

import org.junit.Test

import scala.annotation.tailrec


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
  //练习3.5  （answer） 这个答案要求的是前面连续满足要求的元素
  def dropWhile[A](l:List[A],f:A =>Boolean):List[A]={
    l match {
      case Cons(h,t) if f(h) => dropWhile(t, f) //注意这种写法
      case _ => l
    }
  }
  //练习3.5 update版
  def dropWhile2[A](l:List[A],f:A =>Boolean):List[A]={
    l match {
      case Cons(h,t) =>if(f(h)) dropWhile2(t,f) else Cons(h,dropWhile2(t,f))
      case _ =>l
    }
  }
  //dropWhie:(as:List[A],f:A=>Boolean) =>List[A]    (A,B)=>C
  //dropWhile3:(as:List[A])=>f:A =>Boolean =>List[A]  A =>B =>C 柯里化
  //dropWhile3[A](as:List[A])  返回的是一个函数，入参是A =>Boolean  返回值是List[A]
  //一般来讲，当函数定义包含多个参数组时，参数组里的类型信息从左
  def dropWhile3[A](as:List[A])(f:A =>Boolean):List[A] ={
    as match {
      case Cons(h,t) if f(h) =>dropWhile3(t)(f)
      case _ =>as
    }
  }

  //练习3.6 answer  去掉List最后一个元素
  def init[A](l:List[A]):List[A] ={
    l match {
      case Nil => sys.error("init of empty list")
      case Cons(_,Nil) => Nil
      case Cons(h,t) => Cons(h,init(t))
    }
  }
  //(f:(A,B)=>B)  这个传的匿名函数其实是方法体内自己使用的，所以这个方法中所能使用的变量只有f，as，z
  def foldRight[A,B](as:List[A],z:B)(f:(A,B)=>B):B={
    as match {
      case Nil =>z
      case Cons(h,t) =>f(h,foldRight(t,z)(f))
    }
  }

  def sum2(ns:List[Int]) = foldRight(ns,0)(_ + _)

  //练习3.9
  def length[A](as:List[A]):Int ={
    foldRight(as,0)((x,y)=>y +1)
  }

  @tailrec//练习3.10
  def foldLeft[A,B](as:List[A],z:B)(f:(B,A)=>B):B ={
    as match {
      case Nil =>z
      case Cons(h,t)=>foldLeft(t,f(z,h))(f)
    }
  }
  //练习3.11-1
  def sum3(as:List[Int]):Int={
    foldLeft(as,0)(_ + _)
  }
  //练习3.11-2
  def product3(as:List[Double]):Double ={
    foldLeft(as,1.0)(_ * _)
  }
  //练习3.11-3
  def length3[A](as:List[A]):Int ={
    foldLeft(as,0)((x,y)=>x + 1)
  }
  //练习3.12
  def revert[A](as:List[A]):List[A] ={
    foldLeft(as,Nil:List[A])((b,a)=>Cons(a,b))
  }
  //练习3.13   问题应该是通过foldlef 来实现foldright 避免递归压栈
  // 方法之一，先reverse，再foldleft
  //方法二：如下
  def foldRight2[A,B](as:List[A],z:B)(f:(A,B)=>B):B={
    //foldRight2 方法中的A，B代表A，B类型
    //传入foldLeft时，foldLeft的A,B 代表A，(b:B) => b 类型,所以foldLef的(f:(B,A)=>B)相当于f:(B=>B,A)=>B=>B
    //  foldLef 得出的是(b:B) => b  因此调用只要加上z，就可以返回B类型（对foldRight2来说）了
    //b 代表以后传入且调用函数的变量，这里这个变量调用的时候的值是z
    //使用g代表函数类型为B=>B的对象 ,a代表未来传入的A,故f:(B=>B,A)=>B=>B  变为f:(g,a)=>b=>g(f(a,b))
    //foldLeft(as, (b:B) => b)((g:B=>B,a:A) => ({j:B=>g(f(a,j))})) (z)
    //foldLeft(as, (b:B) => b)((g,a) => j => g(f(a,j)))(z)
    foldLeft(as, (b:B) => b)((g,a) => b => g(f(a,b)))(z) //中间的b只是省略了类型，并非和前面的b相同
  }

  def foldLeftViaFoldRight[A,B](l: List[A], z: B)(f: (B,A) => B): B =
    foldRight(l, (b:B) => b)((a,g) => b => g(f(b,a)))(z)

  //练习3.14 answer  但是这里是往后面append的，往前append需要使用foldLeft
  def append[A](l:List[A],r:List[A]):List[A]={
    foldRight(l, r)((x:A,y:List[A])=>Cons(x,y))
  }
  //练习3.15 answer
  def allToOne[A](l:List[List[A]]):List[A]={
    foldRight(l, Nil:List[A])(append)
  }

}

class ListTest{
  @Test
  def test01(): Unit ={
    val a = List(1,2,5,7,4,9)
    print(List.dropWhile2(a,(x:Int) =>x%2 ==0))
    print(List.dropWhile2[Int](a,x =>x%2 ==0)) //当a中的2 变为“2” 时，上面的表达式不报错
  }

  @Test//练习3.7 第五章后再看
  def test02(): Unit ={
    //flodRight list从左往右递归压栈，最后计算的时候，右边先和初始化值进行计算

    //对象.方法名/函数名（参数）  ==   对象  方法名 参数
    //所以1.+(2) == 1 + 2
    //一元匿名函数 x => x.method()  == _.method
    //一元匿名函数 x => method(x)  === method
    //二元匿名函数 (x,y) => x.+(y)  ==== (x,y)=> x + y  === _ + _
    //(x,y) =>x.方法名(y) ==== (x,y) => x 方法名 y ==== _ 方法名 _
  }

  //练习3.8
  @Test
  def test03(): Unit ={
    val a = List(1,2,3,4,5)
    List.foldRight(a,Nil:List[Int])((x,y)=>Cons(x,y))//这里Nil需要用父类List[Int]装，否则后面匿名函数会报错
    //(x,y)=>Cons(x,y)  ===  Cons(_,_)
    print((x:Int,y:List[Int])=>Cons(x,y))//function2  变量在=> 后面只出现一次 使用_代替
  }
  @Test//练习3.9
  def test04():Unit ={
    val a = List(1,4,6,7)
    print(List.length(a))
  }

  @Test//练习3.11
  def test05(): Unit ={
    val a = List(1,4,6,7)
    print(List.sum3(a))
  }
  @Test//练习3.12
  def test06(): Unit ={
    val a = List(1,4,6,7)
    print(List.revert(a))
    //小结：fold A,B 的泛型不一样，也可以一样，一样的话代表list中2个元素的操作
  }

  @Test//练习3.13
  def test07(): Unit ={
    val a = List(1,4,6,7)
    print(List.foldRight2(a,0)(_ + _))
    //小结：fold A,B 的泛型不一样，也可以一样，一样的话代表list中2个元素的操作
  }

}
