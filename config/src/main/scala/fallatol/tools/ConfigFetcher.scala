package fallatol.tools

import cats.implicits.toTraverseOps
import org.ekrich.config.Config
import scala.jdk.CollectionConverters._
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
