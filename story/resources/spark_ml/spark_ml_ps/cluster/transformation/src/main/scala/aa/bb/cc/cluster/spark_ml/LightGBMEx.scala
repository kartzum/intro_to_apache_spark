package aa.bb.cc.cluster.spark_ml

import org.apache.spark.ml.feature.VectorAssembler
import com.microsoft.ml.spark.lightgbm.LightGBMClassifier
import org.apache.spark.ml.{Pipeline, PipelineModel}
import org.apache.spark.sql._

object LightGBMEx {

  class AClassification(private val spark: SparkSession) {
    var model: PipelineModel = _

    def fit(inTableName: String): Unit = {
      val input = prepare(inTableName)
      model = train(input)
    }

    private def prepare(inTableName: String): DataFrame = {
      spark.sql(s"select * from $inTableName")
    }

    private def train(input: DataFrame): PipelineModel = {
      val assembler = new VectorAssembler()
        .setInputCols(Array("x1", "x2"))
        .setOutputCol("features")

      val lgbClf = new LightGBMClassifier()
        .setFeaturesCol("features").
        setLabelCol("label").
        setProbabilityCol("prob").
        setRawPredictionCol("raw_pred").
        setPredictionCol("predict").
        setObjective("binary").
        setIsUnbalance(true)

      val pipeline = new Pipeline().setStages(Array(assembler, lgbClf))

      pipeline.fit(input)
    }
  }

}
