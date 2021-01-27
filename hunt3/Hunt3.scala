package hunt3

import scala.collection.mutable.ListBuffer
import scala.io.Source

object Hunt3 {
  def main(args: Array[String]): Unit = {
    val input = Source.fromFile("src/main/scala/hunt3/input").getLines.toList
    val network = buildBoard(input.head, input.tail, Array.fill(150, 150)('.'))
    //printBoard(network) uncomment to see a visualiztion of the network
    val startingPoit = network.zipWithIndex.foldLeft((0, 0))((a, b) => {
      val y = b._1.indexWhere(b => b.equals('S'))
      if (y != -1) (b._2, y)
      else a
    })
    println(walk(startingPoit, "", List(), network))
  }

  def buildBoard(head: String, tail: List[String], network: Array[Array[Char]]): Array[Array[Char]] = {
    val coordinatesAndLine = head.split(" ")
    val coordinates = coordinatesAndLine(0).split(",")
    val line = coordinatesAndLine.lift(1).getOrElse("").replaceAll(",", "")
    val x = coordinates(0).toInt
    val y = coordinates(1).toInt
    network(x)(y) = way // initial position before moving
    if (line.isEmpty) buildBoard(tail.head, tail.tail, network)
    else if (tail.isEmpty) buildBoard(line.head.toString, line.tail, x, y, network)
    else buildBoard(tail.head, tail.tail, buildBoard(line.head.toString, line.tail, x, y, network))
  }

  private final val way = '*'

  def buildBoard(c: String, tail: String, x: Int, y: Int, network: Array[Array[Char]]): Array[Array[Char]] = {
    c match {
      case "D" => loop(tail, x, y + 1, network)
      case "U" => loop(tail, x, y - 1, network)
      case "L" => loop(tail, x - 1, y, network)
      case "R" => loop(tail, x + 1, y, network)
      case "S" | "F" | "X" => loop(tail, x, y, network, c(0))
      case _ => loop(tail, x, y, network)
    }
  }

  private def loop(rest: String, x: Int, y: Int, network: Array[Array[Char]], c: Char = way): Array[Array[Char]] = {
    network(x)(y) = c // position right after moving
    if (rest.isEmpty) network
    else buildBoard(rest.head.toString, rest.tail, x, y, network)
  }

  def walk(xy: (Int, Int), path: String, queue: List[Node], network: Array[Array[Char]]): String = {
    if (network(xy._1)(xy._2) == 'F') path
    else {
      network(xy._1)(xy._2) = '0'
      val node = Node(xy._1, xy._2, path)
      val neighbours = buildNeighbours(node, network)
      val newQueue = queue.filter(n => !neighbours.find(n2 => n.x == n2.x && n.y == n2.y && n.path.size >= n2.path.size).isDefined) ++ neighbours

      if (newQueue.isEmpty) node.path
      else walk((newQueue.head.x, newQueue.head.y), newQueue.head.path, newQueue.tail, network)
    }
  }

  def buildNeighbours(poped: Node, network: Array[Array[Char]]): List[Node] = {
    val list = ListBuffer[Node]()
    if ((poped.x - 1 > 0 && poped.x - 1 < network.length) && (network(poped.x - 1)(poped.y).equals(way) || network(poped.x - 1)(poped.y).equals('F'))) {
      list.addOne(Node(poped.x - 1, poped.y, poped.path + "L"))
    }
    if ((poped.x + 1 > 0 && poped.x + 1 < network.length) && (network(poped.x + 1)(poped.y).equals(way) || network(poped.x + 1)(poped.y).equals('F'))) {
      list.addOne(Node(poped.x + 1, poped.y, poped.path + "R"))
    }
    if ((poped.y - 1 > 0 && poped.y - 1 < network.length) && (network(poped.x)(poped.y - 1).equals(way) || network(poped.x - 1)(poped.y).equals('F'))) {
      list.addOne(Node(poped.x, poped.y - 1, poped.path + "U"))
    }
    if ((poped.y + 1 > 0 && poped.y + 1 < network.length) && (network(poped.x)(poped.y + 1).equals(way) || network(poped.x + 1)(poped.y).equals('F'))) {
      list.addOne(Node(poped.x, poped.y + 1, poped.path + "D"))
    }
    list.toList
  }

  case class Node(x: Int, y: Int, path: String)

  def printNetwork(network: Array[Array[Char]]): Unit = {
    network foreach { row => row foreach print; println }
  }
}
