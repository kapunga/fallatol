import sbt.*

object Dependencies {
  object V {
    val cats = "2.12.0"
    val catsEffect = "3.5.7"
    val circe = "0.14.10"
    val fs2 = "3.11.0"
    val scalaTest = "3.2.19"
    val sconfig = "1.8.1"
  }

  object Libraries {
    val cats: Seq[ModuleID] =
      Seq("org.typelevel" %% "cats-core" % V.cats)

    val catsEffect: Seq[ModuleID] =
      Seq("org.typelevel" %% "cats-effect" % V.catsEffect)


    val circe: Seq[ModuleID] = Seq(
      "io.circe" %% "circe-core" % V.circe,
      "io.circe" %% "circe-generic" % V.circe,
      "io.circe" %% "circe-parser" % V.circe)

    val fs2: Seq[ModuleID] =
      Seq("co.fs2" %% "fs2-core" % V.fs2,
          "co.fs2" %% "fs2-io" % V.fs2)

    val scalaTest: Seq[ModuleID] =
      Seq("org.scalatest" %% "scalatest" % V.scalaTest % Test)

    val sconfig: Seq[ModuleID] =
      Seq("org.ekrich" %% "sconfig" % V.sconfig)
  }
}
