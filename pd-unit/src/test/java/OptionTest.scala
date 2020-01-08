import java.net.ServerSocket
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.{LinkedBlockingQueue, ThreadFactory, ThreadPoolExecutor, TimeUnit}

import org.junit.Test

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.{Failure, Success}

class OptionTest {
  @Test
  def test01: Unit ={
    println(new ServerSocket(0).getLocalPort)
  }

  @Test
  def test02: Unit ={
    println(Runtime.getRuntime.maxMemory())
    println(Runtime.getRuntime.totalMemory())
    println(Runtime.getRuntime.freeMemory())
  }

  @Test
  def test03: Unit ={

    def newCachedThreadPool(threadNum: Int, threadName: String, isDaemon: Boolean = true) = {
      val threadPool = new ThreadPoolExecutor(threadNum, threadNum, 120L, TimeUnit.SECONDS,
        new LinkedBlockingQueue[Runnable](10 * threadNum),
        new ThreadFactory {
          val num = new AtomicInteger(0)
          override def newThread(r: Runnable): Thread = {
            val t = new Thread(r)
            t.setDaemon(isDaemon)
            t.setName(threadName + num.incrementAndGet())
            t
          }
        })
      threadPool.allowCoreThreadTimeOut(true)
      threadPool
    }

    implicit val executor = ExecutionContext.fromExecutorService(newCachedThreadPool(100, "test", true))
    println(System.currentTimeMillis() + "--1")
    val f = Future {
      Thread.sleep(3000)
      println("haha")
    }//future是完全异步的,申明的时候就在异步执行了
    f onComplete{
      case Failure(t) =>
        println("failed")
      case Success(_) =>
        println("success")
    }//申明监听器 在futrue开始执行后也没关系,应该是有相应的处理的
    println(System.currentTimeMillis()+ "--2")
    Await.result(f,Duration(2000, TimeUnit.MILLISECONDS))
    //Thread.currentThread().join()
    //等待Duration的时间,执行完成就马上执行后面的代码,到时间没执行完成就报错
    //这里的时间不包括执行监听器的
    println(System.currentTimeMillis()+ "--3")
  }
}
