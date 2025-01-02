# Fallatól - Functional Tools

A collection of [Kapunga's](https://github.com/kapunga) personal micro-libraries. Cross build where possible to all
Scala platforms (**JVM**, **JS**, and **Native**) for Scala versions **2.12**, **2.13**, and **3**.

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

## Planned Micro-Libraries
* `fallatol-slack` - Slack connector library
* `fallatol-llama` - Ollama connector library

## Name
_Fallatól_ is a mashup for the Icelandic words _['fall'](https://en.wiktionary.org/wiki/fall#Icelandic)_ meaning a function in programming, and _[tól](https://en.wiktionary.org/wiki/t%C3%B3l#Icelandic)_
meaning tool(s). Icelandic is a rare enough language that using it in a library name is highly likely to avoid name
collisions. Also, I'm part Icelandic and have started learning the language.
