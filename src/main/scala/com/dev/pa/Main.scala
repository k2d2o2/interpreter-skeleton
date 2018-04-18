package com.dev.pa

import java.io.File

import com.typesafe.scalalogging.Logger
import org.antlr.v4.runtime._

//https://blog.knoldus.com/2016/06/22/generating-visiting-and-unit-testing-grammar-using-antlr4-with-java-and-scala/
object Main {
  val logger = Logger("Main")
  def main(args: Array[String]): Unit = {
    val sourceFile = new File("src/test/pa1/test1.pa1")
    val charStream = CharStreams.fromFileName(sourceFile.getCanonicalPath)
    val lexer = new MyGrammarLexer(charStream)
    val stream = new BufferedTokenStream(lexer)
    val parser = new MyGrammarParser(stream)
    val x: MyGrammarParser.ProgramContext = parser.program()
    val ast: AST.Program = Translator.transProgram(x)
    logger info ast.toString
    logger debug ast.toString
  }
}
