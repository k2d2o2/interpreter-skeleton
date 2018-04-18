package com.dev.pa

object AST {

  case class Program(funcList: List[Func], mainFunc: Func)

  case class Func(name: String, params: List[Param], body: Block)

  type Param = String

  type Block = List[Stmt]

  sealed abstract class Stmt

  case class AssignStmt(lv: LValue, e: Expression) extends Stmt
  case class IfStmt(cond: Condition, trueBlock: Block, falseBlockOpt: Option[Block]) extends Stmt
  case class WhileStmt(cond: Condition, block: Block) extends Stmt
  case class ReturnStmt(e: Expression) extends Stmt

  sealed abstract class Expression

  sealed abstract class Arith extends Expression
  sealed abstract class Condition extends Expression
  sealed abstract class LValue extends Expression

  case class Variable(x: String) extends LValue

  case class UnaryMinus(arith: Arith) extends Arith
  case class Plus(l: Arith, r: Arith) extends Arith
  case class Minus(l: Arith, r: Arith) extends Arith
  case class Mult(l: Arith, r: Arith) extends Arith
  case class Div(l: Arith, r: Arith) extends Arith
  case class IntAtom(i: Int) extends Arith
  case class ArithLv(lv: LValue) extends Arith

  case class Not(cond: Condition) extends Condition
  case class Eq(l: Expression, r: Expression) extends Condition
  case class Ne(l: Expression, r: Expression) extends Condition
  case class Lt(l: Arith, r: Arith) extends Condition
  case class Le(l: Arith, r: Arith) extends Condition
  case class Gt(l: Arith, r: Arith) extends Condition
  case class Ge(l: Arith, r: Arith) extends Condition
  case class And(l: Condition, r: Condition) extends Condition
  case class Or(l: Condition, r: Condition) extends Condition
  case object True extends Condition
  case object False extends Condition
  case class CondLv(lv: LValue) extends Condition

}



