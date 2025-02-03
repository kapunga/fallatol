package fallatol.ollama.client

import fallatol.ollama.Model
import io.circe.Codec

case class EmbedResponse(
    model: Model,
    embeddings: Seq[Seq[Double]],
    totalDuration: Long,
    loadDuration: Long,
    promptEvalCount: Long
)

object EmbedResponse {
  implicit val codec: Codec[EmbedResponse] = EmbedResponseCodec.codec
}
