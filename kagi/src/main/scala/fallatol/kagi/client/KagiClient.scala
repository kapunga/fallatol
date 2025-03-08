/*
 * Copyright (c) 2025 Paul (Thor) Thordarson
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

package fallatol.kagi.client

import sttp.client4.{ DefaultSyncBackend, SyncBackend, UriContext }
import sttp.model.Uri

trait KagiClient[F[_]] {
  def search(query: String, limit: Int): F[SearchResponse]

  def enrichNews(query: String, limit: Int): F[SearchResponse]

  def enrichWeb(query: String, limit: Int): F[SearchResponse]
}

class SyncKagiClient(baseUri: Uri, token: String, backend: SyncBackend)
    extends KagiClient[SyncType] {
  private val kagi: Kagi = new Kagi(baseUri, token)

  override def search(query: String, limit: Int): SyncType[SearchResponse] =
    kagi.search(query, limit).send(backend).body

  override def enrichNews(query: String, limit: Int): SyncType[SearchResponse] =
    kagi.enrichNews(query, limit).send(backend).body

  override def enrichWeb(query: String, limit: Int): SyncType[SearchResponse] =
    kagi.enrichWeb(query, limit).send(backend).body
}

object SyncKagiClient {
  def apply(accessToken: String): SyncKagiClient =
    new SyncKagiClient(uri"https://kagi.com", accessToken, DefaultSyncBackend())
}
