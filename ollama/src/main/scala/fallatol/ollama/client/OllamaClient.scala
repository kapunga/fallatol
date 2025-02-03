/*
 * Copyright (c) 2024 Paul (Thor) Thordarson
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package fallatol.ollama.client

import sttp.client4._
import sttp.model.Uri

trait OllamaClient[F[_]] {
  def chat(request: ChatRequest): F[ChatResponse]
  def embed(request: EmbedRequest): F[EmbedResponse]
}

class SyncOllamaClient(baseUri: Uri, backend: SyncBackend)
    extends OllamaClient[SyncType] {
  private val ollama: Ollama = new Ollama(baseUri)

  override def chat(request: ChatRequest): SyncType[ChatResponse] =
    ollama.chat(request).send(backend).body

  override def embed(request: EmbedRequest): SyncType[EmbedResponse] =
    ollama.embed(request).send(backend).body
}

object SyncOllamaClient {
  def apply(): SyncOllamaClient =
    new SyncOllamaClient(uri"http://localhost:11434", DefaultSyncBackend())
}
