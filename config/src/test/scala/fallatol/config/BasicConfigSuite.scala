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

import org.ekrich.config.{ Config, ConfigFactory }
import org.scalatest.flatspec.AnyFlatSpec

class BasicConfigSuite extends AnyFlatSpec with TestHelpers {
  val basicConfig: String =
    """
      |{
      |  test_string = "foobar"
      |  test_string_2 = null
      |  test_int = 42
      |  test_long = 9876543210
      |  test_double = 1.234
      |  test_boolean = true
      |  test_config {
      |    foo = "bar"
      |    meaning_of_life = 42
      |  }
      |}
      |""".stripMargin

  lazy val config: Config = ConfigFactory.parseString(basicConfig)

  "String config field" should "parse correctly" in {
    val result = config.get[String]("test_string")

    assert(result == Right("foobar"))
  }

  "String config field" should "result in error on incorrect type" in {
    val result = config.get[String]("test_int")

    expectConfigError(result)
  }

  "Int config field" should "parse correctly" in {
    val result = config.get[Int]("test_int")

    assert(result == Right(42))
  }

  "Int config field" should "result in  error on incorrect type" in {
    val result = config.get[Int]("test_string")

    expectConfigError(result)
  }

  "Long config field" should "parse correctly" in {
    val result = config.get[Long]("test_long")

    assert(result == Right(9876543210L))
  }

  "Long config field" should "parse int value correctly" in {
    val result = config.get[Long]("test_int")

    assert(result == Right(42L))
  }

  "Long config field" should "result in  error on incorrect type" in {
    val result = config.get[Long]("test_string")

    expectConfigError(result)
  }

  "Double config field" should "parse correctly" in {
    val result = config.get[Double]("test_double")

    assert(result == Right(1.234))
  }

  "Double config field" should "result in  error on incorrect type" in {
    val result = config.get[Double]("test_string")

    expectConfigError(result)
  }

  "Number config field" should "parse correctly" in {
    val result = config.get[Number]("test_double")

    result match {
      case Right(num) => assert(num.doubleValue() == 1.234)
      case Left(err)  => fail(err)
    }
  }

  "Number config field" should "result in  error on incorrect type" in {
    val result = config.get[Number]("test_string")

    expectConfigError(result)
  }

  "Boolean config field" should "parse correctly" in {
    val result = config.get[Boolean]("test_boolean")

    assert(result == Right(true))
  }

  "Boolean config field" should "result in  error on incorrect type" in {
    val result = config.get[Boolean]("test_string")

    expectConfigError(result)
  }

  "Config config field" should "parse correctly" in {
    val result = config.get[Config]("test_config")

    assert(
      result.flatMap(_.get[String]("foo")) == Right("bar") &&
        result.flatMap(_.get[Int]("meaning_of_life")) == Right(42)
    )
  }

  "Config config field" should "result in  error on incorrect type" in {
    val result = config.get[Config]("test_string")

    expectConfigError(result)
  }

  "ConfigOps.getOrElse" should "get correctly when value is present" in {
    val result = config.getOrElse[String]("test_string", "baz")

    assert(result == Right("foobar"))
  }

  "ConfigOps.getOrElse" should "fallback to default when value is null" in {
    val result = config.getOrElse[String]("test_string_2", "baz")

    assert(result == Right("baz"))
  }

  "ConfigOps.getOrElse" should "fallback to default when value is absent" in {
    val result = config.getOrElse[String]("test_string_3", "baz")

    assert(result == Right("baz"))
  }
}
