package bandhierarchy.analysis

import bandhierarchy._
import bandhierarchy.retriever.GigRetriever

object GigGraphCreator {

  private val defaultDepth = conf.getInt("depth")

  /**
    * Get the graph for a band
    * @param band the band to start with
    * @param depth the depth of the graph
    * @return the graph
    */
  def run(band: Band, depth: Int = defaultDepth): GigGraph = depth match {
    case 0 => Map()
    case n =>
      supports(band) match {
        case Seq() => Map()
        case sup =>
          val rest = sup
            .map(run(_, depth - 1))
            .reduce(_ ++ _)

          rest + (band -> sup)
      }
  }

  /**
    * Get who supports a band
    */
  private def supports(band: Band): Seq[Band] = {
    (GigRetriever run band)
      .filter(_.main == band)
      .flatMap(_.support)
  }
}
