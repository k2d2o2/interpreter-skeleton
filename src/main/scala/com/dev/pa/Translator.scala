package com.dev.pa

import com.dev.pa.MyGrammarParser._
import com.dev.pa.AST._

import scala.collection.JavaConverters._

class Translator {
  private var tempIdx = 0

  def makeTempVar(): TempVariable = {
    tempIdx += 1
    TempVariable(tempIdx.toString)
  }

  def transProgram(programContext: ProgramContext): Program = {
    val functionList: List[FunctionContext] = programContext.function().asScala.toList
    val funcList: List[Func] = functionList.map(transFunction)
    val funcs = funcList.dropRight(1)
    val mainFunc = funcList.last
    Program(funcs, mainFunc)
  }

  def transFunction(functionContext: FunctionContext): Func = {
    val name = functionContext.IDENTIFIER().getText
    val params = transParams(functionContext.params())
    val body = transBlock(functionContext.block())
    Func(name, params, body)
  }

  def transParams(paramsContext: MyGrammarParser.ParamsContext): List[Param] = {
    paramsContext.param().asScala.toList.map(_.IDENTIFIER().getText)
  }

  def transBlock(blockContext: MyGrammarParser.BlockContext): Block = {
    val stmtList = blockContext.stmt().asScala.toList.flatMap(transStmt)
    stmtList
  }

  def transStmt(stmtContext: StmtContext): Block = {
    stmtContext match {
      case stmt: IfStmtContext => {
        val (b, cond) = transCond(stmt.cond())
        val trueBlock = transBlock(stmt.block())
        b :+ IfStmt(cond, trueBlock, None)
      }
      case stmt: PrintStmtContext => {
        val (b, e) = transE(stmt.e())
        b :+ PrintStmt(e)
      }
      case stmt: IfElseStmtContext => {
        val (b, cond) = transCond(stmt.cond())
        val trueBlock = transBlock(stmt.block(0))
        val falseBlock = transBlock(stmt.block(1))
        b :+ IfStmt(cond, trueBlock, Some(falseBlock))
      }
      case stmt: WhileStmtContext => {
        val (b, cond) = transCond(stmt.cond())
        val block = transBlock(stmt.block())
        b :+ WhileStmt(cond, block)
      }
      case stmt: AssignStmtContext => {
        val lv = transLv(stmt.lv())
        val (b, e) = transE(stmt.e())
        b :+ AssignStmt(lv, e)
      }
      case stmt: ReadlineStmtContext => {
        val lv = transLv(stmt.lv())
        List(ReadLineStmt(lv))
      }
      case stmt: ReturnStmtContext => {
        val (b, e) = transE(stmt.e())
        b :+ ReturnStmt(e)
      }
    }
  }

  def transCond(condContext: CondContext): (Block, Expression) = {
    condContext match {
      case cond: CondBinEqContext => {
        val (b1, l) = transCond(cond.cond(0))
        val (b2, r) = transCond(cond.cond(1))
        (b1 ++ b2, Eq(l, r))
      }
      case cond: ArithBinGeContext => {
        val (b1, l) = transArith(cond.arith(0))
        val (b2, r) = transArith(cond.arith(1))
        (b1 ++ b2, Ge(l, r))
      }
      case cond: CondFalseContext => (List.empty, False)
      case cond: NotCondContext => {
        val (b, e) = transCond(cond.cond())
        (b, Not(e))
      }
      case cond: ArithBinNeContext => {
        val (b1, l) = transArith(cond.arith(0))
        val (b2, r) = transArith(cond.arith(1))
        (b1 ++ b2, Ne(l, r))
      }
      case cond: ArithBinLeContext => {
        val (b1, l) = transArith(cond.arith(0))
        val (b2, r) = transArith(cond.arith(1))
        (b1 ++ b2, Le(l, r))
      }
      case cond: CondBinNeContext => {
        val (b1, l) = transCond(cond.cond(0))
        val (b2, r) = transCond(cond.cond(1))
        (b1 ++ b2, Ne(l, r))
      }
      case cond: ArithBinGtContext => {
        val (b1, l) = transArith(cond.arith(0))
        val (b2, r) = transArith(cond.arith(1))
        (b1 ++ b2, Gt(l, r))
      }
      case cond: CondTrueContext => (List.empty, True)
      case cond: CondBinOrContext => {
        val (b1, l) = transCond(cond.cond(0))
        val (b2, r) = transCond(cond.cond(1))
        (b1 ++ b2, Or(l, r))
      }
      case cond: ArithBinLtContext => {
        val (b1, l) = transArith(cond.arith(0))
        val (b2, r) = transArith(cond.arith(1))
        (b1 ++ b2, Lt(l, r))
      }
      case cond: ArithEContext => {
        transArith(cond.arith())
      }
      case cond: CondBinAndContext => {
        val (b1, l) = transCond(cond.cond(0))
        val (b2, r) = transCond(cond.cond(1))
        (b1 ++ b2, And(l, r))
      }
      case cond: ArithBinEqContext => {
        val (b1, l) = transArith(cond.arith(0))
        val (b2, r) = transArith(cond.arith(1))
        (b1 ++ b2, Eq(l, r))
      }
    }
  }

  def transLv(lvContext: LvContext): LValue = {
    val x = lvContext.IDENTIFIER().getText
    Variable(x)
  }

  def transE(eContext: EContext): (Block, Expression) = {
    eContext match {
      case e: EInParenContext => transE(e.e())
      case e: CondEContext => transCond(e.cond())
    }
  }

  def transArith(arithContext: ArithContext): (Block, Arith) = {
    arithContext match {
      case arith: ArithBinMultContext => {
        val (b1, l) = transArith(arith.arith(0))
        val (b2, r) = transArith(arith.arith(1))
        (b1 ++ b2, Mult(l, r))
      }
      case arith: DecContext => (List.empty, IntAtom(arith.DECIMAL().getText.toInt))
      case arith: ArithBinMinusContext => {
        val (b1, l) = transArith(arith.arith(0))
        val (b2, r) = transArith(arith.arith(1))
        (b1 ++ b2, Minus(l, r))
      }
      case arith: ArithBinDivContext => {
        val (b1, l) = transArith(arith.arith(0))
        val (b2, r) = transArith(arith.arith(1))
        (b1 ++ b2, Div(l, r))
      }
      case arith: MinusArithContext => {
        val (b, e) = transArith(arith.arith())
        (b, UnaryMinus(e))
      }
      case arith: ELvContext => {
        (List.empty, transLv(arith.lv()))
      }
      case arith: ECallContext => {
        val tempVar = makeTempVar()
        val name = arith.IDENTIFIER().getText
        val (b, args) = transArgs(arith.args())
        (b ++ List(CallStmt(tempVar, name, args)), tempVar)
      }
      case arith: ArithBinPlusContext => {
        val (b1, l) = transArith(arith.arith(0))
        val (b2, r) = transArith(arith.arith(1))
        (b1 ++ b2, Plus(l, r))
      }
    }
  }

  def transArgs(argsContext: ArgsContext): (Block, List[Expression]) = {
    val blockEList = argsContext.e().asScala.toList.map(transE)
    val block = {
      blockEList.map(_._1).foldLeft(List.empty[Stmt]) {
        case (accumBlock, block1) => accumBlock ++ block1
      }
    }
    (block , blockEList.map(_._2))
  }

}
