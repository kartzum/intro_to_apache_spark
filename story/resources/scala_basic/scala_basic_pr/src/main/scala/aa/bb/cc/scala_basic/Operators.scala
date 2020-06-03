package aa.bb.cc.scala_basic

// https://docs.scala-lang.org/tour/operators.html
object Operators {

  object DefiningAndUsingOperators {
    def run(): Unit = {
      println("DefiningAndUsingOperators")

      class Vector(val x: Double, val y: Double) {
        def +(that: Vector) = new Vector(this.x + that.x, this.y + that.y)
      }

      val vector1 = new Vector(1.0, 2.0)
      val vector2 = new Vector(3.0, 4.0)
      val vector3 = vector1 + vector2

      println(s"${vector3.x},${vector3.y}") // 4.0,6.0
    }
  }

  def main(args: Array[String]): Unit = {
    DefiningAndUsingOperators.run()
  }
}
