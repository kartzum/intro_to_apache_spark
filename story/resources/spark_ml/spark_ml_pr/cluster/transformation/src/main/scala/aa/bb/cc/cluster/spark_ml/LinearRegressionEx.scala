package aa.bb.cc.cluster.spark_ml

import org.apache.spark.ml.regression.{LinearRegression, LinearRegressionModel}
import org.apache.spark.sql.DataFrame

object LinearRegressionEx {
  var model: LinearRegressionModel = _

  def fit(training: DataFrame): Unit = {
    val lr = new LinearRegression()
      .setMaxIter(10)
    model = lr.fit(training)
  }

  def predict(features: org.apache.spark.ml.linalg.Vector): Double = {
    model.predict(features)
  }

  def transform(data: DataFrame): DataFrame = {
    model.transform(data)
  }
}
