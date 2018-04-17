package com.dev.pa

case class AST(stmtList: List[Stmt])

sealed abstract class Stmt

case class IfStmt(condition: Condition, conclusion: Conclusion) extends Stmt

sealed abstract class Condition

sealed abstract class LogicalExpr extends Condition

case class AndExpr(l: LogicalExpr, r: LogicalExpr) extends LogicalExpr
case class OrExpr(l: LogicalExpr, r: LogicalExpr) extends LogicalExpr
case class CompExpr(l: CompOperand, op: CompOp, r: CompOperand) extends LogicalExpr
sealed abstract class LogicalEntity extends LogicalExpr

sealed abstract class CompOperand

sealed abstract class ArithExpr extends CompOperand

case class ArithBinOp(l: ArithExpr, op: ArithOp, r: ArithExpr) extends ArithExpr
case class UnaryMinus(arithExpr: ArithExpr) extends ArithExpr
sealed abstract class NumEntity extends ArithExpr

case class NumAtom(i: Int) extends NumEntity
case class NumVar(x: String) extends NumEntity

sealed abstract class ArithOp

case object Mult extends ArithOp
case object Div extends ArithOp
case object Plus extends ArithOp
case object Minus extends ArithOp

sealed abstract class CompOp

case object GT extends CompOp
case object GE extends CompOp
case object LT extends CompOp
case object LE extends CompOp
case object EQ extends CompOp

case object True extends LogicalEntity
case object False extends LogicalEntity
case class LogiVar(x: String) extends LogicalEntity

case class Conclusion(x: String)
