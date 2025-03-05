/*
 * Copyright (c) 2024 Paul (Thor) Thordarson
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package fallatol

import cats._
import cats.implicits._
import org.ekrich.config.{ Config, ConfigFactory }

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

  /** Type alias for config parsing returning either */
  type ConfigResult[T] = Either[Throwable, T]

  implicit class ConfigOps(config: Config) {
    final def get[A](path: String)(implicit
        cf: ConfigGetter[A]
    ): ConfigResult[A] =
      cf.get(config, path)

    final def getOrElse[A](path: String, default: A)(implicit
        cf: ConfigGetter[A]
    ): ConfigResult[A] = {
      if (config.hasPath(path) && !config.getIsNull(path)) cf.get(config, path)
      else Right(default)
    }

    final def withFallbackF[F[_]: MonadThrow](fallback: F[Config]): F[Config] =
      fallback.map(fb => config.withFallback(fb))
  }

  implicit class ConfigLoadOps(configFactory: ConfigFactory.type) {
    final def loadF[F[_]: MonadThrow](resourceBasename: String): F[Config] =
      MonadThrow[F].catchNonFatal(configFactory.load(resourceBasename))
  }
}
