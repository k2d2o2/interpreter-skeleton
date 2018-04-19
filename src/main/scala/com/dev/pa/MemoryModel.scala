package com.dev.pa

import com.dev.pa.util.PrettyPrintable

object MemoryModel {

  def initialMemory(program: AST.Program): Memory = {
    val functionMap = (program.funcList :+ program.mainFunc).map(func => func.name -> func).toMap
    val emptyEnv = Environment()
    Memory(functionMap, emptyEnv)
  }

  case class Memory
  (
    private val functionMap: Map[AST.funcName, AST.Func],
    private val environment: Environment,
  ) {
    def lookupFunction(name: AST.funcName): Option[AST.Func] = functionMap.get(name)

    def initEnvironment: Memory = this.copy(environment = Environment())
    def getEnvironment: Environment = environment
    def setEnvironment(env: Environment): Memory = this.copy(environment = env)

    def store(l: Location, v: PA1Value): Memory = this.copy(environment = environment.store(l, v))
    def lookup(l: Location): Option[PA1Value] = environment.lookup(l)

    def storeReturn(v: PA1Value): Memory = this.copy(environment = environment.storeReturn(v))
    def hasReturn: Boolean = environment.hasReturn
    def lookupReturn: PA1Value = environment.lookupReturn
  }

  case class Environment
  (
    private val map: Map[Location, PA1Value] = Map.empty,
    private val returnValue: Option[PA1Value] = None
  ) {
    def store(l: Location, v: PA1Value): Environment = this.copy(map = map + (l -> v))
    def lookup(l: Location): Option[PA1Value] = map.get(l)
    def storeReturn(v: PA1Value): Environment = this.copy(returnValue = Some(v))
    def hasReturn: Boolean = returnValue.nonEmpty
    def lookupReturn: PA1Value = returnValue.getOrElse(VoidVal)

  }

  sealed abstract class Location extends PrettyPrintable

  case class VarLoc(x: String) extends Location

  sealed abstract class PA1Value extends PrettyPrintable

  case class IntVal(i: Int) extends PA1Value
  case class BoolVal(b: Boolean) extends PA1Value
  case object VoidVal extends PA1Value

}
