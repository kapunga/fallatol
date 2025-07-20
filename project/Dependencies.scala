import sbt.*

object Dependencies {
  object V {
    val cats = "2.13.0"
    val circe = "0.14.14"
    val circeGenericExtras = "0.14.4"
    val scalaTest = "3.2.19"
    val sconfig = "1.11.0"
    val sttpClient = "4.0.9"
    val tapir = "1.11.36"
  }

  object Libraries {
    val cats: Seq[ModuleID] =
      Seq("org.typelevel" %% "cats-core" % V.cats)

    val circe: Seq[ModuleID] = Seq(
      "io.circe" %% "circe-core" % V.circe,
      "io.circe" %% "circe-generic" % V.circe,
      "io.circe" %% "circe-parser" % V.circe
    )

    val circeExtras: Seq[ModuleID] = Seq(
      "io.circe" %% "circe-generic-extras" % V.circeGenericExtras
    )

    val scalaTest: Seq[ModuleID] =
      Seq("org.scalatest" %% "scalatest" % V.scalaTest % Test)

    val sconfig: Seq[ModuleID] =
      Seq("org.ekrich" %% "sconfig" % V.sconfig)

    val sttpClient: Seq[ModuleID] = Seq(
      "com.softwaremill.sttp.client4" %% "core" % V.sttpClient,
      "com.softwaremill.sttp.client4" %% "circe" % V.sttpClient,
      "com.softwaremill.sttp.client4" %% "fs2" % V.sttpClient
    )

    val tapir: Seq[ModuleID] = Seq(
      "com.softwaremill.sttp.tapir" %% "tapir-core" % V.tapir,
      "com.softwaremill.sttp.tapir" %% "tapir-json-circe" % V.tapir,
      "com.softwaremill.sttp.tapir" %% "tapir-apispec-docs" % V.tapir,
      "com.softwaremill.sttp.apispec" %% "jsonschema-circe" % "0.11.10"
    )
  }
}
