package aa.bb.cc.scala_basic

// https://docs.scala-lang.org/tour/classes.html
// https://alvinalexander.com/scala/how-to-class-superclass-constructor-in-scala-parent-class/
// https://www.w3schools.com/tags/tag_html.asp
// https://www.scala-js.org/
object Classes {

  object DefiningAClass {
    def run(): Unit = {
      println("DefiningAClass")
      class H1 {
      }
      class P {
      }
      val h1 = new H1()
      val p = new P()
      println(s"h1=$h1") // h1=aa.bb.cc.Classes$DefiningAClass$H1$1@6385cb26
      println(s"p=$p") // p=aa.bb.cc.Classes$DefiningAClass$P$1@38364841
    }
  }

  object Constructors {
    def run(): Unit = {
      println("Constructors")
      class H1(val content: String) {
        override def toString = s"H1($content)"
      }
      val h1 = new H1("Title")
      println(s"h1=$h1") // h1=H1(Title)
      println(s"h1.content=${h1.content}") // h1.content=Title
    }
  }

  object MethodsAndMore {
    def run(): Unit = {
      println("MethodsAndMore")

      abstract class Tag(val name: String) {
        def render(): String
      }

      class H1(val content: String) extends Tag("h1") {
        override def render(): String = s"<$name>$content</$name>"
      }

      class P(val content: String) extends Tag("p") {
        override def render(): String = s"<$name>$content</$name>"
      }

      val h1 = new H1("Title1")
      val p = new P("Title2")

      println(s"h1=${h1.render()}") // h1=<h1>Title1</h1>
      println(s"p=${p.render()}") // p=<p>Title2</p>
    }
  }

  def main(args: Array[String]): Unit = {
    DefiningAClass.run()
    Constructors.run()
    MethodsAndMore.run()
  }
}
