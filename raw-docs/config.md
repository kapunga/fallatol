# Fallatól Config

_Note: This is very much a work in progress._

## Quickstart
`fallatol-config` is not published yet, so you will need to build locally. First run:

```bash
fallatol git:(main) sbt publishLocal
```

Then add to your dependencies:

```sbt
 libraryDependencies += "org.fallatol" %% "fallatol-config" % "0.1.0-SNAPSHOT"
```

`fallatol-config` works with [`sconfig`](https://github.com/ekrich/sconfig/) so you may also want to add it to your dependencies as well:

```sbt
libraryDependencies += "org.ekrich" %% "sconfig" % "1.8.1"
```

### Example Usage
The example uses the following `application.conf` file:

```hocon
origin {
  airport_code = "BOS"
  airport_name = "Boston Logan International Airport"
}

destination {
  airport_code = "KEF"
  airport_name = "Keflavíkurflugvöllur"
}
```

Example:

```scala
import fallatol.config._
import org.ekrich.config._

def loadConfig = {
  val config = ConfigFactory.load()
  
  for {
    origin <- config.get[String]("origin.airport_code")
    destination <- config.get[String]("destination.airport_code")
  } yield (origin, destination)
}
```

Example with custom `ConfigMapper` for a case class:

```scala
import fallatol.config._
import org.ekrich.config._

case class Airport(code: String, name: String)

object Airport {
  implicit val airportConfigMapper: ConfigMapper[Airport] =
    ConfigMapper.from[Config, Aiport](config =>
      for {
        code <- config.get[String]("code")
        name <- config.get[String]("name")
      } yield Airport(code, name))
}

def loadConfig: ConfigResult[(Aiport, Aiport)] = {
  val config = ConfigFactory.load()

  for {
    origin <- config.get[Airport]("origin")
    destination <- config.get[Airport]("destination")
  } yield (origin, destination)
}
```
