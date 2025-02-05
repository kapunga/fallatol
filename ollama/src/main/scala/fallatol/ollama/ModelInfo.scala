package fallatol.ollama

import io.circe.Codec
import java.time.Instant



case class ModelDetails(parentModel: Option[Model], format: String, family: String, parameterSize: String, quantizationLevel: String)

object ModelDetails {
  implicit val codec: Codec[ModelDetails] = ModelDetailsCodec.codec
}

case class ModelInfo(model: Model, modifiedAt: Instant, size: Long, digest: String, details: ModelDetails)

object ModelInfo {
  implicit val codec: Codec[ModelInfo] = ModelInfoCodec.codec
}
