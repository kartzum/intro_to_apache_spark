package aa.bb.cc.cluster.spark_ml

import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.sql.SparkSession
import org.scalatest.{Matchers, Outcome, fixture}

class KMeansExSuite extends fixture.FunSuite with Matchers {
  test("simple") { spark =>
    // https://spark.apache.org/docs/latest/ml-clustering.html#k-means
    // https://github.com/freeman-lab/spark-ml-streaming
    import spark.implicits._
    val data = Seq(
      (6, 10),
      (7, 11),
      (7, 12),
      (-6, -10),
      (-8, -13),
      (-7, -12)
    )
    val dataset = data.toDF("x1", "x2")
    val trainingVA = new VectorAssembler()
    trainingVA.setInputCols(Array("x1", "x2"))
    trainingVA.setOutputCol("features")
    val training = trainingVA.transform(dataset)
    val service = KMeansEx
    service.fit(training)

    val model = service.model
    model.clusterCenters.foreach(println)
    // [-7.0,-11.666666666666666]
    //[6.666666666666666,11.0]

    val dataPredict = Seq(
      (8, 13),
      (-9, -14)
    )
    val datasetPredict = dataPredict.toDF("x1", "x2")
    val predictVA = new VectorAssembler()
    predictVA.setInputCols(Array("x1", "x2"))
    predictVA.setOutputCol("features")
    val predict = predictVA.transform(datasetPredict)
    val result = service.transform(predict)
    result.collect().foreach(r => println(r))
    // [8,13,[8.0,13.0],1]
    //[-9,-14,[-9.0,-14.0],0]
  }

  override protected def withFixture(test: OneArgTest): Outcome = {
    val spark = SparkSession.builder().master("local[2]").getOrCreate()

    try withFixture(test.toNoArgTest(spark))

    finally spark.stop()
  }

  override type FixtureParam = SparkSession
}
