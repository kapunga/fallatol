package fallatol.config

import org.scalatest.Assertions.fail

trait TestHelpers {
  def expectConfigError(result: ConfigResult[Any]): Unit = {
    result match {
      case Left(ConfigError.UnexpectedValue(_)) => ()
      case Left(err)    => fail("Failed with unexpected error.", err)
      case Right(value) => fail(s"Succeeded with unexpected value: $value")
    }
  }
}
