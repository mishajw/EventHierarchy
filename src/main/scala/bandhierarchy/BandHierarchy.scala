package bandhierarchy

import bandhierarchy.retriever.GigRetriever

object BandHierarchy {
  def main(args: Array[String]) {
    val startBand = Band("LCD Soundsystem", 241554)
    val gigs = GigRetriever run startBand

    println(gigs.mkString("\n"))
  }
}
