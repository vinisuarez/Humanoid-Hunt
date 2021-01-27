package hunt2

import scala.annotation.tailrec
import scala.io.Source

object Hunt2 {
  def main(args: Array[String]): Unit = {
    val input = Source.fromFile("src/main/scala/hunt2/input").getLines().toList.head
    val first = input.groupBy(i => i).map(i2 => (i2._1, i2._2.size)).toSeq.sortWith(_._2 > _._2)(0)._1
    println(run(input, first, ""))
  }

  @tailrec
  def run(input: String, char: Char, acc: String): String = {
    if (char.equals(';')) acc
    else run(input, getCharAfterThatMostRepeat(input, char), acc + char)
  }

  private def getCharAfterThatMostRepeat(input: String, char: Char): Char = {
    input
      .zipWithIndex
      .map(i => {
        if (i._1.equals(char))
          input.charAt(i._2 + 1).toString
        else ""
      })
      .toList
      .filter(_.nonEmpty)
      .groupBy(i => i)
      .map(i2 => (i2._1, i2._2.size))
      .toSeq
      .sortWith(_._2 > _._2)(0)._1(0)
  }
}
