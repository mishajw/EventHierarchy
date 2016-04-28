import com.typesafe.config.ConfigFactory

import scala.collection.immutable.HashMap

package object bandhierarchy {
  case class Band(name: String, id: Int)
  case class Gig(main: Band, support: Seq[Band])

  type GigGraph = HashMap[Band, Set[Band]]

  type Weight = Double
  type WeightedGigGraph = HashMap[Band, (Weight, Set[Band])]

  val conf = ConfigFactory.load()
}
