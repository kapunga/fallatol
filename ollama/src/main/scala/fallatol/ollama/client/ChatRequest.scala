package fallatol.ollama.client

import fallatol.ollama.{ Message, Model }
import io.circe.Codec

/** https://github.com/ollama/ollama/blob/main/docs/api.md#generate-a-chat-completion
  */
case class ChatRequest(
    model: Model,
    messages: Seq[Message],
    stream: Boolean = false
)

object ChatRequest {
  implicit val codec: Codec[ChatRequest] = ChatRequestCodec.codec
}
