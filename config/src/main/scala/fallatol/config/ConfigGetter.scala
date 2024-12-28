package fallatol.config

import scala.util.Try

import org.ekrich.config.Config

private[config] trait ConfigGetter[A] {
  def get(conf: Config, path: String): ConfigResult[A]
}

private[config] object ConfigGetter {
  implicit def forConfigMapped[A](implicit
      cm: ConfigMapper[A]
  ): ConfigGetter[A] =
    (conf, path) => Try(conf.getValue(path)).toEither.flatMap(cm.get)

  implicit def optionConfigFetcher[A](implicit
      cf: ConfigGetter[A]
  ): ConfigGetter[Option[A]] =
    (conf, path) => {
      if (conf.hasPath(path) && !conf.getIsNull(path))
        cf.get(conf, path).map(Option.apply)
      else Right(None)
    }
}
