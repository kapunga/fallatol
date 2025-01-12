package fallatol.ollama

import fallatol.config.ConfigMapper
import io.circe.syntax.EncoderOps
import io.circe.{ Decoder, Encoder, HCursor }

trait Model {
  def name: String
}

object Model {
  case object Llama3_2 extends Model {
    override val name = "llama3.2"
  }

  case object Mistral extends Model {
    override val name = "mistral"
  }

  case class Custom(name: String) extends Model

  def fromString(name: String): Model = name match {
    case Llama3_2.name => Llama3_2
    case Mistral.name  => Mistral
    case customName    => Custom(customName)
  }

  implicit val modelConfigMapper: ConfigMapper[Model] =
    ConfigMapper.from[String, Model](c => Right(fromString(c)))

  implicit val decoder: Decoder[Model] =
    (c: HCursor) => c.as[String].map(fromString)

  implicit val encoder: Encoder[Model] =
    (m: Model) => m.name.asJson
}
