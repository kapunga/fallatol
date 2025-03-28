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

import scala.annotation.implicitNotFound
import scala.jdk.CollectionConverters._
import scala.util.Try

import cats.implicits.toTraverseOps
import org.ekrich.config._
import org.ekrich.config.impl._

/** The core type class for fallatol-config. It describes how an instance of
  * some type obtained from an instance of `org.ekrich.impl.ConfigValue`.
  * Instances of this type class are used by [[ConfigOps]] which allows getting
  * of instances from `org.ekrich.config.Config` via type parameters like:
  * {{{
  *    config.get[Double]("path.to.double")
  * }}}
  * rather than the built in methods with hard coded values:
  * {{{
  *    config.getDouble("path.to.double")
  * }}}
  */
@implicitNotFound(
  "Could not find a ConfigMapper for type '${A}'. You may have to define one."
)
trait ConfigMapper[A] {
  def get(cv: ConfigValue): ConfigResult[A]
}

/** Contains basic [[ConfigMapper]] instances for types are obtainable out of
  * the box by the [[https://github.com/ekrich/sconfig/ sconfig]] library. This
  * includes some primitives like `String`, `Int`, `Long`, `Double`, `Number`,
  * and `Boolean`, as well as `org.ekrich.config.Config`, which is used for
  * nested configuration, as well as [[ConfigMapper]]s for `List` and `Map` with
  * `String` keys.
  *
  * Also has the [[from]] method for building a new [[ConfigMapper]] from an
  * existing [[ConfigMapper]].
  */
object ConfigMapper {

  /** Create a [[ConfigMapper]] for type B by providing a function mapping type
    * A, with a defined [[ConfigMapper]] to [[ConfigResult]] of type B.
    */
  def from[A, B](f: A => ConfigResult[B])(implicit
      acm: ConfigMapper[A]
  ): ConfigMapper[B] = cv => acm.get(cv).flatMap(f)

  /** Convenience method for `from` a String, since this is a common case */
  def fromString[A](f: String => ConfigResult[A]): ConfigMapper[A] =
    from[String, A](f)

  /** Convenience method for `from` a Config, since that is a common case */
  def fromConfig[A](f: Config => ConfigResult[A]): ConfigMapper[A] =
    from[Config, A](f)

  implicit val stringConfigMapper: ConfigMapper[String] = {
    case cs: ConfigString => Right(cs.value)
    case cv               => Left(ConfigError.UnexpectedValue(cv))
  }

  implicit val intConfigMapper: ConfigMapper[Int] = {
    case cs: ConfigInt => Right(cs.value)
    case cv            => Left(ConfigError.UnexpectedValue(cv))
  }

  implicit val longConfigMapper: ConfigMapper[Long] = {
    case ci: ConfigInt  => Right(ci.value.toLong)
    case cl: ConfigLong => Right(cl.value)
    case cv             => Left(ConfigError.UnexpectedValue(cv))
  }

  implicit val doubleConfigMapper: ConfigMapper[Double] = {
    case cs: ConfigDouble => Right(cs.value)
    case cv               => Left(ConfigError.UnexpectedValue(cv))
  }

  implicit val numberConfigMapper: ConfigMapper[Number] = {
    case cs: ConfigNumber => Right(cs.unwrapped)
    case cv               => Left(ConfigError.UnexpectedValue(cv))
  }

  implicit val booleanConfigMapper: ConfigMapper[Boolean] = {
    case cs: ConfigBoolean => Right(cs.value)
    case cv                => Left(ConfigError.UnexpectedValue(cv))
  }

  implicit val configConfigMapper: ConfigMapper[Config] = {
    case cs: ConfigObject => Right(cs.toConfig)
    case cv               => Left(ConfigError.UnexpectedValue(cv))
  }

  implicit def configListMapper[A](implicit
      cm: ConfigMapper[A]
  ): ConfigMapper[List[A]] = {
    case cs: ConfigList =>
      Try(cs.asScala.toList).toEither.flatMap(_.traverse(cm.get))
    case cv => Left(ConfigError.UnexpectedValue(cv))
  }

  implicit def configMapMapper[A](implicit
      cm: ConfigMapper[A]
  ): ConfigMapper[Map[String, A]] =
    from[Config, Map[String, A]](conf => {
      val entries = conf.entrySet.asScala.toList

      entries
        .traverse(entry => cm.get(entry.getValue).map(entry.getKey -> _))
        .map(_.toMap)
    })
}
