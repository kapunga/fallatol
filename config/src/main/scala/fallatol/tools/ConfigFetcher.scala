package fallatol.tools

import cats.implicits.toTraverseOps
import org.ekrich.config.impl.{ConfigBoolean, ConfigDouble, ConfigInt, ConfigLong, ConfigNumber, ConfigString}
import org.ekrich.config.{Config, ConfigObject, ConfigValue}
import scala.jdk.CollectionConverters.{ListHasAsScala, SetHasAsScala}
import scala.util.Try

trait ConfigFetcher[A] {
  def get(conf: Config, path: String): Either[Throwable, A]
}

object ConfigFetcher {
  implicit def forConfigMapped[A](implicit cm: ConfigMapper[A]): ConfigFetcher[A] =
    (conf, path) => Try(conf.getValue(path)).toEither.flatMap(cm.get)

  implicit def optionConfigFetcher[A](implicit cf: ConfigFetcher[A]): ConfigFetcher[Option[A]] =
    (conf, path) => {
      if (conf.hasPath(path) && !conf.getIsNull(path)) cf.get(conf, path).map(Option.apply)
      else Right(None)
    }

  implicit def listConfigFetcher[A](implicit cf: ConfigMapper[A]): ConfigFetcher[List[A]] =
    (conf, path) => Try(conf.getList(path).asScala.toList).toEither.flatMap(_.traverse(cf.get))

  implicit def mapConfigFetcher[A](implicit cf: ConfigMapper[A]): ConfigFetcher[Map[String, A]] =
    (conf, path) => Try(conf.getConfig(path)).toEither
      .flatMap(_.entrySet.asScala.toList.traverse(entry => cf.get(entry.getValue).map(entry.getKey -> _))
        .map(_.toMap))
}

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
