package fallatol

import org.ekrich.config.Config

/** Provides type classes for extracting values in a generic manner from HOCON
  * config files read by the library
  * [[https://github.com/ekrich/sconfig sconfig]].
  *
  * @example {{{
  *
  *   import fallatol.config._
  *   import org.ekrich.config._
  *
  *   val config = ConfigFactory.parseString(
  *     """
  *       |foo = "bar"
  *       |meaning_of_life = 42
  *       |""".stripMargin)
  *
  *   val foo: ConfigResult[String] = config.get[String]("foo")
  *   val meaningOfLife: ConfigResult[Int] = config.get[Int]("bar")
  * }}}
  */
package object config {

  /** Type alias for */
  type ConfigResult[T] = Either[Throwable, T]

  implicit class ConfigOps(config: Config) {
    final def get[A](path: String)(implicit
        cf: ConfigGetter[A]
    ): ConfigResult[A] =
      cf.get(config, path)
  }
}
