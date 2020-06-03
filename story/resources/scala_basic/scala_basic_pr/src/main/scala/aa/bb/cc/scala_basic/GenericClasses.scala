package aa.bb.cc.scala_basic

// https://docs.scala-lang.org/tour/generic-classes.html
// https://alvinalexander.com/scala/scala-classes-using-generic-types-examples/
// https://dev.to/jmcclell/inheritance-vs-generics-vs-typeclasses-in-scala-20op
object GenericClasses {

  object DefiningAGenericClass {
    def run(): Unit = {
      println("DefiningAGenericClass")

      trait MyArrayInt {
        def set(i: Int, value: Int): Unit

        def get(i: Int): Int
      }

      trait MyArrayDouble {
        def set(i: Int, value: Double): Unit

        def get(i: Int): Double
      }

      trait MyArray[T] {
        def set(i: Int, value: T): Unit

        def get(i: Int): T
      }
    }
  }

  def main(args: Array[String]): Unit = {
    DefiningAGenericClass.run()
  }
}
