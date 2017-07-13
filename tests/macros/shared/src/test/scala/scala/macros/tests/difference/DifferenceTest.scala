package scala.macros.tests.difference

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.junit.Assert._

@RunWith(classOf[JUnit4])
class DifferenceTest {

  case class User(name: String, pwd: String, age: Int = 1, isMerried: Boolean = false)

  @Test
  def simpleCaseClassAndPrimitives(): Unit = {
    val diff = Difference(User("Andrey", "Ivanov"), User("Ivanov", "Andrey", 1, true))
    assertEquals(Diff(Some("User"), List(DiffValue("Andrey", "Ivanov"), DiffValue("Ivanov", "Andrey"), DiffValue(false, true))), diff)
  }
}
