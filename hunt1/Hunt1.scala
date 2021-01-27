package hunt1

import scala.annotation.tailrec
import scala.io.Source

object Hunt1 {
  def main(args: Array[String]): Unit = {
    val input = Source.fromFile("src/main/scala/hunt1/input").getLines.toList
    println(input.map(run).mkString)
  }

  def run(input: String): Char = {
    val stream = input.grouped(8).map(i => Integer.parseInt(i, 2)).toList
    run(0, stream, false)
  }

  @tailrec
  def run(current: Int, stream: List[Int], foundValid: Boolean): Char = {
    if (stream(current) < stream.size) {
      run(stream(current), stream, true)
    } else {
      if (foundValid) {
        stream(current).toChar
      } else {
        run(current + 1, stream, false)
      }
    }
  }
}
