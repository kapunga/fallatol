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

import sttp.client4.circe.asJsonEither
import sttp.client4.{ UriContext, basicRequest }
import sttp.model.HeaderNames.Authorization
import sttp.model.{ MediaType, Uri }

class Kagi(baseUri: Uri, token: String) {
  private val kagiUris = new KagiUris(baseUri)

  def search(query: String, limit: Int): KagiSearchRequest =
    basicRequest
      .header(Authorization, s"Bot $token")
      .contentType(MediaType.ApplicationJson)
      .get(uri"${kagiUris.Search}?q=$query&limit=$limit")
      .response(asJsonEither[SearchResponse.Failure, SearchResponse.Success])
  def enrichNews(query: String, limit: Int): KagiSearchRequest =
    basicRequest
      .header(Authorization, s"Bot $token")
      .contentType(MediaType.ApplicationJson)
      .get(uri"${kagiUris.News}?q=$query&limit=$limit")
      .response(asJsonEither[SearchResponse.Failure, SearchResponse.Success])
  def enrichWeb(query: String, limit: Int): KagiSearchRequest =
    basicRequest
      .header(Authorization, s"Bot $token")
      .contentType(MediaType.ApplicationJson)
      .get(uri"${kagiUris.Web}?q=$query&limit=$limit")
      .response(asJsonEither[SearchResponse.Failure, SearchResponse.Success])
}

private class KagiUris(val baseUri: Uri) {
  val Search = uri"$baseUri/api/v0/search"
  val News = uri"$baseUri/api/v0/enrich/news"
  val Web = uri"$baseUri/api/v0/enrich/web"
}
