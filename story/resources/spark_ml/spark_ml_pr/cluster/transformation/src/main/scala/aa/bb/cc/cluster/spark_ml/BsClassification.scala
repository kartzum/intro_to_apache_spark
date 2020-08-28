package aa.bb.cc.cluster.spark_ml

import org.apache.spark.sql.{DataFrame, SparkSession}
import ml.dmlc.xgboost4j.scala.spark.XGBoostClassifier
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.ml.{Pipeline, PipelineModel}

class BsClassification(private val spark: SparkSession) {
  var model: PipelineModel = _

  def fit(inTableName: String): Unit = {
    val input = prepare(inTableName)
    model = train(input)
  }

  def transform(inTableName: String): DataFrame = {
    val input = prepare(inTableName)
    model.transform(input)
  }

  private def prepare(inTableName: String): DataFrame = {
    spark.sql(s"select * from $inTableName")
  }

  private def train(input: DataFrame): PipelineModel = {
    val assembler = new VectorAssembler()
      .setInputCols(Array("x1", "x2", "x3", "x4"))
      .setOutputCol("features")

    val booster = new XGBoostClassifier(
      Map("eta" -> 0.1f,
        "max_depth" -> 3,
        "objective" -> "multi:softprob",
        "num_class" -> 2,
        "num_round" -> 100,
        "num_workers" -> 2,
        "tree_method" -> "hist"
      )
    )

    booster.setFeaturesCol("features")
    booster.setLabelCol("label")

    val pipeline = new Pipeline()
      .setStages(Array(assembler, booster))

    pipeline.fit(input)
  }

}
