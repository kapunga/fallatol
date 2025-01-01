package fallatol.config

case class Person(name: String, age: Int)

object Person {
  implicit val personConfigMapper: ConfigMapper[Person] =
    ConfigMapper.fromConfig(config =>
      for {
        name <- config.get[String]("name")
        age <- config.get[Int]("age")
      } yield Person(name, age)
    )
}
