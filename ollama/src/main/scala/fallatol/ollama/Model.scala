package fallatol.ollama

import fallatol.config.ConfigMapper
import io.circe.syntax.EncoderOps
import io.circe.{ Decoder, Encoder, HCursor }

trait Model {
  def name: String
}

object Model {
  case object DeepseekR1 extends Model {
    override def name: String = "deepseek-r1"
  }

  case object Gemma2 extends Model {
    override val name = "gemma2"
  }

  case object Hermes3 extends Model {
    override val name = "hermes3"
  }

  case object Llama3_2 extends Model {
    override val name = "llama3.2"
  }

  case object Mistral extends Model {
    override val name = "mistral"
  }

  case object Phi4 extends Model {
    override val name = "phi4"
  }

  case object TinyLlama extends Model {
    override val name = "tinyllama"
  }

  case class Custom(name: String) extends Model

  def fromString(name: String): Model = name match {
    case Gemma2.name    => Gemma2
    case Hermes3.name   => Hermes3
    case Llama3_2.name  => Llama3_2
    case Mistral.name   => Mistral
    case Phi4.name      => Phi4
    case TinyLlama.name => TinyLlama
    case customName     => Custom(customName)
  }

  implicit val modelConfigMapper: ConfigMapper[Model] =
    ConfigMapper.from[String, Model](c => Right(fromString(c)))

  implicit val decoder: Decoder[Model] =
    (c: HCursor) => c.as[String].map(fromString)

  implicit val encoder: Encoder[Model] =
    (m: Model) => m.name.asJson
}
