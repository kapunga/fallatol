import sbt.*

object Dependencies {
  object V {
    val cats = "2.13.0"
    val circe = "0.14.10"
    val scalaTest = "3.2.19"
    val sconfig = "1.8.1"
  }

  object Libraries {
    val cats: Seq[ModuleID] =
      Seq("org.typelevel" %% "cats-core" % V.cats)

    val circe: Seq[ModuleID] = Seq(
      "io.circe" %% "circe-core" % V.circe,
      "io.circe" %% "circe-generic" % V.circe,
      "io.circe" %% "circe-parser" % V.circe
    )

    val scalaTest: Seq[ModuleID] =
      Seq("org.scalatest" %% "scalatest" % V.scalaTest % Test)

    val sconfig: Seq[ModuleID] =
      Seq("org.ekrich" %% "sconfig" % V.sconfig)
  }
}
