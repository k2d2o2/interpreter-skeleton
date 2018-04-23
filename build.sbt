name := "pl_exercise"

version := "0.1"

scalaVersion := "2.12.5"


// sbt 1.1.x
enablePlugins(Antlr4Plugin)

antlr4Version in Antlr4 := "4.7.1"

antlr4PackageName in Antlr4 := Some("com.dev.pa")

antlr4GenListener in Antlr4 := true // default: true

antlr4GenVisitor in Antlr4 := true // default: false

mainClass in Compile := Some("com.dev.pa.Main")

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3"

libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.9.0"

libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.5"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test"

