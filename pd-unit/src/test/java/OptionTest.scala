import org.junit.Test

class OptionTest {
  @Test
  def test01: Unit ={
  None.map(l =>Array("sd",l)).foreach(print)
  }

  @Test
  def test02: Unit ={
    println(Runtime.getRuntime.maxMemory())
    println(Runtime.getRuntime.totalMemory())
    println(Runtime.getRuntime.freeMemory())
  }
}
