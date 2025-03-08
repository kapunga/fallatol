package fallatol.kagi

import io.circe.Codec
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveConfiguredCodec

object MetaCodec {
  implicit val customConfig: Configuration =
    Configuration.default.withSnakeCaseMemberNames
  val metaCodec: Codec[Meta] = deriveConfiguredCodec
}
