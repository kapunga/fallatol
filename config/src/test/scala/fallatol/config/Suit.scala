package fallatol.config

sealed trait Suit {
  def name: String
}
case object Heart extends Suit { val name = "heart" }
case object Diamond extends Suit { val name = "diamond" }
case object Club extends Suit { val name = "club" }
case object Spades extends Suit { val name = "spades" }

object Suit {
  implicit val suitConfigMapper: ConfigMapper[Suit] =
    ConfigMapper.fromString(s =>
      fromString(s).toRight(new IllegalArgumentException(s"Invalid Suit: $s"))
    )

  val all: Seq[Suit] = Heart :: Diamond :: Club :: Spades :: Nil

  def fromString(str: String): Option[Suit] = all.find(_.name == str)
}
