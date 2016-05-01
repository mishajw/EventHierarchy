package bandhierarchy.front.view

import org.scalajs.dom
import org.singlespaced.d3js.forceModule.Node
import org.singlespaced.d3js.{Link, d3}
import org.singlespaced.d3js.Ops._

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
    d3NodeGroups.append("circle")
      .attr("class", "node")
      .attr("fill", "#CCC")
      .attr("stroke", "#333")
      .call(force.drag)
      .attr("r", (n: GraphNode, i: Int) => n.weight)
  }

  val d3Titles = {
    d3NodeGroups.append("text")
      .text((n: GraphNode, i: Int) => n.name)
      .style("font-size", (n: GraphNode, i: Int) => n.weight.get * 2 + "px")
  }

  force.on("tick", (e: dom.Event) => {
    d3Links
      .attr("x1", (n: GraphLink, i: Int) => n.source.x)
      .attr("y1", (n: GraphLink, i: Int) => n.source.y)
      .attr("x2", (n: GraphLink, i: Int) => n.target.x)
      .attr("y2", (n: GraphLink, i: Int) => n.target.y)

    d3Nodes
      .attr("cx", (n: GraphNode, i: Int) => n.x)
      .attr("cy", (n: GraphNode, i: Int) => n.y)

    d3Titles
      .attr("x", (n: GraphNode, i: Int) => n.x)
      .attr("y", (n: GraphNode, i: Int) => n.y)

    ()
  })
}

object Graph {
  def apply(n: js.Array[GraphNode], l: js.Array[GraphLink]) = new Graph(n, l)
}
