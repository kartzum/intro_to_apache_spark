package aa.bb.cc.cluster.spark_ml

import aa.bb.cc.cluster.spark_ml.PipelinesEx.TextRegression
import org.apache.spark.ml.classification.LogisticRegressionModel
import org.apache.spark.sql.SparkSession
import org.scalatest.{Matchers, Outcome, fixture}

class PipelinesExSuite extends fixture.FunSuite with Matchers {
  test("textRegression") { spark =>
    import spark.implicits._

    val trainData = Seq(
      (0L, "a b c d e spark", 1.0),
      (1L, "b d", 0.0),
      (2L, "spark f g h", 1.0),
      (3L, "hadoop mapreduce", 0.0)
    )

    val training = trainData.toDF("id", "text", "label")

    val service = TextRegression
    service.fit(training)

    val model = service.model

    val lrModel = model.stages(2).asInstanceOf[LogisticRegressionModel]

    println(s"Coefficients: ${lrModel.coefficients} Intercept: ${lrModel.intercept}")
    // Coefficients: (1000,[165,286,467,486,494,550,585,750,768,890,979],
    // [-1.9273974102498097,3.3030922754036793,2.7804662370169932,1.6236567968545779,1.6236567968545779,2.7804662370169932,
    // -2.3659813807117143,-2.3659813807117143,2.7804662370169932,-1.9273974102498097,1.6236567968545779])
    // Intercept: -1.6421889526563507

    val predictData = Seq(
      (4L, "spark i j k"),
      (5L, "l m n"),
      (6L, "spark hadoop spark"),
      (7L, "apache hadoop")
    )

    val predict = predictData.toDF("id", "text")

    val result = service.transform(predict)
    result.select("id", "text", "probability", "prediction").collect().foreach(r => println(r))

    // [4,spark i j k,[0.15964077387874098,0.840359226121259],1.0]
    // [5,l m n,[0.8378325685476612,0.16216743145233883],0.0]
    // [6,spark hadoop spark,[0.06926633132976263,0.9307336686702373],1.0]
    // [7,apache hadoop,[0.9821575333444208,0.017842466655579155],0.0]
  }

  override protected def withFixture(test: OneArgTest): Outcome = {
    val spark = SparkSession.builder().master("local[2]").getOrCreate()

    try withFixture(test.toNoArgTest(spark))

    finally spark.stop()
  }

  override type FixtureParam = SparkSession
}
