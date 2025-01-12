package fallatol.ollama.client

import java.time.Instant

import fallatol.ollama.{ Message, Model }
import io.circe.Codec

/** https://github.com/ollama/ollama/blob/main/docs/api.md#generate-a-chat-completion
  */
case class ChatResponse(
    model: Model,
    createdAt: Instant,
    message: Message,
    done: Boolean,
    totalDuration: Option[Long],
    loadDuration: Option[Long],
    promptEvalCount: Option[Long],
    promptEvalDuration: Option[Long],
    evalCount: Option[Long],
    evalDuration: Option[Long]
)

object ChatResponse {
  implicit val codec: Codec[ChatResponse] = ChatResponseCodec.codec
}
