package aa.bb.cc.cluster.spark_ml

import org.apache.spark.ml.clustering.{KMeans, KMeansModel}
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.catalyst.expressions.GenericRowWithSchema
import org.apache.spark.sql.functions.udf
import org.apache.spark.ml.linalg.Vectors
import org.apache.spark.ml.linalg.Vector

import scala.collection.mutable

// https://cwiki.apache.org/confluence/display/Hive/LanguageManual+UDF
// https://spark.apache.org/docs/latest/ml-features#vectorassembler


class BsClustering(private val spark: SparkSession) {
  private var model: KMeansModel = _

  def fit(inTableName: String): Unit = {
    val input = prepare(inTableName)
    model = train(input)
  }

  def transform(inTableName: String): DataFrame = {
    val input = prepare(inTableName)
    model.transform(input)
  }

  private def prepare(inTableName: String): DataFrame = {
    def transformXFun(x: mutable.WrappedArray[GenericRowWithSchema]): Vector = {
      Vectors.dense(x.map(y => (y.getInt(0), y.getInt(1))).sortBy(_._1).map(y => y._2.toDouble).toArray)
    }

    val transformX: mutable.WrappedArray[GenericRowWithSchema] => Vector = transformXFun

    val transformXUdf = udf(transformX)

    val ds = spark.sql(s"select t.key, collect_list((t.period, t.k)) k_list from $inTableName t group by t.key")
    val dsKListTransformed = ds.withColumn("k_list_transformed", transformXUdf(ds("k_list")))

    val assembler = new VectorAssembler()
      .setInputCols(Array("k_list_transformed"))
      .setOutputCol("features")

    assembler.transform(dsKListTransformed)
  }

  private def train(input: DataFrame): KMeansModel = {
    val kmeans = new KMeans().setK(2).setSeed(1L)
    kmeans.fit(input)
  }
}
