import Dependencies.Libraries
import laika.config.{LinkConfig, SourceLinks, SyntaxHighlighting}
import laika.format.Markdown
import laika.helium.Helium
import laika.helium.config.{HeliumIcon, IconLink}
import laika.theme.ThemeProvider

val scala212 = "2.12.20"
val scala213 = "2.13.15"
val scala3 = "3.3.4"
val scalaVersions = Seq(scala212, scala213, scala3)

ThisBuild / scalaVersion := scala213
ThisBuild / organization := "org.kapunga"
ThisBuild / semanticdbEnabled := true
ThisBuild / semanticdbVersion := scalafixSemanticdb.revision
ThisBuild / coverageEnabled := true
ThisBuild / homepage := Some(url("https://kapunga.github.io/fallatol"))
ThisBuild / licenses := List("MIT" -> url("https://opensource.org/license/mit"))
ThisBuild / developers := List(
  Developer(
    id = "kapunga",
    name = "Paul (Thor) Thordarson",
    email = "kapunga@gmail.com",
    url = url("https://github.com/kapunga")
  )
)

lazy val helium: ThemeProvider = Helium.defaults
  .all.metadata(
    title = Some("Fallatól"),
    language = Some("en")
  )
  .site.topNavigationBar(
    navLinks = Seq(
      IconLink.external("https://github.com/kapunga/fallatol", HeliumIcon.github),
    )
  ).build

lazy val root = (project in file("."))
    .settings(
      publish / skip := true,
      name := "fallatol",
      crossScalaVersions := Nil,
      description := "A collection of Thor's micro-libraries.",
      startYear := Some(2024),
      laikaExtensions ++= Seq(Markdown.GitHubFlavor, SyntaxHighlighting),
      // Raw site files are `site-docs` since GitHub pages reads from `docs`
      Laika / sourceDirectories := Seq(file("site-docs")),
      laikaSite / target := file("docs"),
      laikaTheme := helium
    )
    .enablePlugins(LaikaPlugin)
    .aggregate(
      config.js,
      config.jvm,
      config.native,
      prototype.js,
      prototype.jvm
    )

lazy val config = crossProject(JSPlatform, JVMPlatform, NativePlatform)
  .crossType(CrossType.Pure)
  .in(file("config"))
  .settings(
    description := "A collection of Type classes and utilities for use with the `sconfig` library",
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

// Currently can't support native because of CE3 lack of support for Scala Native 0.5
lazy val prototype = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .in(file("prototype"))
  .settings(
    description := "Tools for prototyping",
    startYear := Some(2025),
    moduleName := "fallatol-prototype",
    libraryDependencies ++= Libraries.cats ++ Libraries.catsEffect ++ Libraries.circe ++ Libraries.fs2 ++ Libraries.scalaTest ++ Libraries.sconfig
  )
  .jsSettings(
    crossScalaVersions := scalaVersions,
  )
  .jvmSettings(
    crossScalaVersions := scalaVersions,
  )
