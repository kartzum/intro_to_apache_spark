package aa.bb.cc.scala_basic

// https://docs.scala-lang.org/tour/multiple-parameter-lists.html
// https://alvinalexander.com/scala/scala-collections-classes-methods-organized-category/
// https://docs.scala-lang.org/overviews/collections/overview.html
object MultipleParameterListsCurrying {

  object Example {
    def run(): Unit = {
      println("Example")

      val numbers = List(1, 2, 3)
      val result = numbers.foldLeft(0)((m, n) => m + n)
      println(s"$result") // 6
    }
  }

  def main(args: Array[String]): Unit = {
    Example.run()
  }
}
