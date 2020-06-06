package aa.bb.cc.cluster.spark_ml

import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.sql.SparkSession
import org.scalatest.{Matchers, Outcome, fixture}

class LogisticRegressionExSuite extends fixture.FunSuite with Matchers {
  test("simple") { spark =>
    // https://spark.apache.org/docs/3.0.0-preview/ml-classification-regression.html#logistic-regression
    // https://machinelearningmastery.com/logistic-regression-tutorial-for-machine-learning/
    // https://mapr.com/blog/predicting-breast-cancer-using-apache-spark-machine-learning-logistic-regression/
    // https://www.kdnuggets.com/2020/03/linear-logistic-regression-explained.html
    // https://www.geeksforgeeks.org/understanding-logistic-regression/
    // https://ml-cheatsheet.readthedocs.io/en/latest/logistic_regression.html
    // https://www.scilab.org/tutorials/machine-learning-%E2%80%93-logistic-regression-tutorial
    import spark.implicits._
    // -1*x+3
    val data = Seq(
      (1.0, 1.0, 0),
      (1.0, 2.0, 0),
      (2.0, 1.0, 0),
      (3.0, 1.0, 1),
      (2.0, 2.0, 1),
      (1.0, 3.0, 1)
    )
    val dataset = data.toDF("x1", "x2", "label")
    val trainingVA = new VectorAssembler()
    trainingVA.setInputCols(Array("x1", "x2"))
    trainingVA.setOutputCol("features")
    val training = trainingVA.transform(dataset)
    val service = LogisticRegressionEx
    service.fit(training)

    val model = service.model
    println(s"Coefficients: ${model.coefficients} Intercept: ${model.intercept}")
    // Coefficients: [1.3134317637567263,1.3134317637567263] Intercept: -4.4189901192610765

    val dataPredict = Seq(
      (1.0, 1.5),
      (4.0, 2.0)
    )
    val datasetPredict = dataPredict.toDF("x1", "x2")
    val predictVA = new VectorAssembler()
    predictVA.setInputCols(Array("x1", "x2"))
    predictVA.setOutputCol("features")
    val predict = predictVA.transform(datasetPredict)
    val result = service.transform(predict)
    result.collect().foreach(r => println(r))
    // [1.0,1.5,[1.0,1.5],[1.1354107098692605,-1.1354107098692605],[0.7568360436847866,0.24316395631521334],0.0]
    // [4.0,2.0,[4.0,2.0],[-3.4616004632792814,3.4616004632792814],[0.030424785511222043,0.969575214488778],1.0]
  }

  override protected def withFixture(test: OneArgTest): Outcome = {
    val spark = SparkSession.builder().master("local[2]").getOrCreate()

    try withFixture(test.toNoArgTest(spark))

    finally spark.stop()
  }

  override type FixtureParam = SparkSession
}
