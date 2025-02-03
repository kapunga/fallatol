package fallatol.ollama.client

import sttp.client4._
import sttp.client4.circe._
import sttp.model.Uri

class Ollama(baseUri: Uri) {
  private val ollamaUris = new OllamaUris(baseUri)

  def chat(request: ChatRequest): OllamaRequest[ChatResponse] =
    basicRequest
      .post(ollamaUris.Chat)
      .body(asJson(request))
      .response(asJson[ChatResponse])

  def embed(request: EmbedRequest): OllamaRequest[EmbedResponse] =
    basicRequest
      .post(ollamaUris.Embed)
      .body(asJson(request))
      .response(asJson[EmbedResponse])
}

private class OllamaUris(val baseUri: Uri) {
  val Chat = uri"$baseUri/api/chat"
  val Embed = uri"$baseUri/api/embed"
}
