package fallatol.ollama.client

import sttp.client4._
import sttp.model.Uri

trait OllamaClient[F[_]] {
  def chat(request: ChatRequest): F[ChatResponse]
}

class SyncOllamaClient(baseUri: Uri, backend: SyncBackend)
    extends OllamaClient[SyncType] {
  private val ollama: Ollama = new Ollama(baseUri)

  override def chat(request: ChatRequest): SyncType[ChatResponse] =
    ollama.chat(request).send(backend).body
}

object SyncOllamaClient {
  def apply(): SyncOllamaClient =
    new SyncOllamaClient(uri"http://localhost:11434", DefaultSyncBackend())
}
