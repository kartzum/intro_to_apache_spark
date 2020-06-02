package aa.bb.cc

// https://docs.scala-lang.org/tour/higher-order-functions.html
object HigherOrderFunctions {

  object CommonExamples {
    def run(): Unit = {
      println("CommonExamples")

      val prices = Seq(2, 5, 7)

      val doublePrice = (x: Int) => x * 2
      val doublePrices = prices.map(doublePrice)
      println(s"$doublePrices") // List(4, 10, 14)

      val doublePrices2 = prices.map(x => x * 2)
      println(s"$doublePrices2") // List(4, 10, 14)

      val doublePrices3 = prices.map(_ * 2)
      println(s"$doublePrices3") // List(4, 10, 14)
    }
  }

  object CoercingMethodsIntoFunctions {
    def run(): Unit = {
      println("CoercingMethodsIntoFunctions")

      class Converter(private val k: Double) {
        private def convertFunction(x: Double): Double = x * this.k

        def convert(values: Seq[Double]): Seq[Double] = values.map(this.convertFunction)
      }
      val converter = new Converter(2.5)
      val data = Array[Double](1, 2)
      println(s"${converter.convert(data).mkString(",")}") // 2.5,5.0
    }
  }

  object FunctionsThatAcceptFunctions {
    def run(): Unit = {
      println("FunctionsThatAcceptFunctions")

      class Converter(private val f: Double => Double) {
        def convert(values: Seq[Double]): Seq[Double] = values.map(this.f)
      }

      val converter = new Converter(x => x * 2.5)
      val data = Array[Double](1, 2)
      println(s"${converter.convert(data).mkString(",")}") // 2.5,5.0
    }
  }

  object FunctionsThatReturnFunctions {
    def run(): Unit = {
      println("FunctionsThatReturnFunctions")

      def builder(prefix: String, suffix: String): (String, String) => String = {
        (head: String, tail: String) => s"$prefix$head-$tail$suffix"
      }

      val f = builder("start", "finish")
      val text = f("1", "2")
      println(s"$text") // start1-2finish
    }
  }

  def main(args: Array[String]): Unit = {
    CommonExamples.run()
    CoercingMethodsIntoFunctions.run()
    FunctionsThatAcceptFunctions.run()
    FunctionsThatReturnFunctions.run()
  }
}
