package com.dev.pa

import java.io._

import com.typesafe.scalalogging.Logger
import org.antlr.v4.runtime._

//https://blog.knoldus.com/2016/06/22/generating-visiting-and-unit-testing-grammar-using-antlr4-with-java-and-scala/
object Main {
  val logger = Logger("Main")
  def main(args: Array[String]): Unit = {
    val inC = new BufferedReader(new InputStreamReader(System.in))
    val outC = System.out
    val (options, sourceCodePath, argsForCode) = processArgs(args)
    run(inC, outC, options, sourceCodePath, argsForCode)
  }

  def processArgs(args: Array[String]): (Options, String, List[String]) = {
    val (options, remainingArgs) = OptionParser.parse(args)

    // get interpreter args
    val sourceCodePath: String = remainingArgs.headOption.getOrElse("test/pa1/test3.pa1")
    val argsForCode: List[String] = remainingArgs.drop(1)

    (options, sourceCodePath, argsForCode)
  }

  def run (inC: BufferedReader, outC: PrintStream, options: Options, sourceCodePath: String, argsForCode: List[String]): Unit = {
    logger info "Start program %s".format(sourceCodePath)
    val sourceFile = new File(sourceCodePath)
    val charStream = CharStreams.fromFileName(sourceFile.getCanonicalPath)
    val lexer = new MyGrammarLexer(charStream)
    val stream = new BufferedTokenStream(lexer)
    val parser = new MyGrammarParser(stream)
    val x: MyGrammarParser.ProgramContext = parser.program()
    val (ast: AST.Program, sourceInfoCarrier: SourceInfoCarrier) = (new Translator).transProgram(x)
    logger debug ast.toString
    new Interpreter(sourceInfoCarrier, inC, outC).run(ast, argsForCode)
    logger info "Program terminated."
  }
}
