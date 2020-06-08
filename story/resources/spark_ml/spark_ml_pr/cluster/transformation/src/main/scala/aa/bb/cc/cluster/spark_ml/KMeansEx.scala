package aa.bb.cc.cluster.spark_ml

import org.apache.spark.ml.clustering.{KMeans, KMeansModel}
import org.apache.spark.sql.DataFrame

object KMeansEx {
  var model: KMeansModel = _

  def fit(training: DataFrame): Unit = {
    val kmeans = new KMeans().setK(2).setSeed(1L)
    model = kmeans.fit(training)
  }

  def transform(data: DataFrame): DataFrame = {
    model.transform(data)
  }
}
