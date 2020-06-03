package aa.bb.cc.scala_basic

// https://spark.apache.org/docs/latest/ml-pipeline.html
// https://spark.apache.org/docs/latest/api/java/index.html?org/apache/spark/ml/Transformer.html
// https://spark.apache.org/docs/2.1.0/api/java/org/apache/spark/ml/Pipeline.html
object Transformers {

  class Dataset(val container: Any) {
  }

  trait Transformer {
    def transform(dataset: Dataset): Dataset
  }

  class Tokenizer extends Transformer {
    override def transform(dataset: Dataset): Dataset = {
      val data = dataset.container.asInstanceOf[List[String]]
      val temp = data.flatMap(x => x.toString.split(" "))
      new Dataset(temp)
    }
  }

  class Grouping extends Transformer {
    override def transform(dataset: Dataset): Dataset = {
      val data = dataset.container.asInstanceOf[List[String]]
      val temp = data.groupBy((t: String) => t)
      new Dataset(temp)
    }
  }

  class Counting extends Transformer {
    override def transform(dataset: Dataset): Dataset = {
      val data = dataset.container.asInstanceOf[Map[String, List[String]]]
      val temp = data.mapValues(_.length)
      new Dataset(temp)
    }
  }

  class Pipeline(private val stages: Array[Transformer]) {
    def fit(dataset: Dataset): Dataset = {
      var temp = dataset
      stages.foreach(t => {
        temp = t.transform(temp)
      })
      temp
    }
  }

  object Transformers {
    def run(): Unit = {
      println("Transformers")

      val lines = List("MapReduce is a programming model", "MapReduce is model")
      val dataset = new Dataset(lines)
      val tokenizer = new Tokenizer()
      val words = tokenizer.transform(dataset)
      println(s"${words.container}")
      val grouping = new Grouping()
      val wordsGrouped = grouping.transform(words)
      println(s"${wordsGrouped.container}")
      val counting = new Counting()
      val wordsLengths = counting.transform(wordsGrouped)
      println(s"${wordsLengths.container}") // Map(MapReduce -> 2, is -> 2, model -> 2, programming -> 1, a -> 1)
    }
  }

  object Pipelines {
    def run(): Unit = {
      println("Pipelines")

      val lines = List("MapReduce is a programming model", "MapReduce is model")
      val dataset = new Dataset(lines)
      val tokenizer = new Tokenizer()
      val grouping = new Grouping()
      val counting = new Counting()
      val pipeline = new Pipeline(Array(tokenizer, grouping, counting))
      val wordsLengths = pipeline.fit(dataset)
      println(s"${wordsLengths.container}") // Map(MapReduce -> 2, is -> 2, model -> 2, programming -> 1, a -> 1)
    }
  }

  def main(args: Array[String]): Unit = {
    Transformers.run()
    Pipelines.run()
  }
}
