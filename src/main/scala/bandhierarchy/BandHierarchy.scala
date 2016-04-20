package bandhierarchy

import bandhierarchy.retriever.{BandRetriever, GigRetriever}

object BandHierarchy {
  def main(args: Array[String]) {
    val name = "LCD"

    BandRetriever run name match {
      case Some(band) =>
        val gigs = GigRetriever run band
        println(gigs.mkString("\n"))
      case None =>
        println(s"Couldn't find band $name")
    }
  }
}
