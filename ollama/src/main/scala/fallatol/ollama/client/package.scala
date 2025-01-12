package fallatol.ollama

import io.circe
import sttp.client4._

package object client {
  type OllamaRequest[A] =
    Request[Either[ResponseException[String, circe.Error], A]]
  type SyncType[A] = Either[Throwable, A]
}
