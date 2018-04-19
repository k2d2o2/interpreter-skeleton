package com.dev.pa

object OptionParser {

  type remainingArgs = List[String]

  def parse(args: Array[String]): (Options, remainingArgs) = {
    val options = Options()
    (options, args.toList)
  }

}
