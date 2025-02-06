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

package fallatol.ollama

import io.circe._
import io.circe.syntax.EncoderOps

sealed trait Role {
  def name: String
}

object Role {
  case object System extends Role { val name = "system" }
  case object User extends Role { val name = "user" }
  case object Assistant extends Role { val name = "assistant" }
  case object Tool extends Role { val name = "tool" }

  val values: Set[Role] = Set(System, User, Assistant, Tool)

  private lazy val valueMap: Map[String, Role] =
    Role.values.map(r => r.name -> r).toMap

  implicit val roleEncoder: Encoder[Role] = (r: Role) => r.name.asJson

  implicit val roleDecoder: Decoder[Role] = (c: HCursor) =>
    c.as[String]
      .flatMap(r =>
        valueMap.get(r).toRight(DecodingFailure(s"Invalid Role $r", c.history))
      )
}
