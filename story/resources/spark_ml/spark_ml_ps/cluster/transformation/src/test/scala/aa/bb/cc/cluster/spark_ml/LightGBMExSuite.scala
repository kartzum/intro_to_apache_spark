package aa.bb.cc.cluster.spark_ml

import org.apache.spark.sql.SparkSession
import org.scalatest.{Matchers, Outcome, fixture}

class LightGBMExSuite extends fixture.FunSuite with Matchers {
  test("simple") { spark =>
    import spark.implicits._

    val trainData = Seq(
      (-10.0, -10.0, -5.0, -11.0, 0),
      (-11.0, -21.0, -10.0, -12.0, 0),
      (-21.0, -15.0, -13.0, -15.0, 0),
      (31.0, 11.0, 5.0, 7.0, 1),
      (21.0, 21.0, 20.0, 18.0, 1),
      (41.0, 31.0, 30.1, 29.0, 1)
    )
    val trainInTableName = "train_in_table"
    val trainDataset = trainData.toDF("x1", "x2", "x3", "x4", "label")
    trainDataset.createOrReplaceTempView(trainInTableName)

    try {
      val service = new LightGBMEx.AClassification(spark)
      service.fit(trainInTableName)

      val model = service.model
      println(model.stages(1))

      val predictData = Seq(
        (-10.0, -10.0, -5.0, -10.0),
        (21.0, 21.0, 5.0, 15.0)
      )
      val predictInTableName = "predict_in_table"
      val predictDataset = predictData.toDF("x1", "x2", "x3", "x4")
      predictDataset.createOrReplaceTempView(predictInTableName)

      val result = service.transform(predictInTableName)

      result.collect().foreach(r => println(r))
    }
    catch {
      case _: Throwable => println()
    }
  }

  override protected def withFixture(test: OneArgTest): Outcome = {
    val spark = SparkSession.builder().master("local[1]").getOrCreate()

    try withFixture(test.toNoArgTest(spark))

    finally spark.stop()
  }

  override type FixtureParam = SparkSession
}