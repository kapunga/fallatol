package fallatol

import org.ekrich.config.Config

package object tools {
  implicit class ConfigOps(config: Config) {
    def get[A](path: String)(implicit cf: ConfigFetcher[A]): Either[Throwable, A] =
      cf.get(config, path)
  }
}
