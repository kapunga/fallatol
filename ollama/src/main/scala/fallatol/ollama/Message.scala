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

package fallatol.ollama

import io.circe._
import io.circe.syntax._

sealed trait Message {
  def role: Role
  def content: String
}

object Message {
  case class User(content: String, name: Option[String] = None)
      extends Message {
    val role: Role = Role.User
  }

  implicit val userDecoder: Decoder[User] = (c: HCursor) =>
    for {
      content <- c.get[String]("content")
      name <- c.get[Option[String]]("name")
    } yield User(content, name)

  implicit val userEncoder: Encoder[User] = (user: User) =>
    Json
      .obj(
        "role" -> Json.fromString(user.role.name),
        "content" -> Json.fromString(user.content),
        "name" -> Json.fromStringOrNull(user.name)
      )
      .dropNullValues

  // TODO: Images should be base64 encoded strings
  case class Assistant(content: String, images: List[String]) extends Message {
    val role: Role = Role.Assistant
  }

  implicit val assistantDecoder: Decoder[Assistant] = AssistantMessageDecoder()

  implicit val assistantEncoder: Encoder[Assistant] = (assistant: Assistant) =>
    Json
      .obj(
        "role" -> Json.fromString(assistant.role.name),
        "content" -> Json.fromString(assistant.content),
        "images" -> Json.fromValues(assistant.images.map(Json.fromString))
      )
      .dropNullValues

  case class System(content: String) extends Message {
    val role: Role = Role.System
  }

  implicit val systemDecoder: Decoder[System] = (c: HCursor) =>
    c.downField("content").as[String].map(System.apply)

  implicit val systemEncoder: Encoder[System] = (system: System) =>
    Json.obj(
      "role" -> Json.fromString(system.role.name),
      "content" -> Json.fromString(system.content)
    )

  implicit val messageDecoder: Decoder[Message] = (c: HCursor) =>
    c.get[Role]("role").flatMap {
      case Role.Assistant => c.as[Assistant]
      case Role.User      => c.as[User]
      case Role.System    => c.as[System]
      case role => Left(DecodingFailure(s"Invalid Role $role", c.history))
    }

  implicit val messageEncoder: Encoder[Message] = Encoder.instance {
    case assistant: Assistant => assistant.asJson
    case system: System       => system.asJson
    case user: User           => user.asJson
  }
}
