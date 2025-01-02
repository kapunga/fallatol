# Fallatól - Functional Tools

![Scala CI](https://github.com/kapunga/fallatol/workflows/Scala%20CI/badge.svg)
[![Scala Steward badge](https://img.shields.io/badge/Scala_Steward-helping-blue.svg?style=flat&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAQCAMAAAARSr4IAAAAVFBMVEUAAACHjojlOy5NWlrKzcYRKjGFjIbp293YycuLa3pYY2LSqql4f3pCUFTgSjNodYRmcXUsPD/NTTbjRS+2jomhgnzNc223cGvZS0HaSD0XLjbaSjElhIr+AAAAAXRSTlMAQObYZgAAAHlJREFUCNdNyosOwyAIhWHAQS1Vt7a77/3fcxxdmv0xwmckutAR1nkm4ggbyEcg/wWmlGLDAA3oL50xi6fk5ffZ3E2E3QfZDCcCN2YtbEWZt+Drc6u6rlqv7Uk0LdKqqr5rk2UCRXOk0vmQKGfc94nOJyQjouF9H/wCc9gECEYfONoAAAAASUVORK5CYII=)](https://scala-steward.org)
[![Maven Central](https://img.shields.io/maven-central/v/org.kapunga/fallatol-config_2.13.svg)](https://maven-badges.herokuapp.com/maven-central/org.kapunga/fallatol-config_2.13)

A collection of [Kapunga's](https://github.com/kapunga) personal micro-libraries. Cross build where possible to all
Scala platforms (**JVM**, **JS**, and **Native**) for Scala versions **2.12**, **2.13**, and **3**.

## About

Fallatól is a collection of experimental micro-libraries that emerged from patterns I found myself repeating in my
personal Scala projects. These libraries serve both as a centralized repository for code reuse in my side-projects
and as well as to gain additional practice in end to end library development. While I'm making an effort to document
and test them well, I make no guarantees as to production readiness or comprehensive feature coverage. I do welcome
feedback and feature requests if you find them useful.

Main documentation is located [here](https://fallatol.kapunga.org).

## Current Micro-Libraries

### [_fallatol-config_](https://fallatol.kapunga.org/config.html) 

A collection of implicits used with the [_sconfig library_](https://github.com/ekrich/sconfig/), providing a `get`
method to `Config` that is generic and referentially transparent.

### Quickstart

Add to your project's `build.sbt`:
```sbt
libraryDependencies += "org.fallatol" %% "fallatol-config" % "0.1.1"
```
Read some configs:
```scala
import fallatol.config._
import org.ekrich.config._

case class Person(name: String, age: Int)
    
def loadConfig = {
  val personHocon =
    """
      |{
      |  name: "Alice"
      |  age: 37
      |}
      |""".stripMargin
      
  val config = ConfigFactory.parseString(personHocon)
  
  for {
    name <- config.get[String]("name")
    age <- config.get[Int]("age")
  } yield (origin, destination)
}
```
