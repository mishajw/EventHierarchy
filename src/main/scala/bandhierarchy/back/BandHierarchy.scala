package bandhierarchy.back

import java.io.PrintWriter

import bandhierarchy.back.manipulation.{D3GraphExporter, GigGraphCreator, GigGraphWeighter}
import bandhierarchy.back.retriever.BandRetriever
import org.json4s.jackson.JsonMethods._

object BandHierarchy {
  def main(args: Array[String]) {
    val name = "LCD Soundsystem"

    BandRetriever run name match {
      case Some(band) =>
        println("Getting graph")
        val graph = GigGraphCreator run band

        println("Applying weights")
        val weighted = GigGraphWeighter run graph

        println("Converting to JSON")
        val json = D3GraphExporter toJson weighted

        val jsonText  = pretty(render(json))
        println(s"JSON:\n$jsonText")

        println("Writing to file")
        new PrintWriter("target/graph.json") { write(jsonText); close() }

      case None =>
        println(s"Couldn't find band $name")
    }
  }
}
