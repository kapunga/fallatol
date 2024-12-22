package fallatol

import org.ekrich.config.Config

package object tools {
  type ConfigResult[T] = Either[Throwable, T]

  implicit class ConfigOps(config: Config) {
    def get[A](path: String)(implicit
        cf: ConfigFetcher[A]
    ): ConfigResult[A] =
      cf.get(config, path)
  }
}
