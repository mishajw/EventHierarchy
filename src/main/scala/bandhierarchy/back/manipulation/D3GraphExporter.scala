package bandhierarchy.back.manipulation

import bandhierarchy.back.{Band, Weight, WeightedGigGraph}
import org.json4s.JsonAST.JValue
import org.json4s.JsonDSL._

object D3GraphExporter {
  def toJson(g: WeightedGigGraph): JValue = {
    val bands: Seq[Band] = (g.keys ++ g.values.flatMap(_._2)).toSet.toSeq

    ("nodes" ->
      bands.map { b =>
        ("name" -> b.name) ~
        ("id" -> b.id) ~
        ("weight" -> weight(b, g))
      }) ~
    ("links" ->
      g.flatMap { case (b, (w, sup)) =>
        sup.map { s =>
          ("source" -> (bands indexOf b)) ~
          ("target" -> (bands indexOf s))
        }
      })
  }

  private def weight(b: Band, g: WeightedGigGraph): Weight = {
    g contains b match {
      case false => 0
      case true => g(b) match { case (w, _) => w }
    }
  }
}
