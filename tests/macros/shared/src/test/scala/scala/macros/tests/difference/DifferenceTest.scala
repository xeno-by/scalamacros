package scala.macros.tests.difference

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.junit.Assert._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

@RunWith(classOf[JUnit4])
class DifferenceTest {

  case class User(name: String, pwd: String, age: Int = 1, isMerried: Boolean = false)

  case class User2(name: Option[String], pwd: String, age: Int = 1, isMerried: Boolean = false)

  @Test
  def simpleCaseClassAndPrimitives(): Unit = {
    val diff =
      Difference(User("Andrey", "Ivanov"), User("Ivanov", "Andrey", 1, true))
    assertEquals(
      Diff(
        Some("DifferenceTest.this.User"),
        List(
          DiffValue("Andrey", "Ivanov"),
          DiffValue("Ivanov", "Andrey"),
          DiffValue("false", "true"))),
      diff)
  }

  @Test
  def simpleCaseClassAndPrimitivesInOptionContext(): Unit = {
    val diff =
      Difference(User2(Option("Andrey"), "Ivanov"), User2(Option("Ivanov"), "Andrey", 1, true))
    val diff2 = Difference(User2(None, "Ivanov"), User2(Option("Ivanov"), "Andrey", 1, true))

    assertEquals(
      Diff(
        Some("DifferenceTest.this.User2"),
        List(
          DiffValue("Andrey", "Ivanov"),
          DiffValue("Ivanov", "Andrey"),
          DiffValue("false", "true"))),
      diff)
    assertEquals(
      Diff(
        Some("DifferenceTest.this.User2"),
        List(
          DiffValue("empty", "Ivanov"),
          DiffValue("Ivanov", "Andrey"),
          DiffValue("false", "true"))),
      diff2)
  }
}
