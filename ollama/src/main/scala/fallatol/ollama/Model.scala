package fallatol.ollama

import fallatol.config.ConfigMapper

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
}
