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

import fallatol.config.ConfigMapper
import io.circe.syntax.EncoderOps
import io.circe.{ Decoder, Encoder, HCursor }

trait Model {
  def name: String
}

object Model {
  case object DeepseekR1 extends Model {
    override def name: String = "deepseek-r1"
  }

  case object Gemma2 extends Model {
    override val name = "gemma2"
  }

  case object Hermes3 extends Model {
    override val name = "hermes3"
  }

  case object Llama3_2 extends Model {
    override val name = "llama3.2"
  }

  case object Mistral extends Model {
    override val name = "mistral"
  }

  case object Phi4 extends Model {
    override val name = "phi4"
  }

  case object TinyLlama extends Model {
    override val name = "tinyllama"
  }

  case class Custom(name: String) extends Model

  def fromString(name: String): Model = name match {
    case Gemma2.name    => Gemma2
    case Hermes3.name   => Hermes3
    case Llama3_2.name  => Llama3_2
    case Mistral.name   => Mistral
    case Phi4.name      => Phi4
    case TinyLlama.name => TinyLlama
    case customName     => Custom(customName)
  }

  implicit val modelConfigMapper: ConfigMapper[Model] =
    ConfigMapper.from[String, Model](c => Right(fromString(c)))

  implicit val decoder: Decoder[Model] =
    (c: HCursor) => c.as[String].map(fromString)

  implicit val encoder: Encoder[Model] =
    (m: Model) => m.name.asJson
}
