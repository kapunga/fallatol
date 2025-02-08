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

package fallatol

import java.util.concurrent.TimeUnit

import scala.concurrent.duration.{ Duration, FiniteDuration }

import io.circe._

package object ollama {

  /** Ollama uses Go duration formatting.
    * @see
    *   [[https://pkg.go.dev/time#ParseDuration]]
    */
  implicit val durationEncoder: Encoder[Duration] = (duration: Duration) => {
    def unitSuffix(unit: TimeUnit): String =
      unit match {
        case TimeUnit.NANOSECONDS  => "ns"
        case TimeUnit.MICROSECONDS => "µs"
        case TimeUnit.MILLISECONDS => "ms"
        case TimeUnit.SECONDS      => "s"
        case TimeUnit.MINUTES      => "m"
        case TimeUnit.HOURS        => "h"
        case TimeUnit.DAYS         => "d"
      }

    duration match {
      case fd: FiniteDuration =>
        if (fd.length == 0) Json.fromString("0")
        else Json.fromString(s"${fd.length}${unitSuffix(fd.unit)}")
      case dur => Json.fromString(dur.toString)
    }
  }

  implicit val durationDecoder: Decoder[Duration] = (c: HCursor) => {
    c.as[String]
      .flatMap(dur =>
        try {
          Right(Duration(dur))
        } catch {
          case _: NumberFormatException =>
            Left(DecodingFailure(s"Invalid Duration: $dur", c.history))
        }
      )
  }

  implicit val durationCodec: Codec[Duration] =
    Codec.from(durationDecoder, durationEncoder)
}
