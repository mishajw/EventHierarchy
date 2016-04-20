package bandhierarchy

import bandhierarchy.analysis.GigGraphCreator
import bandhierarchy.retriever.BandRetriever

object BandHierarchy {
  def main(args: Array[String]) {
    val name = "LCD Soundsystem"

    BandRetriever run name match {
      case Some(band) =>
        val graph = GigGraphCreator run band
        println(graph .mkString("\n"))
      case None =>
        println(s"Couldn't find band $name")
    }
  }
}
