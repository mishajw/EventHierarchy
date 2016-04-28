package bandhierarchy

import bandhierarchy.manipulation.{GigGraphCreator, GigGraphWeighter}
import bandhierarchy.retriever.BandRetriever

object BandHierarchy {
  def main(args: Array[String]) {
    val name = "LCD Soundsystem"

    BandRetriever run name match {
      case Some(band) =>
        println("Getting graph")
        val graph = GigGraphCreator run band
        println("Applying weights")
        val weighted = GigGraphWeighter run graph
        println("Done")
        println(weighted.mkString("\n"))

      case None =>
        println(s"Couldn't find band $name")
    }
  }
}
