package com.dev.pa

import com.dev.pa.AST._
import com.dev.pa.MemoryModel._
import com.dev.pa.SourceInfoCarrier.Tag
import com.dev.pa.util.{PrettyPrintable, Printer}

import scala.io.StdIn
import scala.util.Try

class Interpreter(private val sic: SourceInfoCarrier) {

  case class PA1Exception(msg: String, tag: Tag) extends Exception

  def run(program: Program, inputs: List[String]): Unit = {
    try {
      // initialize memory
      val initMemory = MemoryModel.initialMemory(program)
      // get main function
      val mainFunction = program.mainFunc
      // parse arguments
      val args: List[PA1Value] = inputs.map {
        argStr => evaluateInput(argStr, mainFunction.tag)
      }
      // run main function
      runFunc(mainFunction, args, initMemory)
    }
    catch {
      case e: PA1Exception =>
        Printer.println("[%s:%s][Error] %s".format(sic.getLine(e.tag), sic.getCharPoint(e.tag), e.msg))
    }
  }

  // Our semantics: storing arguments is responsible for callee
  def runFunc(function: Func, args: List[PA1Value], memory: Memory): Memory = {
    val params = function.params
    if (params.length == args.length) {
      val argsMappedMemory = {
        params.zip(args).foldLeft(memory) {
          case (memory1, (param, v)) =>
            val l = evaluateParam(param)
            memory1.store(l, v)
        }
      }
      runBlock(function.body, argsMappedMemory)
    }
    else {
      throw PA1Exception(
        "Invalid argument. params: %s, args: %s"
          .format(PrettyPrintable.prettyStr(params), PrettyPrintable.prettyStr(args)),
        function.tag
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
        case AssignStmt(lv: LValue, e: Expression, tag: Tag) => ???
        case IfStmt(cond: Expression, trueBlock: Block, Some(falseBlock), tag: Tag) => ???
        case IfStmt(cond: Expression, trueBlock: Block, None, tag: Tag) => {
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
                "Invalid condition value: %s".format(v.prettyStr),
                tag
              )
            }
          }
        }
          // Attension: do not use foldLeft interpreting while statement for tail recursion.
        case WhileStmt(cond: Expression, block: Block, tag: Tag) => ???
        case ReturnStmt(e: Expression, tag: Tag) => {
          val v = evaluateExpression(e, memory)
          memory.storeReturn(v)
        }
        case PrintStmt(e: Expression, tag: Tag) => {
          val v = evaluateExpression(e, memory)
          Printer.println(v.prettyStr)
          memory
        }
        case ReadLineStmt(lv: LValue, tag: Tag) => {
          val l = evaluateLValue(lv)
          val v = {
            Printer.print("INPUT: ")
            val input = StdIn.readLine()
            evaluateInput(input, tag)
          }
          memory.store(l, v)
        }
          // Our semantics: storing return value is responsible for caller.
        case CallStmt(retLv: TempVariable, name: funcName, args: List[Expression], tag: Tag) => {
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
                "Invalid function call: %s".format(name),
                tag
              )
          }
        }
      }
    }
  }

  def evaluateInput(input: String, tag: Tag): PA1Value = {
    Try(IntVal(input.toInt))
      .recover { case e: NumberFormatException =>
        BoolVal(input.toBoolean)
      }
      .recover { case e: IllegalArgumentException =>
        throw PA1Exception("Illegal argument: %s".format(input), tag)
      }.get
  }

  def evaluateExpression(expression: Expression, memory: Memory): PA1Value = {
    expression match {
      case Not(cond: Expression, tag: Tag) => ???
      case Eq(l: Expression, r: Expression, tag: Tag) => ???
      case Ne(l: Expression, r: Expression, tag: Tag) => ???
      case Lt(l: Arith, r: Arith, tag: Tag) => ???
      case Le(l: Arith, r: Arith, tag: Tag) => ???
      case Gt(l: Arith, r: Arith, tag: Tag) => ???
      case Ge(l: Arith, r: Arith, tag: Tag) => ???
      case And(l: Expression, r: Expression, tag: Tag) => ???
      case Or(l: Expression, r: Expression, tag: Tag) => ???
      case True(tag: Tag) => ???
      case False(tag: Tag) => ???
      case arith: Arith => evaluateArith(arith, memory)
    }
  }

  def evaluateArith(arith: Arith, memory: Memory): PA1Value = {
    arith match {
      case UnaryMinus(arith: Arith, tag: Tag) => ???
      case Plus(l: Arith, r: Arith, tag: Tag) => {
        val v1 = evaluateArith(l, memory)
        val v2 = evaluateArith(r, memory)
        (v1, v2) match {
          case (IntVal(i1), IntVal(i2)) => IntVal(i1 + i2)
          case _ => throw PA1Exception("Cannot add %s, %s".format(v1.prettyStr, v2.prettyStr), tag)
        }
      }
      case Minus(l: Arith, r: Arith, tag: Tag) => ???
      case Mult(l: Arith, r: Arith, tag: Tag) => ???
      case Div(l: Arith, r: Arith, tag: Tag) => ???
      case IntAtom(i: Int, tag: Tag) => IntVal(i)
      case lv: LValue => {
        val l: Location = evaluateLValue(lv)
        val v: PA1Value = {
          memory.lookup(l) match {
            case Some(v1) => v1
            case None =>
              throw PA1Exception("Invalid memory location: %s".format(l.prettyStr), lv.tag)
          }
        }
        v
      }
    }
  }

  def evaluateLValue(value: LValue): Location = {
    value match {
      case Variable(x, tag: Tag) => VarLoc(x)
      case TempVariable(x, tag: Tag) => VarLoc(x)
    }
  }

}
