package aa.bb.cc.cluster.spark_ml

import org.apache.spark.sql.DataFrame
import org.apache.spark.ml.regression.{GBTRegressionModel, GBTRegressor}

object GBTRegressorEx {
  var model: GBTRegressionModel = _

  def fit(training: DataFrame): Unit = {
    val gbt = new GBTRegressor()
      .setMaxIter(10)
    model = gbt.fit(training)
  }

  def transform(data: DataFrame): DataFrame = {
    model.transform(data)
  }
}
