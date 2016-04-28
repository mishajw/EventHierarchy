package bandhierarchy.manipulation

import bandhierarchy.WeightedGigGraph
import org.json4s.JsonAST.JValue
import org.json4s.JsonDSL._

object D3GraphExporter {
  def toJson(g: WeightedGigGraph): JValue = {
    val gSeq = g.toSeq
    val gBandSeq = gSeq map { case (b, _) => b }

    ("nodes" ->
      gSeq.map { case (b, (w, s)) =>
        ("name" -> b.name) ~
          ("id" -> b.id) ~
          ("weight" -> w)
      }) ~
    ("links" ->
      g.map { case (b, (w, sup)) =>
        sup.map { s =>
          ("source" -> (gBandSeq indexOf b)) ~
          ("distination" -> (gBandSeq indexOf s))
        }
      })
  }
}
