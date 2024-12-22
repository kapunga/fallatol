package fallatol.tools

import scala.util.Try

import org.ekrich.config.Config

trait ConfigFetcher[A] {
  def get(conf: Config, path: String): ConfigResult[A]
}

object ConfigFetcher {
  implicit def forConfigMapped[A](implicit
      cm: ConfigMapper[A]
  ): ConfigFetcher[A] =
    (conf, path) => Try(conf.getValue(path)).toEither.flatMap(cm.get)

  implicit def optionConfigFetcher[A](implicit
      cf: ConfigFetcher[A]
  ): ConfigFetcher[Option[A]] =
    (conf, path) => {
      if (conf.hasPath(path) && !conf.getIsNull(path))
        cf.get(conf, path).map(Option.apply)
      else Right(None)
    }
}
