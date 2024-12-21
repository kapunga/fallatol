package fallatol.tools

import org.ekrich.config.{Config, ConfigFactory}
import org.scalatest.flatspec.AnyFlatSpec


class BasicConfigSuite extends AnyFlatSpec {
  val basicConfig: String =
    """
      |{
      |  test_string = "foobar"
      |  test_int = 42
      |  test_long = 1234567890
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

  "Int config field" should "parse correctly" in {
    val result = config.get[Int]("test_int")

    assert(result == Right(42))
  }

  "Long config field" should "parse correctly" in {
    val result = config.get[Long]("test_long")

    assert(result == Right(1234567890L))
  }

  "Double config field" should "parse correctly" in {
    val result = config.get[Double]("test_double")

    assert(result == Right(1.234))
  }

  "Number config field" should "parse correctly" in {
    val result = config.get[Number]("test_double")

    result match {
      case Right(num) => assert(num.doubleValue() == 1.234)
      case Left(err)  => fail(err)
    }
  }

  "Boolean config field" should "parse correctly" in {
    val result = config.get[Boolean]("test_boolean")

    assert(result == Right(true))
  }

  "Config config field" should "parse correctly" in {
    val result = config.get[Config]("test_config")

    assert(result.flatMap(_.get[String]("foo")) == Right("bar"))
    assert(result.flatMap(_.get[Int]("meaning_of_life")) == Right(42))
  }
}
