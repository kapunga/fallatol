package fallatol.ollama

import io.circe.Codec
import io.circe.Codec.AsObject.derivedConfigured
import io.circe.derivation.Configuration

object ModelInfoCodec:
  given Configuration = Configuration.default.withSnakeCaseMemberNames
  val codec: Codec[ModelInfo] = derivedConfigured[ModelInfo]
