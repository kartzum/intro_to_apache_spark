package aa.bb.cc.scala_basic

// https://docs.scala-lang.org/tour/variances.html
// https://www.james-willett.com/scala-generics/
// https://groz.github.io/scala/intro/variance/
// https://ru.wikibooks.org/wiki/Scala_%D0%B2_%D0%BF%D1%80%D0%B8%D0%BC%D0%B5%D1%80%D0%B0%D1%85
object Variances {

  abstract class Tag(val name: String) {
    def render(): String
  }

  class H1(val content: String) extends Tag("h1") {
    override def render(): String = s"<$name>$content</$name>"
  }

  class P(val content: String) extends Tag("p") {
    override def render(): String = s"<$name>$content</$name>"
  }

  object Covariance {
    def run(): Unit = {
      println("Covariance")

      def render(tags: Seq[Tag]): Unit =
        tags.foreach {
          t => println(t.render())
        }

      val h1S = Seq(new H1("c1"), new H1("c2"))
      // <h1>c1</h1>
      // <h1>c2</h1>

      val pS = Seq(new P("p1"), new P("p2"))
      // <p>p1</p>
      // <p>p2</p>

      render(h1S)

      render(pS)
    }
  }

  object Contravariance {
    def run(): Unit = {
      println("Contravariance")

      abstract class Renderer[-A] {
        def render(value: A): Unit
      }

      class TagRenderer extends Renderer[Tag] {
        override def render(value: Tag): Unit = {
          println(value.render())
        }
      }

      class H1Renderer extends Renderer[H1] {
        override def render(value: H1): Unit = {
          println(value.render())
        }
      }

      def render(r: Renderer[H1], v: H1): Unit = r.render(v)

      val h1Renderer: Renderer[H1] = new H1Renderer
      val tagRenderer: Renderer[Tag] = new TagRenderer

      render(h1Renderer, new H1("c1")) // <h1>c1</h1>
      render(tagRenderer, new H1("c2")) // <h1>c2</h1>
    }

    object Invariance {
      def run(): Unit = {
        println("Invariance")

        class Container[A](value: A) {
          private var _value: A = value

          def getValue: A = _value

          def setValue(value: A): Unit = {
            _value = value
          }
        }

        // val h1Container: Container[H1] = new Container(new H1("c1"))
        // val tagContainer: Container[Tag] = h1Container
        // tagContainer.setValue(new P("p1"))
        // val h1: H1 = h1Container.getValue
      }
    }

  }

  def main(args: Array[String]): Unit = {
    Covariance.run()
    Contravariance.run()
    Invariance.run()
  }
}
