import Dependencies.Libraries
import laika.config.{MessageFilter, MessageFilters, SyntaxHighlighting}
import laika.format.Markdown

val scala212 = "2.12.20"
val scala213 = "2.13.15"
val scala3 = "3.3.4"
val scalaVersions = Seq(scala212, scala213, scala3)

ThisBuild / scalaVersion := scala213
ThisBuild / organization := "org.fallatol"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / semanticdbEnabled := true
ThisBuild / semanticdbVersion := scalafixSemanticdb.revision
ThisBuild / coverageEnabled := true
ThisBuild / homepage := Some(url("https://github.com/kapunga/fallatol"))
ThisBuild / licenses := List("MIT" -> url("https://opensource.org/license/mit"))
ThisBuild / developers := List(
  Developer(
    id = "kapunga",
    name = "Paul (Thor) Thordarson",
    email = "kapunga@gmail.com",
    url = url("https://github.com/kapunga")
  )
)

lazy val root = (project in file("."))
    .settings(
      publish / skip := true,
      name := "fallatol",
      crossScalaVersions := Nil,
      description := "A collection of Thor's micro-libraries.",
      startYear := Some(2024),
      laikaExtensions ++= Seq(Markdown.GitHubFlavor, SyntaxHighlighting),
      Laika / sourceDirectories := Seq(file("raw-docs")),
      laikaSite / target := file("docs")
    )
    .enablePlugins(LaikaPlugin)
    .aggregate(
      config.js,
      config.jvm,
      config.native
    )

lazy val config = crossProject(JSPlatform, JVMPlatform, NativePlatform)
  .crossType(CrossType.Pure)
  .in(file("config"))
  .settings(
    description := "A collection of Typeclasses and utilities for use with the `sconfig` library",
    startYear := Some(2024),
    moduleName := "fallatol-config",
    libraryDependencies ++= Libraries.cats ++ Libraries.scalaTest ++ Libraries.sconfig
  )
  .jsSettings(
    crossScalaVersions := scalaVersions,
  )
  .jvmSettings(
    crossScalaVersions := scalaVersions,
  )
  .nativeSettings(
    crossScalaVersions := scalaVersions
  )

