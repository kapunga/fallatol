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
