package bandhierarchy.analysis

import bandhierarchy._
import bandhierarchy.retriever.GigRetriever

import scala.collection.immutable.{Set, HashMap}
import scala.collection.mutable

object GigGraphCreator {

  private val defaultDepth = conf.getInt("depth")

  /**
    * Get the graph for a band
 *
    * @param band the band to start with
    * @return the graph
    */
  def run(band: Band): GigGraph = {
    val visited = mutable.Set[Band](band)

    def traverse(band: Band, depth: Int): GigGraph = depth match {
      case 0 => HashMap()
      case _ =>
        supports(band) diff visited match {
          case sup if sup.isEmpty => HashMap()
          case sup =>
            val rest = sup
              .map(traverse(_, depth - 1))
              .reduce(combine)

            visited ++= sup

            rest + (band -> sup)
        }
    }

    traverse(band, defaultDepth)
  }

  /**
    * Get who supports a band
    */
  private def supports(band: Band): Set[Band] = {
    (GigRetriever run band)
      .filter(_.main == band)
      .flatMap(_.support)
      .toSet
  }

  private def combine[A](g1: GigGraph, g2: GigGraph): GigGraph = {
    g1.merged(g2){ case ((k, v1), (_, v2)) =>
      (k, v1 ++ v2)
    }
  }
}
