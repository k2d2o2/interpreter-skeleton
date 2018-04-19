package com.dev.pa

class SourceInfoCarrier {
  import SourceInfoCarrier._

  private var idx = 0

  private def newTag(): Tag = {
    idx += 1
    idx
  }

  type map = Map[Tag, SourceInfo]
  type reverseMap = Map[SourceInfo, Tag]
  private var tagMap: (map, reverseMap) = (Map.empty, Map.empty)

  def getLine(tag: Tag): Int = tagMap._1.get(tag).map(_.lineNumber).getOrElse(SourceInfoCarrier.dummyLine)
  def getCharPoint(tag: Tag): Int = tagMap._1.get(tag).map(_.charPoint).getOrElse(SourceInfoCarrier.dummyCharPoint)

  def getTag(lineNumber: Int, charPoint: Int): Tag = {
    val sourceInfo = SourceInfo(lineNumber, charPoint)
    tagMap._2.getOrElse(sourceInfo, {
      val tag = newTag()
      val map = tagMap._1 + (tag -> sourceInfo)
      val reverseMap = tagMap._2 + (sourceInfo -> tag)
      tagMap = (map, reverseMap)
      tag
    })
  }
}

object SourceInfoCarrier {
  type Tag = Int
  val dummyLine: Int = -1
  val dummyCharPoint: Int = -1
  case class SourceInfo
  (
    lineNumber: Int,
    charPoint: Int
  )
}

