package bandhierarchy.back.manipulation

import bandhierarchy.back._

object GigGraphWeighter {

  private val carryValue = 0.5
  private lazy val inverseCarryValue = 1 / carryValue
  private val baseValue = 1.0
  private val iterations = 10

  def run(g: GigGraph): WeightedGigGraph = {
    def f(wgg: WeightedGigGraph, i: Int = iterations): WeightedGigGraph = i match {
      case 0 => wgg
      case _ => smooth(f(wgg, i - 1))
    }

    f(default(g))
  }

  private def default(g: GigGraph): WeightedGigGraph = {
    g map { case (b, s) =>
      b -> (baseValue, s)
    }
  }

  private def smooth(implicit g: WeightedGigGraph): WeightedGigGraph = {
    g map { case (b, (w, s)) =>
      val allWeights =
        supporterWeights(b).map(_ * inverseCarryValue) ++
        supportedWeights(b).map(_ * carryValue)

      b -> ((allWeights.sum + w) / 2, s)
    }
  }

  private def supporterWeights(b: Band)(implicit g: WeightedGigGraph): Seq[Weight] = {
    g(b) match {
      case (w, s) => s.toSeq
        .withFilter(g contains)
        .map(g(_))
        .map { case (w, s) => w }
    }
  }

  private def supportedWeights(b: Band)(implicit g: WeightedGigGraph): Seq[Weight] = {
    g
      .withFilter { case (_, (_, s)) => s contains b }
      .map { case (_, (w, _)) => w }
      .toSeq
  }
}
