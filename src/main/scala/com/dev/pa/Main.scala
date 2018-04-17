package com.dev.pa

import com.typesafe.scalalogging.Logger
import org.antlr.v4.runtime._

//https://blog.knoldus.com/2016/06/22/generating-visiting-and-unit-testing-grammar-using-antlr4-with-java-and-scala/
object Main {
  val logger = Logger("Main")
  def main(args: Array[String]): Unit = {
    val charStream = CharStreams.fromString("if true and true or true then abcd;")
    val lexer = new MyGrammarLexer(charStream)
    val stream = new BufferedTokenStream(lexer)
    val parser = new MyGrammarParser(stream)
    val x: MyGrammarParser.Rule_setContext = parser.rule_set()
    val ast: AST = (new Translator).transRuleSet(x)
    ast
    logger info ast.toString
    logger debug ast.toString
  }
}
