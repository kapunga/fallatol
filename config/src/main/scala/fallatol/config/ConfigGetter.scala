package fallatol.config

import scala.util.Try

import org.ekrich.config.Config

/** Helper type class to allow [[ConfigOps]] to allow
  * {{{
  *   config.get[Option[A]]
  * }}}
  * instead of having to have a separate method for Options like:
  * {{{
  *   config.getOption[A]
  * }}}
  *
  * The two type classes defined in the companion object should cover all cases.
  */
private[config] trait ConfigGetter[A] {
  def get(conf: Config, path: String): ConfigResult[A]
}

private[config] object ConfigGetter {

  /** [[ConfigGetter]] for any type with a required value. */
  implicit def forConfigMapped[A](implicit
      cm: ConfigMapper[A]
  ): ConfigGetter[A] =
    (conf, path) => Try(conf.getValue(path)).toEither.flatMap(cm.get)

  /** [[ConfigGetter]] for any type with an optional value. */
  implicit def optionConfigFetcher[A](implicit
      cf: ConfigGetter[A]
  ): ConfigGetter[Option[A]] =
    (conf, path) => {
      if (conf.hasPath(path) && !conf.getIsNull(path))
        cf.get(conf, path).map(Option.apply)
      else Right(None)
    }
}
