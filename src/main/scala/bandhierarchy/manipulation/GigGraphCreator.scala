package bandhierarchy.manipulation

import bandhierarchy._
import bandhierarchy.retriever.GigRetriever

import scala.Predef
import scala.collection.immutable.{HashSet, Set, HashMap}
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

    def traverse(band: Band, depth: Int): Seq[GigGraph] = depth match {
      case 0 => Seq()
      case _ =>
        val thisGraph = graphForBand(band)
        val bands = bandsFromGraph(thisGraph)
        val newBands = bands diff visited

        val otherGraphs = newBands
          .flatMap(traverse(_, depth - 1))
          .toSeq

        visited ++= newBands

        otherGraphs :+ thisGraph
    }

    traverse(band, defaultDepth) reduce combine
  }

  /**
    * Get who supports a band
    */
  private def graphForBand(band: Band): GigGraph = {
    val (main, sup) = GigRetriever run band partition (_.main == band)

    HashMap(sup.map(_.main).map(_ -> Set(band)): _*) +
      (band -> main.flatMap(_.support).toSet)
  }

  private def combine[A](g1: GigGraph, g2: GigGraph): GigGraph = {
    g1.merged(g2){ case ((k, v1), (_, v2)) =>
      (k, v1 ++ v2)
    }
  }

  private def bandsFromGraph(graph: GigGraph): Set[Band] = {
    (graph.keys ++ graph.values.flatten).toSet
  }
}
