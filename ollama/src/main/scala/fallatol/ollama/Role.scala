package fallatol.ollama

import io.circe._
import io.circe.syntax.EncoderOps

sealed trait Role {
  def name: String
}

object Role {
  case object System extends Role { val name = "system" }
  case object User extends Role { val name = "user" }
  case object Assistant extends Role { val name = "assistant" }
  case object Tool extends Role { val name = "tool" }

  val values: Set[Role] = Set(System, User, Assistant, Tool)

  private lazy val valueMap: Map[String, Role] =
    Role.values.map(r => r.name -> r).toMap

  implicit val roleEncoder: Encoder[Role] = (r: Role) => r.name.asJson

  implicit val roleDecoder: Decoder[Role] = (c: HCursor) =>
    c.as[String]
      .flatMap(r =>
        valueMap.get(r).toRight(DecodingFailure(s"Invalid Role $r", c.history))
      )
}
