# Fallatól Ollama

A prototype library for interacting with [Ollama](https://ollama.com/).


_Note: This library is under development and is mostly receiving updates as I have need of the provided functionality.
Feature requests are more than welcome!_

### Quickstart

Add to your project's `build.sbt`:
```sbt
libraryDependencies += "org.fallatol" %% "fallatol-ollama" % "0.3.0"
```

Read some configs:
```scala
import fallatol.ollama._
import fallatol.ollama.client._

// Create a client
val ollama = SyncOllamaClient()

// Use a client
val chatRequest = ChatRequest(Model.Llama3_2, Seq(Message.User("Say 'Hello World' but as a haiku.")))

val response = ollama.chat(chatRequest)

response.foreach(r => println(r.message.content))

// World, I greet you now
// Digital words dance with joy
// Hello, you are here,
```
