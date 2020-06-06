package aa.bb.cc.cluster.spark_ml

import org.apache.spark.sql.DataFrame
import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.ml.classification.LogisticRegressionModel

object LogisticRegressionEx {
  var model: LogisticRegressionModel = _

  def fit(training: DataFrame): Unit = {
    val lr = new LogisticRegression()
      .setMaxIter(10)
      .setRegParam(0.1)
    model = lr.fit(training)
  }

  def transform(data: DataFrame): DataFrame = {
    model.transform(data)
  }
}
