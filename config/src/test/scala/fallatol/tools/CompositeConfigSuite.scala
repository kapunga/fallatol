package fallatol.tools

import cats.implicits.toTraverseOps
import org.ekrich.config.{Config, ConfigFactory}
import org.scalatest.flatspec.AnyFlatSpec

class CompositeConfigSuite extends AnyFlatSpec with TestHelpers {
  val compositeConfig: String =
    """
      |{
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
      |}
      |""".stripMargin

  lazy val config: Config = ConfigFactory.parseString(compositeConfig)

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
}
