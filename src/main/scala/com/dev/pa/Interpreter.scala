package com.dev.pa

import com.dev.pa.AST._
import com.dev.pa.MemoryModel._

class Interpreter {

  def run(program: Program, input: List[String]): Unit = ???

  def runFunc(function: Func, memory: Memory): Memory = ???

//  map.get(l) match {
//    case Some(v) => v
//    case None =>
//      Printer.print("Runtime Error: invalid memory location")
//
//  }
}
