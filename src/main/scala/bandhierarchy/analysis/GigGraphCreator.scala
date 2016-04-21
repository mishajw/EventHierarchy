package bandhierarchy.analysis

import bandhierarchy._
import bandhierarchy.retriever.GigRetriever

import scala.collection.mutable

object GigGraphCreator {

  private val defaultDepth = conf.getInt("depth")

  /**
    * Get the graph for a band
    * @param band the band to start with
    * @return the graph
    */
  def run(band: Band): GigGraph = {
    val visited = mutable.Set[Band](band)

    def traverse(band: Band, depth: Int): GigGraph = depth match {
      case 0 => Map()
      case _ =>
        supports(band).filterNot(visited.contains) match {
          case Seq() => Map()
          case sup =>
            val rest = sup
              .map(traverse(_, depth - 1))
              .reduce(_ ++ _)

            visited ++= sup

            rest + (band -> sup)
        }
    }

    traverse(band, defaultDepth)
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
