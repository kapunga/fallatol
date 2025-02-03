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
