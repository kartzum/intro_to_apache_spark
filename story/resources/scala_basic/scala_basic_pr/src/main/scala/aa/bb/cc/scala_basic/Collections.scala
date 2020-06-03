package aa.bb.cc.scala_basic

// https://docs.scala-lang.org/overviews/collections/overview.html
// https://alvinalexander.com/scala/scala-collections-classes-methods-organized-category/
// https://alvinalexander.com/scala/collection-scala-flatmap-examples-map-flatten/
object Collections {

  object MutableAndImmutableCollections {
    def run(): Unit = {
      println("MutableAndImmutableCollections")

      val immutableSeq = scala.collection.immutable.Seq(1, 2, 3)
      println(s"immutableSeq=${immutableSeq.mkString(",")}") // immutableSeq=1,2,3
      val mutableSeq = scala.collection.mutable.Seq(1, 2, 3)
      println(s"mutableSeq=${mutableSeq.mkString(",")}") // mutableSeq=1,2,3
    }
  }

  object MainMethods {
    def run(): Unit = {
      println("MainMethods")

      val a = scala.collection.immutable.Seq(1, 2, 3, 4, 5)

      a.forall(x => {
        println(x)
        true
      })
      a.foreach(x => {
        println(x)
      })

      println("Converting")
      val aTraversable = a.toTraversable
      val aSet = a.toSet
      println(s"$aTraversable") // List(1, 2, 3, 4, 5)
      println(s"$aSet") // Set(5, 1, 2, 3, 4)

      println("Finding")
      val aFilter1 = a.filter(_ > 3)
      println(s"$aFilter1") // List(4, 5)
      println(s"${a.tail}") // List(2, 3, 4, 5)
      println(s"${a.head}") // 1

      println("Transforming")
      val extend = (x: Int) => Seq(x - 1, x, x + 1)
      val aExtended = a.flatMap(extend)
      println(s"$aExtended") // List(0, 1, 2, 1, 2, 3, 2, 3, 4, 3, 4, 5, 4, 5, 6)

      println("Grouping")
      val aGrouped1 = a.groupBy(_ > 2)
      println(s"$aGrouped1") // Map(false -> List(1, 2), true -> List(3, 4, 5))
    }
  }

  def main(args: Array[String]): Unit = {
    MutableAndImmutableCollections.run()
    MainMethods.run()
  }
}
