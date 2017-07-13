package scala.macros.tests.difference

import scala.macros._

case class Diff(entity: Option[String], fields: List[DiffValue])

case class DiffValue(oldValue: Any, newValue: Any)

object Difference {
  inline def apply[A <: Product](lastState: A, newState: A): Diff = meta {
    val buff = Term.fresh("buff")
    val body = A.vals.filter(_.isCase).map { f =>
      val fnName = f.name.value
      val v1 = Term.fresh(s"${fnName}1")
      val v2 = Term.fresh(s"${fnName}2")
      q"""
           val $v1 = $lastState.${Term.Name(fnName)}
           val $v2 = $newState.${Term.Name(fnName)}
           if($v1 != $v2 ) $buff += DiffValue($v1, $v2)
        """
    }

    q"""val $buff = scala.collection.mutable.ListBuffer[DiffValue]()
                ..$body
        Diff(Option(${Lit.String(A.toString)}), $buff.toList)"""
  }
}
