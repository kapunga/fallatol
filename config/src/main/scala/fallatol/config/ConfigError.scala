package fallatol.config

import org.ekrich.config.ConfigValue

object ConfigError {
  case class UnexpectedValue[CV <: ConfigValue](configValue: CV)
      extends Exception(
        s"Unexpected config value type ${configValue.valueType} - ${configValue.render}"
      )
}
