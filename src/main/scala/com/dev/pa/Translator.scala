package com.dev.pa

import com.dev.pa.MyGrammarParser._
import com.dev.pa.AST._
import com.dev.pa.SourceInfoCarrier.Tag
import org.antlr.v4.runtime.ParserRuleContext

import scala.collection.JavaConverters._

class Translator {
  private val sourceInfoCarrier = new SourceInfoCarrier
  private var tempIdx = 0

  def getSourceInfoTag(parserRuleContext: ParserRuleContext): SourceInfoCarrier.Tag = {
    val tag = {
      val line = parserRuleContext.start.getLine
      val charPoint = parserRuleContext.start.getCharPositionInLine
      sourceInfoCarrier.getTag(line, charPoint)
    }
    tag
  }

  def makeTempVar(tag: Tag): TempVariable = {
    tempIdx += 1
    TempVariable(tempIdx.toString, tag)
  }

  def transProgram(programContext: ProgramContext): (Program, SourceInfoCarrier) = {
    val functionList: List[FunctionContext] = programContext.function().asScala.toList
    val funcList: List[Func] = functionList.map(transFunction)
    val funcs = funcList.dropRight(1)
    val mainFunc = funcList.last
    (Program(funcs, mainFunc), sourceInfoCarrier)
  }

  def transFunction(functionContext: FunctionContext): Func = {
    val name = functionContext.IDENTIFIER().getText
    val params = transParams(functionContext.params())
    val body = transBlock(functionContext.block())
    val tag = getSourceInfoTag(functionContext)
    Func(name, params, body, tag)
  }

  def transParams(paramsContext: MyGrammarParser.ParamsContext): List[Param] = {
    paramsContext.param().asScala.toList.map(_.IDENTIFIER().getText)
  }

  def transBlock(blockContext: MyGrammarParser.BlockContext): Block = {
    val stmtList = blockContext.stmt().asScala.toList.flatMap(transStmt)
    stmtList
  }

  def transStmt(stmtContext: StmtContext): Block = {
    val tag = getSourceInfoTag(stmtContext)
    stmtContext match {
      case stmt: IfStmtContext => {
        val (b, cond) = transCond(stmt.cond())
        val trueBlock = transBlock(stmt.block())
        b :+ IfStmt(cond, trueBlock, None, tag)
      }
      case stmt: PrintStmtContext => {
        val (b, e) = transE(stmt.e())
        b :+ PrintStmt(e, tag)
      }
      case stmt: IfElseStmtContext => {
        val (b, cond) = transCond(stmt.cond())
        val trueBlock = transBlock(stmt.block(0))
        val falseBlock = transBlock(stmt.block(1))
        b :+ IfStmt(cond, trueBlock, Some(falseBlock), tag)
      }
      case stmt: WhileStmtContext => {
        val (b, cond) = transCond(stmt.cond())
        val block = transBlock(stmt.block())
        b :+ WhileStmt(cond, block, tag)
      }
      case stmt: AssignStmtContext => {
        val lv = transLv(stmt.lv())
        val (b, e) = transE(stmt.e())
        b :+ AssignStmt(lv, e, tag)
      }
      case stmt: ReadlineStmtContext => {
        val lv = transLv(stmt.lv())
        List(ReadLineStmt(lv, tag))
      }
      case stmt: ReturnStmtContext => {
        val (b, e) = transE(stmt.e())
        b :+ ReturnStmt(e, tag)
      }
    }
  }

  def transCond(condContext: CondContext): (Block, Expression) = {
    val tag = getSourceInfoTag(condContext)
    condContext match {
      case cond: CondBinEqContext => {
        val (b1, l) = transCond(cond.cond(0))
        val (b2, r) = transCond(cond.cond(1))
        (b1 ++ b2, Eq(l, r, tag))
      }
      case cond: ArithBinGeContext => {
        val (b1, l) = transArith(cond.arith(0))
        val (b2, r) = transArith(cond.arith(1))
        (b1 ++ b2, Ge(l, r, tag))
      }
      case cond: CondFalseContext => (List.empty, False(tag))
      case cond: NotCondContext => {
        val (b, e) = transCond(cond.cond())
        (b, Not(e, tag))
      }
      case cond: ArithBinNeContext => {
        val (b1, l) = transArith(cond.arith(0))
        val (b2, r) = transArith(cond.arith(1))
        (b1 ++ b2, Ne(l, r, tag))
      }
      case cond: ArithBinLeContext => {
        val (b1, l) = transArith(cond.arith(0))
        val (b2, r) = transArith(cond.arith(1))
        (b1 ++ b2, Le(l, r, tag))
      }
      case cond: CondBinNeContext => {
        val (b1, l) = transCond(cond.cond(0))
        val (b2, r) = transCond(cond.cond(1))
        (b1 ++ b2, Ne(l, r, tag))
      }
      case cond: ArithBinGtContext => {
        val (b1, l) = transArith(cond.arith(0))
        val (b2, r) = transArith(cond.arith(1))
        (b1 ++ b2, Gt(l, r, tag))
      }
      case cond: CondTrueContext => (List.empty, True(tag))
      case cond: CondBinOrContext => {
        val (b1, l) = transCond(cond.cond(0))
        val (b2, r) = transCond(cond.cond(1))
        (b1 ++ b2, Or(l, r, tag))
      }
      case cond: ArithBinLtContext => {
        val (b1, l) = transArith(cond.arith(0))
        val (b2, r) = transArith(cond.arith(1))
        (b1 ++ b2, Lt(l, r, tag))
      }
      case cond: ArithEContext => {
        transArith(cond.arith())
      }
      case cond: CondBinAndContext => {
        val (b1, l) = transCond(cond.cond(0))
        val (b2, r) = transCond(cond.cond(1))
        (b1 ++ b2, And(l, r, tag))
      }
      case cond: ArithBinEqContext => {
        val (b1, l) = transArith(cond.arith(0))
        val (b2, r) = transArith(cond.arith(1))
        (b1 ++ b2, Eq(l, r, tag))
      }
    }
  }

  def transLv(lvContext: LvContext): LValue = {
    val tag = getSourceInfoTag(lvContext)
    val x = lvContext.IDENTIFIER().getText
    Variable(x, tag)
  }

  def transE(eContext: EContext): (Block, Expression) = {
    eContext match {
      case e: EInParenContext => transE(e.e())
      case e: CondEContext => transCond(e.cond())
    }
  }

  def transArith(arithContext: ArithContext): (Block, Arith) = {
    val tag = getSourceInfoTag(arithContext)
    arithContext match {
      case arith: ArithBinMultContext => {
        val (b1, l) = transArith(arith.arith(0))
        val (b2, r) = transArith(arith.arith(1))
        (b1 ++ b2, Mult(l, r, tag))
      }
      case arith: DecContext => (List.empty, IntAtom(arith.DECIMAL().getText.toInt, tag))
      case arith: ArithBinMinusContext => {
        val (b1, l) = transArith(arith.arith(0))
        val (b2, r) = transArith(arith.arith(1))
        (b1 ++ b2, Minus(l, r, tag))
      }
      case arith: ArithBinDivContext => {
        val (b1, l) = transArith(arith.arith(0))
        val (b2, r) = transArith(arith.arith(1))
        (b1 ++ b2, Div(l, r, tag))
      }
      case arith: MinusArithContext => {
        val (b, e) = transArith(arith.arith())
        (b, UnaryMinus(e, tag))
      }
      case arith: ELvContext => {
        (List.empty, transLv(arith.lv()))
      }
      case arith: ECallContext => {
        val tempVar = makeTempVar(tag)
        val name = arith.IDENTIFIER().getText
        val (b, args) = transArgs(arith.args())
        (b ++ List(CallStmt(tempVar, name, args, tag)), tempVar)
      }
      case arith: ArithBinPlusContext => {
        val (b1, l) = transArith(arith.arith(0))
        val (b2, r) = transArith(arith.arith(1))
        (b1 ++ b2, Plus(l, r, tag))
      }
    }
  }

  def transArgs(argsContext: ArgsContext): (Block, List[Expression]) = {
    val blockEList = argsContext.arg().asScala.toList.map(transArg)
    val block = {
      blockEList.map(_._1).foldLeft(List.empty[Stmt]) {
        case (accumBlock, block1) => accumBlock ++ block1
      }
    }
    (block , blockEList.map(_._2))
  }

  def transArg(argContext: ArgContext): (Block, Expression) = {
    transE(argContext.e())
  }

}
