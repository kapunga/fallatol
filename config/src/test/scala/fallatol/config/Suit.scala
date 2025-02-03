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

package fallatol.config

sealed trait Suit {
  def name: String
}
case object Heart extends Suit { val name = "heart" }
case object Diamond extends Suit { val name = "diamond" }
case object Club extends Suit { val name = "club" }
case object Spades extends Suit { val name = "spades" }

object Suit {
  implicit val suitConfigMapper: ConfigMapper[Suit] =
    ConfigMapper.fromString(s =>
      fromString(s).toRight(new IllegalArgumentException(s"Invalid Suit: $s"))
    )

  val all: Seq[Suit] = Heart :: Diamond :: Club :: Spades :: Nil

  def fromString(str: String): Option[Suit] = all.find(_.name == str)
}
