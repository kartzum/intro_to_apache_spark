package aa.bb.cc.scala_basic

// https://docs.scala-lang.org/tour/basics.html
object Basics {

  object Expressions {
    def run(): Unit = {
      println("Expressions")

      println("Values")
      val a = 2
      val b = 3
      val c = a + b
      println(s"a+b=$c") // a+b=5

      println("Variables")
      var x = 2
      x = 3
      println(s"x=$x") // x=3

      println("General types")
      val aInt = 73
      val bDouble = 36.6
      val cString = "Scala"
      val dChar = 'd'
      val eBool = true
      val fUnit = Unit

      println(s"a = $aInt, ${aInt.getClass}") // a = 73, int
      println(s"b = $bDouble, ${bDouble.getClass}") // b = 36.6, double
      println(s"c = $cString, ${cString.getClass}") // c = Scala, class java.lang.String
      println(s"d = $dChar, ${dChar.getClass}") // d = d, char
      println(s"e = $eBool, ${eBool.getClass}") // e = true, boolean
      println(s"f= $fUnit, ${fUnit.getClass}") // f= object scala.Unit, class scala.Unit$
    }
  }

  object Blocks {
    def run(): Unit = {
      println("Blocks")
      val value = {
        val a = 2
        val b = 3
        a + b
      }
      println(s"value=$value") // value=5
    }
  }

  object Functions {
    def run(): Unit = {
      println("Functions")
      val addOne = (x: Int) => x + 1
      println(s"addOne(1)=${addOne(1)}") // addOne(1)=2
      val add = (x: Int, y: Int) => x + y
      println(s"add(2, 3)=${add(2, 3)}") // add(2, 3)=5
    }
  }

  object Methods {
    def run(): Unit = {
      println("Methods")

      def add(x: Int, y: Int): Int = x + y

      println(s"add(2, 3)=${add(2, 3)}") // add(2, 3)=5
    }
  }

  object Classes {
    def run(): Unit = {
      println("Classes")
      class Calculator {
        def add(a: Int, b: Int): Int = {
          a + b
        }
      }
      val calculator = new Calculator()
      println(s"calculator.add(2, 3)=${calculator.add(2, 3)}") // calculator.add(2, 3)=5
    }
  }

  object CaseClasses {
    def run(): Unit = {
      println("CaseClasses")
      case class Point(x: Int, y: Int)
      val point = Point(2, 3)
      println(s"point.x=${point.x},point.y=${point.y}") // point.x=2,point.y=3
    }
  }

  object Objects {
    def run(): Unit = {
      println("Objects")
      case class Point(x: Int, y: Int)
      object Transformations {
        def trans(data: String): Point = {
          val t = data.split(",")
          Point(t(0).toInt, t(1).toInt)
        }
      }
      val point = Transformations.trans("2,3")
      println(s"point.x=${point.x},point.y=${point.y}") // point.x=2,point.y=3
    }
  }

  object Traits {
    def run(): Unit = {
      println("Traits")
      trait Generator {
        def generate(): String
      }
      class AGenerator extends Generator {
        override def generate(): String = "a"
      }
      class BGenerator extends Generator {
        override def generate(): String = "b"
      }
      val a = new AGenerator()
      val b = new BGenerator()
      println(s"a=${a.generate()}") // a=a
      println(s"b=${b.generate()}") // b=b
    }
  }

  object MainMethod {
    def run(): Unit = {
      println("MainMethod")
      MainMethod.main(new Array[String](0))
    }

    def main(args: Array[String]): Unit = {
      println("main method") // main method
    }
  }

  def main(args: Array[String]): Unit = {
    Expressions.run()
    Blocks.run()
    Functions.run()
    Methods.run()
    Classes.run()
    CaseClasses.run()
    Objects.run()
    Traits.run()
    MainMethod.run()
  }

}
