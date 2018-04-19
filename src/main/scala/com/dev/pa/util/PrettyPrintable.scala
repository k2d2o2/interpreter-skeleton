package com.dev.pa.util


trait PrettyPrintable {
  def prettyStr: String = PrettyPrintable.prettyStr(this)
}

object PrettyPrintable {
  object MapExtractor {
    def unapplySeq[A <: Any, B <: Any](m: Map[A, B]): Option[Seq[(Any, Any)]] = Some(m.toSeq)
  }

  def prettyStr(o: Any): String = {
    o match {
      case l: List[_] => l.map(prettyStr).mkString("[", ";", "]")
      case opt: Option[_] => opt.map(prettyStr).getOrElse("null")
      case MapExtractor(m @ _*) => m.map{case (k, v) => prettyStr(k) + "->" + prettyStr(v)}.mkString("[", ";","]")
      case prod: Product => {
        val iter = prod.productIterator
        iter.map(prettyStr).mkString("(", ",", ")")
      }
      case others => others.toString
    }
  }
}

