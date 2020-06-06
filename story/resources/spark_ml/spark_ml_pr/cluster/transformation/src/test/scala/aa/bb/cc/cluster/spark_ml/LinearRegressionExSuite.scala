package aa.bb.cc.cluster.spark_ml

import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.sql.SparkSession
import org.scalatest.{Matchers, Outcome, fixture}

class LinearRegressionExSuite extends fixture.FunSuite with Matchers {
  test("simple") { spark =>
    import spark.implicits._
    // https://machinelearningmastery.com/simple-linear-regression-tutorial-for-machine-learning/
    // https://www.instaclustr.com/support/documentation/cassandra-add-ons/apache-spark/spark-mllib-linear-regression-example/
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
    val service = LinearRegressionEx
    service.fit(training)

    val model = service.model
    println(s"Coefficients: ${model.coefficients} Intercept: ${model.intercept}")
    // Coefficients: [0.8000000000000009] Intercept: 0.39999999999999675
    // y = 0.4 + 0.8 * x
    val trainingSummary = model.summary
    println(s"numIterations: ${trainingSummary.totalIterations}")
    println(s"objectiveHistory: [${trainingSummary.objectiveHistory.mkString(",")}]")
    trainingSummary.residuals.show()
    println(s"RMSE: ${trainingSummary.rootMeanSquaredError}")
    println(s"r2: ${trainingSummary.r2}")
    // numIterations: 1
    // objectiveHistory: [0.0]
    // residuals
    // ...
    // RMSE: 0.692820323027551
    // r2: 0.7272727272727272

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
    // [6,[6.0],5.200000000000003]
    // [10,[10.0],8.400000000000006]
    println(0.4 + 0.8 * 6) // 5.200000000000001
    println(0.4 + 0.8 * 10) // 8.4
  }

  override protected def withFixture(test: OneArgTest): Outcome = {
    val spark = SparkSession.builder().master("local[2]").getOrCreate()

    try withFixture(test.toNoArgTest(spark))

    finally spark.stop()
  }

  override type FixtureParam = SparkSession
}
