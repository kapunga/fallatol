import Dependencies.Libraries

val scala212 = "2.12.20"
val scala213 = "2.13.15"
val scala3 = "3.3.4"

ThisBuild / scalaVersion := "2.13.15"
ThisBuild / crossScalaVersions := Seq(scala212, scala213, scala3)
ThisBuild / organization := "org.fallatol"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / semanticdbEnabled := true
ThisBuild / semanticdbVersion := scalafixSemanticdb.revision
ThisBuild / coverageEnabled := true

lazy val root = (project in file("."))
    //.jsSettings(/* ... */) // defined in sbt-scalajs-crossproject
    //.jvmSettings(/* ... */)
    // configure Scala-Native settings
    //.nativeSettings(/* ... */) // defined in sbt-scala-native
    .settings(
      publish / skip := true,
      name := "fallatol",
      crossScalaVersions := Nil,
      description := "A collection of Thor's micro-libraries.",
      startYear := Some(2024)
    )
    .aggregate(
      config.js,
      config.jvm,
      config.native
    )

lazy val config = crossProject(JSPlatform, JVMPlatform, NativePlatform)
  .crossType(CrossType.Pure)
  .withoutSuffixFor(JVMPlatform)
  .in(file("config"))
  .settings(
    description := "A collection of Typeclasses and utilities for use with the `sconfig` library",
    startYear := Some(2024),
    moduleName := "fallatol-config",
    crossScalaVersions := Seq(scala212, scala213, scala3),
    libraryDependencies ++= Libraries.cats ++ Libraries.scalaTest ++ Libraries.sconfig
  )

