package fallatol.ollama

import io.circe.Codec
import io.circe.Codec.AsObject.derivedConfigured
import io.circe.derivation.Configuration

object ModelDetailsCodec:
  given Configuration = Configuration.default.withSnakeCaseMemberNames
  val codec: Codec[ModelDetails] = derivedConfigured[ModelDetails]