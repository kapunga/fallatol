# Fallatól - Functional Tools

![Scala CI](https://github.com/kapunga/fallatol/workflows/Scala%20CI/badge.svg)
[![Scala Steward badge](https://img.shields.io/badge/Scala_Steward-helping-blue.svg?style=flat&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAQCAMAAAARSr4IAAAAVFBMVEUAAACHjojlOy5NWlrKzcYRKjGFjIbp293YycuLa3pYY2LSqql4f3pCUFTgSjNodYRmcXUsPD/NTTbjRS+2jomhgnzNc223cGvZS0HaSD0XLjbaSjElhIr+AAAAAXRSTlMAQObYZgAAAHlJREFUCNdNyosOwyAIhWHAQS1Vt7a77/3fcxxdmv0xwmckutAR1nkm4ggbyEcg/wWmlGLDAA3oL50xi6fk5ffZ3E2E3QfZDCcCN2YtbEWZt+Drc6u6rlqv7Uk0LdKqqr5rk2UCRXOk0vmQKGfc94nOJyQjouF9H/wCc9gECEYfONoAAAAASUVORK5CYII=)](https://scala-steward.org)
[![Maven Central](https://img.shields.io/maven-central/v/org.kapunga/fallatol-config_2.13.svg)](https://maven-badges.herokuapp.com/maven-central/org.kapunga/fallatol-config_2.13)

A collection of [Kapunga's](https://github.com/kapunga) personal micro-libraries. Cross build where possible to all
Scala platforms (**JVM**, **JS**, and **Native**) for Scala versions **2.12**, **2.13**, and **3**.

Main documentation for these libraries is located [here](https://fallatol.kapunga.org).

## What are these Micro-Libraries?

Fallatól is a collection of micro-libraries stemming from patterns I've found myself repeating over and over when I
start a new toy Scala project. Collecting theses micro-libraries reduces the amount of time spent copy-pasting code
when new toy projects are started. It also gives space to develop and refine half-baked ideas, collecting them into
a single place, rather than scattered across half a dozen abandoned exploratory projects.

Additionally, writing and publishing these micro-libraries are a learning experience in and of itself, offering an
opportunity to practice setting up projects in SBT, configuring CI, writing and publishing documentation, ensuring
adequate code coverage by testing, keeping dependencies up to date, etc.

These micro-libraries are primarily intended to support my personal projects and learning. That being said, I will
be making an effort for them easily usable by others, via documentation and publishing. If you find any of them to
be helpful or would like to see an additional feature that would support your use case, let me know!

## Things these Micro-Libraries are not

* **Production Ready** - Although I am endeavoring to make these libraries well tested and bug free, I can't offer
a guarantee that they fully ready for prime time in production use.
* **Full Featured** - Since these micro-libraries stem from use cases in toy projects and experiments, they likely
won't comprehensively cover the use cases in the domain they are meant to address. If you do find yourself wishing
that they had a feature to support a use case that you find valuable, do not hesitate to open an issue and I'll be
more than happy to see what I can do about adding it!

## Current Micro-Libraries

### [_fallatol-config_](https://fallatol.kapunga.org/config.html) 

A collection of implicits used with the [_sconfig library_](https://github.com/ekrich/sconfig/), providing a `get`
method to `Config` that is generic and referentially transparent.
