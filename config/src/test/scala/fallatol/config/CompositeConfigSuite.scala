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

import cats.implicits.toTraverseOps
import org.ekrich.config.{ Config, ConfigFactory }
import org.scalatest.flatspec.AnyFlatSpec

class CompositeConfigSuite extends AnyFlatSpec with TestHelpers {
  val compositeConfig: String =
    """
      |{
      |  test_person = {
      |    name = "Bilbo"
      |    age = 111
      |  }
      |  test_string_option_a = "foobar"
      |  test_string_option_b = null
      |  test_int_array = [1, 2, 3]
      |  test_config_array = [{
      |      num = 1
      |    }, {
      |      num = 2
      |    }, {
      |      num = 3
      |    }
      |  ]
      |  test_config_map {
      |    foo = 1
      |    bar = 2
      |    baz = 3
      |  }
      |  test_config_person_array = [{
      |    name = "Alice"
      |    age = 37
      |  }, {
      |    name = "Bob"
      |    age = 42
      |  }]
      |  trump = "spades"
      |  typo_suite = "heat"
      |}
      |""".stripMargin

  lazy val config: Config = ConfigFactory.parseString(compositeConfig)

  "Case class" should "parse correctly" in {
    val result = config.get[Person]("test_person")

    assert(result == Right(Person("Bilbo", 111)))
  }

  "Optional Config Some" should "parse as Some" in {
    val result = config.get[Option[String]]("test_string_option_a")

    assert(result == Right(Some("foobar")))
  }

  "Optional Config Null" should "parse as None" in {
    val result = config.get[Option[String]]("test_string_option_b")

    assert(result == Right(None))
  }

  "Optional Config empty" should "parse as None" in {
    val result = config.get[Option[String]]("test_string_option_c")

    assert(result == Right(None))
  }

  "Config Array" should "parse correctly" in {
    val result = config.get[List[Int]]("test_int_array")

    assert(result == Right(List(1, 2, 3)))
  }

  "Config Array of case classes" should "parse correctly" in {
    val result = config.get[List[Person]]("test_config_person_array")

    assert(result == Right(List(Person("Alice", 37), Person("Bob", 42))))
  }

  "Config Array of Configs" should "parse correctly" in {
    val result = config
      .get[List[Config]]("test_config_array")
      .flatMap(_.traverse(_.get[Int]("num")))

    assert(result == Right(List(1, 2, 3)))
  }

  "Config Array" should "result in error on incorrect type" in {
    val result = config.get[List[Int]]("test_string_option_a")

    expectConfigError(result)
  }

  "Config Map" should "parse correctly" in {
    val result = config.get[Map[String, Int]]("test_config_map")

    assert(
      result == Right(
        Map(
          "foo" -> 1,
          "bar" -> 2,
          "baz" -> 3
        )
      )
    )
  }

  "String mapped ConfigMapper" should "parse correctly" in {
    val result = config.get[Suit]("trump")

    assert(result == Right(Spades))
  }

  "String mapped ConfigMapper" should "raise error on parse error" in {
    val result = config.get[Suit]("typo_suite")

    expectIllegalArgumentException(result)
  }
}
