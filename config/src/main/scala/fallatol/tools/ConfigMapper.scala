package fallatol.tools

import scala.jdk.CollectionConverters.{ListHasAsScala, SetHasAsScala}
import scala.util.Try

import cats.implicits.toTraverseOps
import org.ekrich.config._
import org.ekrich.config.impl._

trait ConfigMapper[A] {
  def get(cv: ConfigValue): ConfigResult[A]
}

object ConfigMapper {
  def flatMapped[A, B](f: A => ConfigResult[B])(implicit
      acm: ConfigMapper[A]
  ): ConfigMapper[B] =
    cv => acm.get(cv).flatMap(f)

  implicit val stringConfigMapper: ConfigMapper[String] = {
    case cs: ConfigString => Right(cs.value)
    case cv               => Left(ConfigError.UnexpectedValue(cv))
  }

  implicit val intConfigMapper: ConfigMapper[Int] = {
    case cs: ConfigInt => Right(cs.value)
    case cv            => Left(ConfigError.UnexpectedValue(cv))
  }

  implicit val longConfigMapper: ConfigMapper[Long] = {
    case ci: ConfigInt  => Right(ci.value.toLong)
    case cl: ConfigLong => Right(cl.value)
    case cv             => Left(ConfigError.UnexpectedValue(cv))
  }

  implicit val doubleConfigMapper: ConfigMapper[Double] = {
    case cs: ConfigDouble => Right(cs.value)
    case cv               => Left(ConfigError.UnexpectedValue(cv))
  }

  implicit val numberConfigMapper: ConfigMapper[Number] = {
    case cs: ConfigNumber => Right(cs.unwrapped)
    case cv               => Left(ConfigError.UnexpectedValue(cv))
  }

  implicit val booleanConfigMapper: ConfigMapper[Boolean] = {
    case cs: ConfigBoolean => Right(cs.value)
    case cv                => Left(ConfigError.UnexpectedValue(cv))
  }

  implicit val configConfigMapper: ConfigMapper[Config] = {
    case cs: ConfigObject => Right(cs.toConfig)
    case cv               => Left(ConfigError.UnexpectedValue(cv))
  }

  implicit def configListMapper[A](implicit
      cm: ConfigMapper[A]
  ): ConfigMapper[List[A]] = {
    case cs: ConfigList =>
      Try(cs.asScala.toList).toEither.flatMap(_.traverse(cm.get))
    case cv => Left(ConfigError.UnexpectedValue(cv))
  }

  implicit def configMapMapper[A](implicit
      cm: ConfigMapper[A]
  ): ConfigMapper[Map[String, A]] =
    flatMapped[Config, Map[String, A]](conf => {
      val entries = conf.entrySet.asScala.toList

      entries
        .traverse(entry => cm.get(entry.getValue).map(entry.getKey -> _))
        .map(_.toMap)
    })
}
