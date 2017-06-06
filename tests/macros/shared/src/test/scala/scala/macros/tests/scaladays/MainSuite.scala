package scala.macros.tests
package scaladays

import java.io.ByteArrayOutputStream
import org.junit._
import org.junit.runner._
import org.junit.runners._
import org.junit.Assert._

@RunWith(classOf[JUnit4])
class MainSuite {
  @Test
  def simple: Unit = {
    val out = new ByteArrayOutputStream()
    Console.withOut(out) {
      Test.main(Array[String]())
    }
    assertEquals(out.toString, "hello world\n")
  }
}
@main
object Test {
  println("hello world")
}
