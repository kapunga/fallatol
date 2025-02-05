package fallatol.ollama

import io.circe.Codec
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveConfiguredCodec

object ModelDetailsCodec {
  implicit val customConfig: Configuration =
    Configuration.default.withSnakeCaseMemberNames
  val codec: Codec[ModelDetails] = deriveConfiguredCodec
}
