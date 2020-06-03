package aa.bb.cc.cluster.spark_ml

import org.apache.spark.sql.DataFrame

object CountService {
  def calc(dataset: DataFrame): Long = {
    dataset.collectAsList().size()
  }
}