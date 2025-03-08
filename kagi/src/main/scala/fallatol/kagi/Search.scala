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

package fallatol.kagi

import io.circe._
import io.circe.generic.semiauto.{ deriveCodec, deriveDecoder }
import io.circe.syntax.EncoderOps

sealed trait Search

object Search {
  case class Result(
      rank: Option[Int],
      url: String,
      title: String,
      snippet: Option[String],
      published: Option[String],
      thumbnail: Option[Image]
  ) extends Search
  case class Related(list: List[String]) extends Search

  case class Image(url: String, height: Option[Int], width: Option[Int])

  implicit val imageCodec: Codec[Image] = deriveCodec
  implicit val resultCodec: Codec[Result] = new Codec[Result] {
    val decoder: Decoder[Result] = deriveDecoder

    override def apply(c: HCursor): Decoder.Result[Result] = decoder(c)

    override def apply(a: Result): Json =
      Json
        .obj(
          "t" -> Json.fromInt(0),
          "rank" -> a.rank.fold(Json.Null)(Json.fromInt),
          "url" -> Json.fromString(a.url),
          "title" -> Json.fromString(a.title),
          "snippet" -> a.snippet.fold(Json.Null)(Json.fromString),
          "published" -> a.published.fold(Json.Null)(Json.fromString),
          "thumbnail" -> a.thumbnail.fold(Json.Null)(imageCodec.apply)
        )
        .dropNullValues
  }

  implicit val relatedCodec: Codec[Related] = new Codec[Related] {
    val decoder: Decoder[Related] = deriveDecoder

    override def apply(c: HCursor): Decoder.Result[Related] = decoder(c)

    override def apply(a: Related): Json =
      Json.obj(
        "t" -> Json.fromInt(1),
        "list" -> Json.fromValues(a.list.map(Json.fromString))
      )
  }

  implicit val searchCodec: Codec[Search] = new Codec[Search] {
    override def apply(c: HCursor): Decoder.Result[Search] =
      c.downField("t").as[Int].flatMap {
        case 0 => resultCodec(c)
        case 1 => relatedCodec(c)
        case t => Left(DecodingFailure(s"Invalid type: $t", c.history))
      }

    override def apply(s: Search): Json = s match {
      case r: Result  => r.asJson
      case r: Related => r.asJson
    }
  }
}
