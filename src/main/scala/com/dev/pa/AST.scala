package com.dev.pa

import com.dev.pa.SourceInfoCarrier.Tag

object AST {

  case class Program(funcList: List[Func], mainFunc: Func)

  case class Func(name: funcName, params: List[Param], body: Block, tag: Tag)

  type funcName = String

  type Param = String

  type Block = List[Stmt]

  sealed abstract class Stmt { val tag: Tag }

  case class AssignStmt(lv: LValue, e: Expression, tag: Tag) extends Stmt
  case class IfStmt(cond: Expression, trueBlock: Block, falseBlockOpt: Option[Block], tag: Tag) extends Stmt
  case class WhileStmt(cond: Expression, block: Block, tag: Tag) extends Stmt
  case class ReturnStmt(e: Expression, tag: Tag) extends Stmt
  case class PrintStmt(e: Expression, tag: Tag) extends Stmt
  case class ReadLineStmt(lv: LValue, tag: Tag) extends Stmt
  case class CallStmt(retLv: TempVariable, name: funcName, args: List[Expression], tag: Tag) extends Stmt

  sealed abstract class Expression { val tag: Tag }

  case class Variable(x: String, tag: Tag) extends LValue
  case class TempVariable(x: String, tag: Tag) extends LValue

  case class UnaryMinus(arith: Arith, tag: Tag) extends Arith
  case class Plus(l: Arith, r: Arith, tag: Tag) extends Arith
  case class Minus(l: Arith, r: Arith, tag: Tag) extends Arith
  case class Mult(l: Arith, r: Arith, tag: Tag) extends Arith
  case class Div(l: Arith, r: Arith, tag: Tag) extends Arith
  case class IntAtom(i: Int, tag: Tag) extends Arith
  sealed abstract class LValue extends Arith { val tag: Tag }

  case class Not(cond: Expression, tag: Tag) extends Expression
  case class Eq(l: Expression, r: Expression, tag: Tag) extends Expression
  case class Ne(l: Expression, r: Expression, tag: Tag) extends Expression
  case class Lt(l: Arith, r: Arith, tag: Tag) extends Expression
  case class Le(l: Arith, r: Arith, tag: Tag) extends Expression
  case class Gt(l: Arith, r: Arith, tag: Tag) extends Expression
  case class Ge(l: Arith, r: Arith, tag: Tag) extends Expression
  case class And(l: Expression, r: Expression, tag: Tag) extends Expression
  case class Or(l: Expression, r: Expression, tag: Tag) extends Expression
  case class True(tag: Tag) extends Expression
  case class False(tag: Tag) extends Expression
  sealed abstract class Arith extends Expression { val tag: Tag }

}



