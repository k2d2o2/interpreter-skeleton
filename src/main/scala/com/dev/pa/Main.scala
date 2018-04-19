package com.dev.pa

import java.io.File

import com.typesafe.scalalogging.Logger
import org.antlr.v4.runtime._

//https://blog.knoldus.com/2016/06/22/generating-visiting-and-unit-testing-grammar-using-antlr4-with-java-and-scala/
object Main {
  val logger = Logger("Main")
  def main(args: Array[String]): Unit = {
    val (_, remainingArgs) = OptionParser.parse(args)

    // get interpreter args
    val sourceCodePath: String = remainingArgs.headOption.getOrElse("src/test/pa1/test1.pa1")
    val argsForCode: List[String] = remainingArgs.drop(1)

    val sourceFile = new File(sourceCodePath)
    val charStream = CharStreams.fromFileName(sourceFile.getCanonicalPath)
    val lexer = new MyGrammarLexer(charStream)
    val stream = new BufferedTokenStream(lexer)
    val parser = new MyGrammarParser(stream)
    val x: MyGrammarParser.ProgramContext = parser.program()
    val ast: AST.Program = (new Translator).transProgram(x)
    logger info ast.toString
    (new Interpreter).run(ast, argsForCode)
    logger info "Program terminated."
  }
}
