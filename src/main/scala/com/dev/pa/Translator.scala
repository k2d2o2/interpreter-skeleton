package com.dev.pa

import com.dev.pa.MyGrammarParser._

import scala.collection.JavaConverters._

class Translator {

  def transRuleSet(rule_set: Rule_setContext): AST = {
    val singleSetList: List[Single_ruleContext] = rule_set.single_rule().asScala.toList
    AST(singleSetList.map(transSingleSet))
  }

  def transSingleSet(single_set: Single_ruleContext): Stmt = {
    val condition = transCond(single_set.condition())
    IfStmt(condition, Conclusion("x"))
  }

  def transCond(condition: ConditionContext): Condition = {
    transLogi(condition.logical_expr())
  }

  def transLogi(logicalExpr: Logical_exprContext): LogicalExpr = {
    logicalExpr match {
      case _: LogicalEntityContext => LogiVar("a")
      case _: ComparisonExpressionContext => LogiVar("b")
      case _: LogicalExpressionInParenContext => LogiVar("c")
      case _: LogicalExpressionAndContext => LogiVar("d")
      case _: LogicalExpressionOrContext => LogiVar("e")
    }
  }
}
