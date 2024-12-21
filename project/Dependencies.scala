import sbt.*

object Dependencies {
  object V {
    val cats = "2.12.0"
    val scalaTest = "3.2.19"
    val sconfig = "1.8.1"
  }

  object Libraries {
    val cats: Seq[ModuleID] =
      Seq("org.typelevel" %% "cats-core" % V.cats)

    val scalaTest: Seq[ModuleID] =
      Seq("org.scalatest" %% "scalatest" % V.scalaTest % Test)

    val sconfig: Seq[ModuleID] =
      Seq("org.ekrich" %% "sconfig" % V.sconfig)
  }
}
