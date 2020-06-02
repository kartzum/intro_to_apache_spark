package aa.bb.cc

// https://docs.scala-lang.org/tour/unified-types.html
object UnifiedTypes {

  object ScalaTypeHierarchy {
    def run(): Unit = {
      println("ScalaTypeHierarchy")
      val a: Any = 10
      val b: Any = true
      val c: AnyVal = 10.1
      val d: AnyRef = new Array[String](0)
      println(s"type(a)=${a.getClass}") // type(a)=class java.lang.Integer
      println(s"type(b)=${b.getClass}") // type(b)=class java.lang.Boolean
      println(s"type(c)=${c.getClass}") // type(c)=class java.lang.Double
      println(s"type(d)=${d.getClass}") // type(d)=class [Ljava.lang.String;
    }
  }

  object TypeCasting {
    def run(): Unit = {
      println("TypeCasting")
      val x: Long = 73
      val y: Float = x
      println(s"y=$y") // y=73.0
    }
  }

  object NothingAndNull {
    def run(): Unit = {
      println("NothingAndNull")
      val a = null
      println(s"a=$a") // a=null
    }
  }

  def main(args: Array[String]): Unit = {
    ScalaTypeHierarchy.run()
    TypeCasting.run()
    NothingAndNull.run()
  }
}
