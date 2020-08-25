package aa.bb.cc.cluster.spark_ml

import org.apache.spark.ml.{Pipeline, PipelineModel}
import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.ml.feature.{HashingTF, Tokenizer}
import org.apache.spark.sql.DataFrame

object PipelinesEx {

  object Transformers {

    class Dataset(val container: Any) {
    }

    trait Transformer {
      def transform(dataset: Dataset): Dataset
    }

    class Pipeline(private val stages: Array[Transformer]) {
      def fit(dataset: Dataset): Transformer = {
        null
      }
    }

    class Tokenizer extends Transformer {
      override def transform(dataset: Dataset): Dataset = {
        null
      }
    }

    class HashingTF extends Transformer {
      override def transform(dataset: Dataset): Dataset = {
        null
      }
    }

    class LogisticRegression extends Transformer {
      override def transform(dataset: Dataset): Dataset = {
        null
      }
    }

    def example(): Unit = {
      val tokenizer = new Tokenizer()
      val hashingTF = new HashingTF()
      val lr = new LogisticRegression()
      val pipeline = new Pipeline(Array(tokenizer, hashingTF, lr))

      val training = new Dataset(Array("a", "b"))
      val model = pipeline.fit(training)

      val predict = new Dataset(Array("c", "d"))
      model.transform(predict)
    }

  }

  object TextRegression {
    var model: PipelineModel = _

    def fit(training: DataFrame): Unit = {
      val tokenizer = new Tokenizer()
        .setInputCol("text")
        .setOutputCol("words")
      val hashingTF = new HashingTF()
        .setNumFeatures(1000)
        .setInputCol(tokenizer.getOutputCol)
        .setOutputCol("features")
      val lr = new LogisticRegression()
        .setMaxIter(10)
        .setRegParam(0.001)
      val pipeline = new Pipeline()
        .setStages(Array(tokenizer, hashingTF, lr))
      model = pipeline.fit(training)
    }

    def transform(data: DataFrame): DataFrame = {
      model.transform(data)
    }
  }

}
