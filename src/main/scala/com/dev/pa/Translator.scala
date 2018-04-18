package com.dev.pa

import com.dev.pa.MyGrammarParser._
import com.dev.pa.AST._

import scala.collection.JavaConverters._

object Translator {

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
    val stmtList = blockContext.stmt().asScala.toList.map(transStmt)
    stmtList
  }

  def transStmt(stmtContext: StmtContext): Stmt = {
    stmtContext match {
      case stmt: IfStmtContext => {
        val cond = transCond(stmt.cond())
        val trueBlock = transBlock(stmt.block())
        IfStmt(cond, trueBlock, None)
      }
      case stmt: IfElseStmtContext => {
        val cond = transCond(stmt.cond())
        val trueBlock = transBlock(stmt.block(0))
        val falseBlock = transBlock(stmt.block(1))
        IfStmt(cond, trueBlock, Some(falseBlock))
      }
      case stmt: WhileStmtContext => {
        val cond = transCond(stmt.cond())
        val block = transBlock(stmt.block())
        WhileStmt(cond, block)
      }
      case stmt: AssignStmtContext => {
        val lv = transLv(stmt.lv())
        val e = transE(stmt.e())
        AssignStmt(lv, e)
      }
      case stmt: ReturnStmtContext => {
        val e = transE(stmt.e())
        ReturnStmt(e)
      }
    }
  }

  def transCond(condContext: CondContext): Condition = {
    condContext match {
      case cond: CondBinEqContext => {
        val l = transCond(cond.cond(0))
        val r = transCond(cond.cond(1))
        Eq(l, r)
      }
      case cond: ArithBinGeContext => {
        val l = transArith(cond.arith(0))
        val r = transArith(cond.arith(1))
        Ge(l, r)
      }
      case cond: CondFalseContext => False
      case cond: NotCondContext => Not(transCond(cond.cond()))
      case cond: CondLvContext => CondLv(transLv(cond.lv()))
      case cond: ArithBinNeContext => {
        val l = transArith(cond.arith(0))
        val r = transArith(cond.arith(1))
        Ne(l, r)
      }
      case cond: ArithBinLeContext => {
        val l = transArith(cond.arith(0))
        val r = transArith(cond.arith(1))
        Le(l, r)
      }
      case cond: CondBinNeContext => {
        val l = transCond(cond.cond(0))
        val r = transCond(cond.cond(1))
        Ne(l, r)
      }
      case cond: ArithBinGtContext => {
        val l = transArith(cond.arith(0))
        val r = transArith(cond.arith(1))
        Gt(l, r)
      }
      case cond: CondTrueContext => True
      case cond: CondBinOrContext => {
        val l = transCond(cond.cond(0))
        val r = transCond(cond.cond(1))
        Or(l, r)
      }
      case cond: ArithBinLtContext => {
        val l = transArith(cond.arith(0))
        val r = transArith(cond.arith(1))
        Lt(l, r)
      }
      case cond: CondBinAndContext => {
        val l = transCond(cond.cond(0))
        val r = transCond(cond.cond(1))
        And(l, r)
      }
      case cond: ArithBinEqContext => {
        val l = transArith(cond.arith(0))
        val r = transArith(cond.arith(1))
        Eq(l, r)
      }
    }
  }

  def transLv(lvContext: LvContext): LValue = {
    val x = lvContext.IDENTIFIER().getText
    Variable(x)
  }

  def transE(eContext: EContext): Expression = {
    eContext match {
      case e: ELvContext => transLv(e.lv())
      case e: EInParenContext => transE(e.e())
      case e: CondEContext => transCond(e.cond())
      case e: ArithEContext => transArith(e.arith())
    }
  }

  def transArith(arithContext: ArithContext): Arith = {
    arithContext match {
      case arith: ArithBinMultContext => {
        val l = transArith(arith.arith(0))
        val r = transArith(arith.arith(1))
        Mult(l, r)
      }
      case arith: DecContext => IntAtom(arith.DECIMAL().getText.toInt)
      case arith: ArithBinMinusContext => {
        val l = transArith(arith.arith(0))
        val r = transArith(arith.arith(1))
        Minus(l, r)
      }
      case arith: ArithBinDivContext => {
        val l = transArith(arith.arith(0))
        val r = transArith(arith.arith(1))
        Div(l, r)
      }
      case arith: MinusArithContext => UnaryMinus(transArith(arith.arith()))
      case arith: ArithLvContext => ArithLv(transLv(arith.lv()))
      case arith: ArithBinPlusContext => {
        val l = transArith(arith.arith(0))
        val r = transArith(arith.arith(1))
        Plus(l, r)
      }
    }
  }

}
