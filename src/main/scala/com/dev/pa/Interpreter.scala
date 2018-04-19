package com.dev.pa

import com.dev.pa.AST._
import com.dev.pa.MemoryModel._
import com.dev.pa.util.{PrettyPrintable, Printer}

import scala.io.StdIn
import scala.util.Try

class Interpreter {

  case class PA1Exception(msg: String) extends Exception

  def run(program: Program, inputs: List[String]): Unit = {
    try {
      // initialize memory
      val initMemory = MemoryModel.initialMemory(program)
      // get main function
      val mainFunction = program.mainFunc
      // parse arguments
      val args: List[PA1Value] = inputs.map {
        argStr => evaluateInput(argStr)
      }
      // run main function
      runFunc(mainFunction, args, initMemory)
    }
    catch {
      case e: PA1Exception =>
        Printer.println("[Error] %s".format(e.msg))
    }
  }

  // Our semantics: storing arguments is responsible for callee
  def runFunc(function: Func, args: List[PA1Value], memory: Memory): Memory = {
    val params = function.params
    if (params.length == args.length) {
      val argsMapedMemory = {
        params.zip(args).foldLeft(memory) {
          case (memory1, (param, v)) =>
            val l = evaluateParam(param)
            memory1.store(l, v)
        }
      }
      runBlock(function.body, argsMapedMemory)
    }
    else {
      throw PA1Exception(
        "Invalid argument. params: %s, args: %s"
          .format(PrettyPrintable.prettyStr(params), PrettyPrintable.prettyStr(args))
      )
    }
  }

  def evaluateParam(param: Param): Location = VarLoc(param)

  def runBlock(block: Block, memory: Memory): Memory = {
    if (memory.hasReturn) memory
    else {
      block.foldLeft(memory) {
        case (memory1, stmt) => runStmt(stmt, memory1)
      }
    }
  }

  def runStmt(stmt: Stmt, memory: Memory): Memory = {
    if (memory.hasReturn) memory
    else {
      stmt match {
        case AssignStmt(lv: LValue, e: Expression) => ???
        case IfStmt(cond: Expression, trueBlock: Block, Some(falseBlock)) => ???
        case IfStmt(cond: Expression, trueBlock: Block, None) => {
          val condValue = evaluateExpression(cond, memory)
          condValue match {
            case BoolVal(true) => {
              ???
            }
            case BoolVal(false) => {
              ???
            }
            case v => {
              throw PA1Exception(
                "Invalid condition value: %s".format(v.prettyStr)
              )
            }
          }
        }
          // Attension: do not use foldLeft interpreting while statement for tail recursion.
        case WhileStmt(cond: Expression, block: Block) => ???
        case ReturnStmt(e: Expression) => {
          val v = evaluateExpression(e, memory)
          memory.storeReturn(v)
        }
        case PrintStmt(e: Expression) => {
          val v = evaluateExpression(e, memory)
          Printer.println(v.prettyStr)
          memory
        }
        case ReadLineStmt(lv: LValue) => {
          val l = evaluateLValue(lv)
          val v = {
            Printer.print("INPUT: ")
            val input = StdIn.readLine()
            evaluateInput(input)
          }
          memory.store(l, v)
        }
          // Our semantics: storing return value is responsible for caller.
        case CallStmt(retLv: TempVariable, name: funcName, args: List[Expression]) => {
          val functionOpt = memory.lookupFunction(name)
          functionOpt match {
            case Some(function) => {
              val evaledArgs: List[PA1Value] = ???
              // Push environment
              val callerEnvironment: Environment = memory.getEnvironment
              val calleeInitMemory = memory.initEnvironment
              val calleeReturnMemory: Memory = runFunc(function, evaledArgs, calleeInitMemory)
              val returnValue: PA1Value = ???
              // Pop environment
              val callerMemory: Memory = calleeReturnMemory.setEnvironment(callerEnvironment)
              val returnValueMappedMemory: Memory = ???
              returnValueMappedMemory
            }
            case None =>
              throw PA1Exception(
                "Invalid function call: %s".format(name)
              )
          }
        }
      }
    }
  }

  def evaluateInput(input: String): PA1Value = {
    Try(IntVal(input.toInt))
      .recover { case e: NumberFormatException =>
        BoolVal(input.toBoolean)
      }
      .recover { case e: IllegalArgumentException =>
        throw PA1Exception("Illegal argument: %s".format(input))
      }.get
  }

  def evaluateExpression(expression: Expression, memory: Memory): PA1Value = {
    expression match {
      case Not(cond: Expression) => ???
      case Eq(l: Expression, r: Expression) => ???
      case Ne(l: Expression, r: Expression) => ???
      case Lt(l: Arith, r: Arith) => ???
      case Le(l: Arith, r: Arith) => ???
      case Gt(l: Arith, r: Arith) => ???
      case Ge(l: Arith, r: Arith) => ???
      case And(l: Expression, r: Expression) => ???
      case Or(l: Expression, r: Expression) => ???
      case True => ???
      case False => ???
      case arith: Arith => evaluateArith(arith, memory)
    }
  }

  def evaluateArith(arith: Arith, memory: Memory): PA1Value = {
    arith match {
      case UnaryMinus(arith: Arith) => ???
      case Plus(l: Arith, r: Arith) => {
        val v1 = evaluateArith(l, memory)
        val v2 = evaluateArith(r, memory)
        (v1, v2) match {
          case (IntVal(i1), IntVal(i2)) => IntVal(i1 + i2)
          case _ => throw PA1Exception("Cannot add %s, %s".format(v1.prettyStr, v2.prettyStr))
        }
      }
      case Minus(l: Arith, r: Arith) => ???
      case Mult(l: Arith, r: Arith) => ???
      case Div(l: Arith, r: Arith) => ???
      case IntAtom(i: Int) => IntVal(i)
      case lv: LValue => {
        val l: Location = evaluateLValue(lv)
        val v: PA1Value = {
          memory.lookup(l) match {
            case Some(v1) => v1
            case None =>
              throw PA1Exception("Invalid memory location: %s".format(l.prettyStr))
          }
        }
        v
      }
    }
  }

  def evaluateLValue(value: LValue): Location = {
    value match {
      case Variable(x) => VarLoc(x)
      case TempVariable(x) => VarLoc(x)
    }
  }

}
