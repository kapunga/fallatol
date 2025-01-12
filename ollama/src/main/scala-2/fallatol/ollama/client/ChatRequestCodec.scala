package fallatol.ollama.client

import io.circe.Codec
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveConfiguredCodec

object ChatRequestCodec {
  implicit val customConfig: Configuration =
    Configuration.default.withSnakeCaseMemberNames
  val codec: Codec[ChatRequest] = deriveConfiguredCodec
}
