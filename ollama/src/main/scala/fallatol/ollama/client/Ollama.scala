package fallatol.ollama.client

import sttp.client4._
import sttp.client4.circe._
import sttp.model.Uri

class Ollama(baseUri: Uri) {
  private val ollamaUris = new OllamaUris(baseUri)

  def chat(request: ChatRequest): OllamaRequest[ChatResponse] =
    basicRequest
      .post(ollamaUris.Chat)
      .body(request)
      .response(asJson[ChatResponse])
}

private class OllamaUris(val baseUri: Uri) {
  val Chat = uri"$baseUri/api/chat"
}
