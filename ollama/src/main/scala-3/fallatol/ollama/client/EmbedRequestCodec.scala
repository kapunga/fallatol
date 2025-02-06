package fallatol.ollama.client

import io.circe.Codec
import io.circe.Codec.AsObject.derivedConfigured
import io.circe.derivation.Configuration

object EmbedRequestCodec:
  given Configuration = Configuration.default.withSnakeCaseMemberNames
  val codec: Codec[EmbedRequest] = derivedConfigured[EmbedRequest]
