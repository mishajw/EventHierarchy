package object bandhierarchy {
  case class Band(name: String, id: Int)
  case class Gig(main: Band, support: Seq[Band])
}
