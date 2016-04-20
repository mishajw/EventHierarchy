package bandhierarchy.analysis

import bandhierarchy.Band
import bandhierarchy.retriever.GigRetriever

object GigGraphCreator {
  def run(band: Band, depth: Int = 1): Map[Band, Seq[Band]] = depth match {
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

  private def supports(band: Band): Seq[Band] = {
    (GigRetriever run band)
      .filter(_.main == band)
      .flatMap(_.support)
  }
}
