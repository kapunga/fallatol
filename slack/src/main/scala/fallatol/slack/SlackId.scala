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

package fallatol.slack

import scala.reflect.ClassTag

import io.circe.{ Codec, Decoder, Encoder }

sealed trait SlackId {
  def id: String
}

sealed trait TopLevelTeamId extends SlackId

object SlackId {
  implicit def slackIdCodec[I <: SlackId](implicit ct: ClassTag[I]): Codec[I] =
    Codec.from(
      Decoder.decodeString.flatMap(raw => {
        val slackId = SlackId.fromString(raw)

        if (ct.runtimeClass.isInstance(slackId))
          Decoder.const(slackId.asInstanceOf[I])
        else
          Decoder.failedWithMessage(
            s"Expected ${ct.runtimeClass.getSimpleName} but got $slackId - $raw"
          )
      }),
      Encoder.encodeString.contramap(_.id)
    )

  case class App(id: String) extends SlackId
  case class Enterprise(id: String) extends TopLevelTeamId
  case class Event(id: String) extends SlackId
  case class Team(id: String) extends TopLevelTeamId
  case class User(id: String) extends SlackId
  case class Unknown(id: String) extends SlackId

  private def fromString(raw: String): SlackId = raw match {
    case id if id.startsWith("A")  => App(id)
    case id if id.startsWith("E")  => Enterprise(id)
    case id if id.startsWith("Ev") => Event(id)
    case id if id.startsWith("T")  => Team(id)
    case id if id.startsWith("U")  => User(id)
    case id if id.startsWith("W")  => User(id)
    case _                         => Unknown(raw)
  }
}
