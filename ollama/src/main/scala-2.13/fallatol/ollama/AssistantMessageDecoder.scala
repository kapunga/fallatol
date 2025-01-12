package fallatol.ollama

import io.circe.{ Decoder, HCursor }

object AssistantMessageDecoder {
  def apply(): Decoder[Message.Assistant] = (c: HCursor) =>
    for {
      content <- c.get[String]("content")
      images <- c.downField("images").as[List[String]].orElse(Right(List.empty))
    } yield Message.Assistant(content, images)
}
