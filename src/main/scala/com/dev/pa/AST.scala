package com.dev.pa

object AST {

  case class Program(funcList: List[Func], mainFunc: Func)

  case class Func(name: funcName, params: List[Param], body: Block)

  type funcName = String

  type Param = String

  type Block = List[Stmt]

  sealed abstract class Stmt

  case class AssignStmt(lv: LValue, e: Expression) extends Stmt
  case class IfStmt(cond: Expression, trueBlock: Block, falseBlockOpt: Option[Block]) extends Stmt
  case class WhileStmt(cond: Expression, block: Block) extends Stmt
  case class ReturnStmt(e: Expression) extends Stmt
  case class PrintStmt(e: Expression) extends Stmt
  case class CallStmt(retLv: TempVariable, name: funcName, args: List[Expression]) extends Stmt

  sealed abstract class Expression

  case class Variable(x: String) extends LValue
  case class TempVariable(x: String) extends LValue

  case class UnaryMinus(arith: Arith) extends Arith
  case class Plus(l: Arith, r: Arith) extends Arith
  case class Minus(l: Arith, r: Arith) extends Arith
  case class Mult(l: Arith, r: Arith) extends Arith
  case class Div(l: Arith, r: Arith) extends Arith
  case class IntAtom(i: Int) extends Arith
  sealed abstract class LValue extends Arith

  case class Not(cond: Expression) extends Expression
  case class Eq(l: Expression, r: Expression) extends Expression
  case class Ne(l: Expression, r: Expression) extends Expression
  case class Lt(l: Arith, r: Arith) extends Expression
  case class Le(l: Arith, r: Arith) extends Expression
  case class Gt(l: Arith, r: Arith) extends Expression
  case class Ge(l: Arith, r: Arith) extends Expression
  case class And(l: Expression, r: Expression) extends Expression
  case class Or(l: Expression, r: Expression) extends Expression
  case object True extends Expression
  case object False extends Expression
  sealed abstract class Arith extends Expression

}



