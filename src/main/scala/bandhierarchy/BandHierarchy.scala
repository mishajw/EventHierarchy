package bandhierarchy

import bandhierarchy.manipulation.{D3GraphExporter, GigGraphCreator, GigGraphWeighter}
import bandhierarchy.retriever.BandRetriever
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
        println(s"JSON:\n${pretty(render(json))}")

      case None =>
        println(s"Couldn't find band $name")
    }
  }
}
