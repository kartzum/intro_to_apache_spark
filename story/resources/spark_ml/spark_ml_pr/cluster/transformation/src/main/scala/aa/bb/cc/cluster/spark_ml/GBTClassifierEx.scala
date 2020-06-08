package aa.bb.cc.cluster.spark_ml

import org.apache.spark.sql.DataFrame
import org.apache.spark.ml.classification.{GBTClassificationModel, GBTClassifier}

object GBTClassifierEx {
  var model: GBTClassificationModel = _

  def fit(training: DataFrame): Unit = {
    val gbt = new GBTClassifier()
      .setMaxIter(10)
    model = gbt.fit(training)
  }

  def transform(data: DataFrame): DataFrame = {
    model.transform(data)
  }
}
