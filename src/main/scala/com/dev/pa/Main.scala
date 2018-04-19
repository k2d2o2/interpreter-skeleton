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
    val sourceCodePath: String = remainingArgs.headOption.getOrElse("src/test/pa1/test3.pa1")
    logger info "Start program %s".format(sourceCodePath)
    val argsForCode: List[String] = remainingArgs.drop(1)

    val sourceFile = new File(sourceCodePath)
    val charStream = CharStreams.fromFileName(sourceFile.getCanonicalPath)
    val lexer = new MyGrammarLexer(charStream)
    val stream = new BufferedTokenStream(lexer)
    val parser = new MyGrammarParser(stream)
    val x: MyGrammarParser.ProgramContext = parser.program()
    val (ast: AST.Program, sourceInfoCarrier: SourceInfoCarrier) = (new Translator).transProgram(x)
    logger debug ast.toString
    new Interpreter(sourceInfoCarrier).run(ast, argsForCode)
    logger info "Program terminated."
  }
}
