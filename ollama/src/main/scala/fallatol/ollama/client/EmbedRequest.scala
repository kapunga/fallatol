package fallatol.ollama.client

import fallatol.ollama.Model
import io.circe.Codec

case class EmbedRequest(model: Model, input: Seq[String])

object EmbedRequest {
  def apply(model: Model, input: String): EmbedRequest =
    EmbedRequest(model, Seq(input))

  implicit val codec: Codec[EmbedRequest] = EmbedRequestCodec.codec
}
