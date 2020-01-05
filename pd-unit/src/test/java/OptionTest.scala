import org.junit.Test

class OptionTest {
  @Test
  def test01: Unit ={
      val a = Option(null)
      a.foreach(println)
  }
}
