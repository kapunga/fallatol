package fallatol.ollama.client

import fallatol.ollama.ModelInfo
import io.circe.syntax._
import io.circe._

case class TagsResponse(models: List[ModelInfo])

object TagsResponse {
  implicit val tagsResponseDecoder: Decoder[TagsResponse] = (c: HCursor) =>
    c.downField("models").as[List[ModelInfo]].map(TagsResponse(_))

  implicit val tagsResponseEncoder: Encoder[TagsResponse] = (tr: TagsResponse) => {
    Json.obj("models" -> Json.fromValues(tr.models.map(_.asJson)))
  }
}
