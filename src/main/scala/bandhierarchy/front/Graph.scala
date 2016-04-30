package bandhierarchy.front

import org.scalajs.dom
import org.singlespaced.d3js.forceModule.Node
import org.singlespaced.d3js.{Link, Selection, d3}

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport
import scala.scalajs.js.{JSApp, UndefOr}

object Graph extends JSApp {
  @js.native
  trait GraphJson extends js.Object {
    val nodes: js.Array[JsonNode] = js.native
    val links: js.Array[JsonLink] = js.native
  }

  @js.native
  trait JsonNode extends js.Object {
    val name: String = js.native
    val id: Int = js.native
    val weight: Double = js.native
  }

  @js.native
  trait JsonLink extends js.Object {
    val source: Int = js.native
    val target: Int = js.native
  }

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

  @JSExport
  override def main(): Unit = {
    d3.json("/target/graph.json", (error: js.Any, jsonRaw: js.Any) => {
      val json = jsonRaw.asInstanceOf[GraphJson]

      val newNodes = json.nodes
        .map(n => new GraphNode(n.name, n.id, n.weight))
      val newLinks = json.links
        .map(l => new GraphLink(newNodes(l.source), newNodes(l.target)))

      force
        .nodes(newNodes)
        .links(newLinks)
        .start()

      val link = svg.selectAll[GraphLink](".link")
        .data(newLinks)
        .enter().append("line")
        .attr("class", "link")

      link.attr("stroke-width", mkFunction(link, (l: GraphLink, i: Int) => 1))

      val node: Selection[GraphNode] = svg.selectAll[GraphNode](".node")
        .data(newNodes)
        .enter().append("circle")
        .attr("class", "node")
        .call(force.drag)

      node.attr("r", mkFunction(node, (n: GraphNode, i) => n.weight))

      node.append("title").text(mkFunction(node, (n: GraphNode, i) => n.name))

      force.on("tick", (e: dom.Event) => {
        link
          .attr("x1", mkFunction(link, (n: GraphLink, i) => n.source.x))
          .attr("y2", mkFunction(link, (n: GraphLink, i) => n.source.y))
          .attr("x1", mkFunction(link, (n: GraphLink, i) => n.target.x))
          .attr("y2", mkFunction(link, (n: GraphLink, i) => n.target.y))

        node
          .attr("cx", mkFunction(node, (n: GraphNode, i) => n.x))
          .attr("cy", mkFunction(node, (n: GraphNode, i) => n.y))

        ()
      })

      ()
    })
  }

  private def mkFunction[T, R](sel: Selection[T], f: (T, Int) => R) = {
    new scala.scalajs.js.Function3[T, Int, scala.scalajs.js.UndefOr[Int], scala.scalajs.js.|[scala.scalajs.js.|[Double, String], Boolean]] {
      override def apply(t: T, i: Int, j: UndefOr[Int]) = f(t, i).asInstanceOf[d3.Primitive]
    }.asInstanceOf[sel.DatumFunction[d3.Primitive]]
  }
}
