import Dependencies.Libraries

ThisBuild / scalaVersion := "2.13.15"
ThisBuild / organization := "org.fallatol"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / semanticdbEnabled := true
ThisBuild / semanticdbVersion := scalafixSemanticdb.revision
ThisBuild / coverageEnabled := true

lazy val scalacOpts = List(
  "-Ywarn-unused:imports",
  "-Xsource:3"
)

lazy val root = (project in file("."))
  .settings(
    publish / skip := true,
    name := "fallatol",
    description := "A collection of Thor's micro-libraries.",
    startYear := Some(2024)
  )
  .aggregate(config)

lazy val config = (project in file("config"))
  .settings(
    description := "A collection of Typeclasses and utilities for use with the `sconfig` library",
    startYear := Some(2024),
    moduleName := "fallatol-config",
    libraryDependencies ++= Libraries.cats ++ Libraries.scalaTest ++ Libraries.sconfig,
    scalacOptions ++= scalacOpts
  )

