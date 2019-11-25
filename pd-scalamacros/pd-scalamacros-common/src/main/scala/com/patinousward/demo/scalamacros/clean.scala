package com.patinousward.demo.scalamacros

import scala.reflect.macros.whitebox
import scala.language.experimental.macros
import scala.annotation.{StaticAnnotation, compileTimeOnly}


@compileTimeOnly("enable macro paradise to expand macro annotations")
class clean extends StaticAnnotation {
  def macroTransform(annottees: Any*): Any = macro clean.impl
}

object clean {
  def impl(c: whitebox.Context)(annottees: c.Expr[Any]*): c.Expr[Any] = {
    import c.universe._
    val class_tree = annottees.map(_.tree).toList match {
      //::Nil不可缺
      case q"$mods val $pat = $expr"::Nil =>{

        c.abort(c.enclosingPosition, "bingo")
      }
      case _ => c.abort(c.enclosingPosition, "Annotation @clean can be used only with var or val")
    }
    c.Expr[Any](class_tree)
  }
}
