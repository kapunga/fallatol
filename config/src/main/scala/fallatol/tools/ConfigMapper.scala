package fallatol.tools

import org.ekrich.config.{Config, ConfigObject, ConfigValue}
import org.ekrich.config.impl.{ConfigBoolean, ConfigDouble, ConfigInt, ConfigLong, ConfigNumber, ConfigString}

trait ConfigMapper[A] {
  def get(cv: ConfigValue): Either[Throwable, A]
}

object ConfigMapper {
  def flatMapped[A, B](f: A => Either[Throwable, B])(implicit acm: ConfigMapper[A]): ConfigMapper[B] =
    cv => acm.get(cv).flatMap(f)

  implicit val stringConfigMapper: ConfigMapper[String] = {
    case cs: ConfigString => Right(cs.value)
    case cv => Left(new RuntimeException(s"Unexpected Value $cv"))
  }

  implicit val intConfigMapper: ConfigMapper[Int] = {
    case cs: ConfigInt => Right(cs.value)
    case cv => Left(new RuntimeException(s"Unexpected Value $cv"))
  }

  implicit val longConfigMapper: ConfigMapper[Long] = {
    case cs: ConfigLong => Right(cs.value)
    case cv => Left(new RuntimeException(s"Unexpected Value $cv"))
  }

  implicit val doubleConfigMapper: ConfigMapper[Double] = {
    case cs: ConfigDouble => Right(cs.value)
    case cv => Left(new RuntimeException(s"Unexpected Value $cv"))
  }

  implicit val numberConfigMapper: ConfigMapper[Number] = {
    case cs: ConfigNumber => Right(cs.unwrapped)
    case cv => Left(new RuntimeException(s"Unexpected Value $cv"))
  }

  implicit val booleanConfigMapper: ConfigMapper[Boolean] = {
    case cs: ConfigBoolean => Right(cs.value)
    case cv => Left(new RuntimeException(s"Unexpected Value $cv"))
  }

  implicit val configConfigMapper: ConfigMapper[Config] = {
    case cs: ConfigObject => Right(cs.toConfig)
    case cv => Left(new RuntimeException(s"Unexpected Value $cv"))
  }
}
