package scala.macros.tests.difference

import scala.macros._

case class Diff(entity: Option[String], fields: List[DiffValue])

case class DiffValue(oldValue: String, newValue: String)

object Difference {
  inline def apply[A <: Product](lastState: A, newState: A): Diff = meta {
    val buff = Term.fresh("buff")
    println(q"${Type.Apply(Type.Name("_root_.scala.Option"), List(Type.Placeholder(Type.Bounds(None, None))))}")
    val body = A.vals.filter(_.isCase).map { f =>
      val fnName = f.name.value
      val v1 = Term.fresh(s"${fnName}1")
      val v2 = Term.fresh(s"${fnName}2")
      val isOption = false//f.info.<:<(Type.Existential(Type.Name("_root_.scala.Option"), List(Type.Placeholder(Type.Bounds(Some(Type.Name("_root_.scala.Int")), None)).asInstanceOf[Stat])))
      q"""
           val $v1 = $lastState.${Term.Name(fnName)}
           val $v2 = $newState.${Term.Name(fnName)}
           if(${Lit.Boolean(isOption)}) {
              ($v1: Any, $v2: Any) match {
                case (Some(v1), Some(v2)) if(v1 != v2) => $buff += DiffValue(v1.toString, v2.toString)
                case (None, Some(v2)) => $buff += DiffValue("empty", v2.toString)
                case (Some(v1), None) => $buff += DiffValue(v1.toString, "empty")
                case _ =>
              }
           } else if($v1 != $v2)  $buff += DiffValue($v1.toString, $v2.toString)
        """
    }

    q"""
        val $buff = _root_.scala.collection.mutable.ListBuffer[DiffValue]()
                ..$body
        Diff(Option(${Lit.String(A.toString)}), $buff.toList)"""
  }
}

//ExistentialTypeTree(
//  AppliedTypeTree(Ident(TypeName("Option")), List(Ident(TypeName("_$1")))),
//  List(TypeDef(Modifiers(DEFERRED | SYNTHETIC), TypeName("_$1"), List(), TypeBoundsTree(EmptyTree, EmptyTree)))
//)