package fallatol

import org.ekrich.config.Config

package object config {
  type ConfigResult[T] = Either[Throwable, T]

  implicit class ConfigOps(config: Config) {
    final def get[A](path: String)(implicit
        cf: ConfigGetter[A]
    ): ConfigResult[A] =
      cf.get(config, path)
  }
}
