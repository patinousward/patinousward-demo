import org.junit.Test

class OptionTest {
  @Test
  def test01: Unit ={
      val a = Option(null)
      a.map(_ =>print(_))
  }
}
