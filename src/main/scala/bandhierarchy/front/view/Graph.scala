package bandhierarchy.front.view

import org.scalajs.dom
import org.singlespaced.d3js.forceModule.Node
import org.singlespaced.d3js.{Link, Selection, d3}

import scala.scalajs.js
import scala.scalajs.js.UndefOr

object Graph {
  class GraphNode(val name: String, val id: Int, private val _weight: Double) extends Node {
    weight = _weight
  }

  class GraphLink(_source: GraphNode, _target: GraphNode) extends Link[GraphNode] {
    override def source: GraphNode = _source
    override def target: GraphNode = _target
  }

  val width: Double = 600
  val height: Double = 400

  val force = d3.layout.force[GraphNode, GraphLink]()
    .charge(-120)
    .linkDistance(30)
    .size((width, height))

  val svg = d3.select("#graph")
    .append("svg")
    .attr("width", width)
    .attr("height", height)

  def start(_nodes: js.Array[GraphNode], _links: js.Array[GraphLink]): Unit = {
    force
      .nodes(_nodes)
      .links(_links)
      .start()

    val link = svg.selectAll[GraphLink](".link")
      .data(_links)
      .enter().append("line")
      .attr("class", "link")
      .attr("stroke", "#BBB")
      .attr("stroke-width", 1)

    val node: Selection[GraphNode] = svg.selectAll[GraphNode](".node")
      .data(_nodes)
      .enter().append("circle")
      .attr("class", "node")
      .call(force.drag)

    node.attr("r", mkFunction(node, (n: GraphNode, i) => n.weight))

    node.append("title").text(mkFunction(node, (n: GraphNode, i) => n.name))

    force.on("tick", (e: dom.Event) => {
      link
        .attr("x1", mkFunction(link, (n: GraphLink, i) => n.source.x))
        .attr("y1", mkFunction(link, (n: GraphLink, i) => n.source.y))
        .attr("x2", mkFunction(link, (n: GraphLink, i) => n.target.x))
        .attr("y2", mkFunction(link, (n: GraphLink, i) => n.target.y))

      node
        .attr("cx", mkFunction(node, (n: GraphNode, i) => n.x))
        .attr("cy", mkFunction(node, (n: GraphNode, i) => n.y))

      ()
    })

    ()
  }

  private def mkFunction[T, R](sel: Selection[T], f: (T, Int) => R) = {
    new scala.scalajs.js.Function3[T, Int, scala.scalajs.js.UndefOr[Int], scala.scalajs.js.|[scala.scalajs.js.|[Double, String], Boolean]] {
      override def apply(t: T, i: Int, j: UndefOr[Int]) = f(t, i).asInstanceOf[d3.Primitive]
    }.asInstanceOf[sel.DatumFunction[d3.Primitive]]
  }
}
