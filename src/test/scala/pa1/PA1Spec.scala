package pa1


import java.io._

import com.dev.pa.Main
import org.scalatest._

import scala.collection.mutable.Stack

class PA1Spec extends FlatSpec with Matchers {

  "test1.pa1" should "print correct result" in {
    val os = new ByteArrayOutputStream()
    val myOut = new PrintStream(os)
    val userInputs = ""
    val myIn = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(userInputs.getBytes)))
    val mainArgs = Array("test/pa1/test1.pa1")

    try {
      // get interpreter args
      val (options, sourceCodePath, argsForCode) = Main.processArgs(mainArgs)
      Main.run(myIn, myOut, options, sourceCodePath, argsForCode)
      val printResult = os.toString
      val lastPrint = printResult.replaceAll("(INPUT: |\r|\n)", "")
      lastPrint should be("3")
    }
    finally {
      myOut.close()
      myIn.close()
    }
  }

  "test2.pa1" should "print correct result" in {
    val os = new ByteArrayOutputStream()
    val myOut = new PrintStream(os)
    val userInputs = ""
    val myIn = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(userInputs.getBytes)))
    val mainArgs = Array("test/pa1/test2.pa1", "3", "4")

    try {
      // get interpreter args
      val (options, sourceCodePath, argsForCode) = Main.processArgs(mainArgs)
      Main.run(myIn, myOut, options, sourceCodePath, argsForCode)
      val printResult = os.toString
      val lastPrint = printResult.replaceAll("(INPUT: |\r|\n)", "")
      lastPrint should be("7")
    }
    finally {
      myOut.close()
      myIn.close()
    }
  }

  "test3.pa1" should "print correct result" in {
    val os = new ByteArrayOutputStream()
    val myOut = new PrintStream(os)
    val userInputs = "1\r4\r"
    val myIn = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(userInputs.getBytes)))
    val mainArgs = Array("test/pa1/test3.pa1")

    try {
      // get interpreter args
      val (options, sourceCodePath, argsForCode) = Main.processArgs(mainArgs)
      Main.run(myIn, myOut, options, sourceCodePath, argsForCode)
      val printResult = os.toString
      val lastPrint = printResult.replaceAll("(INPUT: |\r|\n)", "")
      lastPrint should be("6")
    }
    finally {
      myOut.close()
      myIn.close()
    }
  }

//  "A Stack" should "pop values in last-in-first-out order" in {
//    val stack = new Stack[Int]
//    stack.push(1)
//    stack.push(2)
//    stack.pop() should be (2)
//    stack.pop() should be (1)
//  }
//
//  it should "throw NoSuchElementException if an empty stack is popped" in {
//    val emptyStack = new Stack[Int]
//    a [NoSuchElementException] should be thrownBy {
//      emptyStack.pop()
//    }
//  }
}