import com.typesafe.config.ConfigFactory

package object bandhierarchy {
  case class Band(name: String, id: Int)
  case class Gig(main: Band, support: Seq[Band])

  type GigGraph = Map[Band, Seq[Band]]
  type GigTree = (Band, Seq[GigTree])

  val conf = ConfigFactory.load()
}
