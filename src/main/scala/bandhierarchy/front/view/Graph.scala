package bandhierarchy.front.view

import org.scalajs.dom
import org.singlespaced.d3js.forceModule.Node
import org.singlespaced.d3js.{Link, d3}

import scala.scalajs.js

class GraphNode(val name: String, val id: Int, private val _weight: Double) extends Node {
  weight = _weight
}

class GraphLink(_source: GraphNode, _target: GraphNode) extends Link[GraphNode] {
  override def source: GraphNode = _source
  override def target: GraphNode = _target
}

class Graph(nodes: js.Array[GraphNode], links: js.Array[GraphLink]) {
  val width: Double = 600
  val height: Double = 400

  val svg = d3.select("#graph")
    .append("svg")
    .attr("width", width)
    .attr("height", height)

  val force = {
    d3.layout.force[GraphNode, GraphLink]()
      .charge(-120)
      .linkDistance(30)
      .size((width, height))
      .nodes(nodes)
      .links(links)
      .start()
  }

  val d3Links = {
    svg.selectAll[GraphLink](".link")
      .data(links)
      .enter().append("line")
      .attr("class", "link")
      .attr("stroke", "#BBB")
      .attr("stroke-width", 1)
  }

  val d3NodeGroups = {
    svg.selectAll("g.gnode")
      .data(nodes)
      .enter().append("g")
      .classed("gnode", value = true)
  }

  val d3Nodes = {
    val n = d3NodeGroups.append("circle")
      .attr("class", "node")
      .attr("fill", "#CCC")
      .attr("stroke", "#333")
      .call(force.drag)

    n.attr("r", mkFunction(n, (n: GraphNode, i) => n.weight))
  }

  val d3Titles = {
    val t = d3NodeGroups.append("text")

    t.text(mkFunction(t, (n: GraphNode, i) => n.name))
    t.style("font-size", mkFunction(t, (n: GraphNode, i) => n.weight.get * 2 + "px"))
  }

  force.on("tick", (e: dom.Event) => {
    d3Links
      .attr("x1", mkFunction(d3Links, (n: GraphLink, i) => n.source.x))
      .attr("y1", mkFunction(d3Links, (n: GraphLink, i) => n.source.y))
      .attr("x2", mkFunction(d3Links, (n: GraphLink, i) => n.target.x))
      .attr("y2", mkFunction(d3Links, (n: GraphLink, i) => n.target.y))

    d3Nodes
      .attr("cx", mkFunction(d3Nodes, (n: GraphNode, i) => n.x))
      .attr("cy", mkFunction(d3Nodes, (n: GraphNode, i) => n.y))

    d3Titles
      .attr("x", mkFunction(d3Nodes, (n: GraphNode, i) => n.x))
      .attr("y", mkFunction(d3Nodes, (n: GraphNode, i) => n.y))

    ()
  })
}

object Graph {
  def apply(n: js.Array[GraphNode], l: js.Array[GraphLink]) = new Graph(n, l)
}
