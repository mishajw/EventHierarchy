package bandhierarchy.front.controller

import bandhierarchy.front.view.Graph
import bandhierarchy.front.view.Graph.{GraphLink, GraphNode}
import org.singlespaced.d3js.d3

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport

object Main extends js.JSApp {

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

  @JSExport
  override def main(): Unit = {
    d3.json("/target/graph.json", (error: js.Any, jsonRaw: js.Any) => {
      val json = jsonRaw.asInstanceOf[GraphJson]

      val nodes = json.nodes
        .map(n => new GraphNode(n.name, n.id, n.weight))
      val links = json.links
        .map(l => new GraphLink(nodes(l.source), nodes(l.target)))

      Graph.start(nodes, links)

      ()
    })
  }
}
