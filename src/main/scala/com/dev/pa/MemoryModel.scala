package com.dev.pa

object MemoryModel {

  def initialMemory(program: AST.Program): Memory = {
    val functionMap = program.funcList.map(func => func.name -> func).toMap
    val emptyEnv = Environment()
    Memory(functionMap, emptyEnv)
  }

  case class Memory(functionMap: Map[AST.funcName, AST.Func], environment: Environment)

  case class Environment(private val map: Map[Location, Value] = Map.empty) {
    def store(l: Location, v: Value): Environment = this.copy(map = map + (l -> v))
    def lookup(l: Location): Option[Value] = map.get(l)
  }

  sealed abstract class Location

  case class VarLoc(x: String) extends Location

  sealed abstract class Value

  case class IntVal(i: Int) extends Value
  case class BoolVal(b: Boolean) extends Value
}
