package aa.bb.cc

// https://janzhou.org/2015/scala-functional-programming-for-word-count.html
// https://en.wikipedia.org/wiki/MapReduce
object MapReduce {

  object Base {
    def run(): Unit = {
      println("Base")

      val lines = List("MapReduce is a programming model", "MapReduce is model")
      println(s"$lines")
      val words = lines.flatMap(_.split(" ")) // List(MapReduce is a programming model, MapReduce is model)
      println(s"$words") // List(MapReduce, is, a, programming, model, MapReduce, is, model)
      val wordsGrouped = words.groupBy((word: String) => word) // Map(MapReduce -> List(MapReduce, MapReduce), is -> List(is, is), model -> List(model, model), programming -> List(programming), a -> List(a))
      println(s"$wordsGrouped")
      val wordsLengths = wordsGrouped.mapValues(_.length)
      println(s"$wordsLengths") // Map(MapReduce -> 2, is -> 2, model -> 2, programming -> 1, a -> 1)
    }
  }

  def main(args: Array[String]): Unit = {
    Base.run()
  }
}
