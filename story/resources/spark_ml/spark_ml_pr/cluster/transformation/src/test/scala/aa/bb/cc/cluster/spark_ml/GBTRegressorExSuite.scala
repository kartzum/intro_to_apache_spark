package aa.bb.cc.cluster.spark_ml

import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.sql.SparkSession
import org.scalatest.{Matchers, Outcome, fixture}

class GBTRegressorExSuite extends fixture.FunSuite with Matchers {
  test("simple") { spark =>
    // https://spark.apache.org/docs/latest/ml-classification-regression.html#gradient-boosted-tree-regression
    import spark.implicits._
    val data = Seq(
      (1, 1),
      (2, 3),
      (4, 3),
      (3, 2),
      (5, 5)
    )
    val dataset = data.toDF("x", "label")
    val trainingVA = new VectorAssembler()
    trainingVA.setInputCols(Array("x"))
    trainingVA.setOutputCol("features")
    val training = trainingVA.transform(dataset)
    val service = GBTRegressorEx
    service.fit(training)

    val model = service.model
    println(s"Learned regression GBT model:\n ${model.toDebugString}")

    val dataPredict = Seq(
      (6),
      (10)
    )
    val datasetPredict = dataPredict.toDF("x")
    val predictVA = new VectorAssembler()
    predictVA.setInputCols(Array("x"))
    predictVA.setOutputCol("features")
    val predict = predictVA.transform(datasetPredict)
    val result = service.transform(predict)
    result.collect().foreach(r => println(r))
    // [6,[6.0],5.0]
    //[10,[10.0],5.0]
  }

  override protected def withFixture(test: OneArgTest): Outcome = {
    val spark = SparkSession.builder().master("local[2]").getOrCreate()

    try withFixture(test.toNoArgTest(spark))

    finally spark.stop()
  }

  override type FixtureParam = SparkSession
}
