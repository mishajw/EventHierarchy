package bandhierarchy.front

import org.scalajs.dom
import org.scalajs.dom.{Selection, raw}
import org.singlespaced.d3js.d3
import org.singlespaced.d3js.histogramModule.Bin

import scala.scalajs.js
import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport

object Graph extends JSApp {
  @js.native
  trait GraphJson extends js.Object {
    val nodes: js.Array[GraphNode] = js.native
    val links: js.Array[GraphNode] = js.native
  }

  @js.native
  trait GraphNode extends js.Object {
    val name: String = js.native
    val id: Int = js.native
    val weight: Double = js.native
  }

  @js.native
  trait GraphLink extends js.Object {
    val source: Int = js.native
    val destination: Int = js.native
  }

  val width: Double = 600
  val height: Double = 400

  val force = d3.layout.force()
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

      force
        .nodes(json.nodes.asInstanceOf[js.Array[Nothing]])
        .links(json.links.asInstanceOf[js.Array[Nothing]])
        .start()

      val link = svg.selectAll(".link")
        .data(json.links)
        .enter().append("line")
        .attr("class", "link")
        .style("stroke-width", 1)

      val node = svg.selectAll(".node")
        .data(json.nodes)
        .enter().append("circle")
        .attr("class", "node")
        .attr("r", 5)
        .call(force.drag)

      // node.append(title)...

      force.on("tick", (e: dom.Event) => {
        link
          .attr("x1", 0)
          .attr("y1", 0)
          .attr("x2", 0)
          .attr("y2", 0)

        node
          .attr("cx", 0)
          .attr("cy", 0)

        ()
      })

      ()
    })
  }
}
