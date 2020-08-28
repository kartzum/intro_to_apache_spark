package aa.bb.cc.cluster.spark_ml

import org.apache.spark.sql.SparkSession
import org.scalatest.{Matchers, Outcome, fixture}

class LightGBMExSuite extends fixture.FunSuite with Matchers {
  test("simple") { spark =>
    import spark.implicits._

    val trainData = Seq(
      (-10.0, -10.0, 0.0),
      (-11.0, -21.0, 0.0),
      (-21.0, -15.0, 0.0),
      (31.0, 11.0, 1.0),
      (21.0, 21.0, 1.0),
      (41.0, 31.0, 1.0)
    )
    val trainInTableName = "train_in_table"
    val trainDataset = trainData.toDF("x1", "x2", "label")
    trainDataset.createOrReplaceTempView(trainInTableName)

    try {
      val service = new LightGBMEx.AClassification(spark)
      service.fit(trainInTableName)

      val model = service.model
      println(model.stages(1))
    }
    catch {
      case _: Throwable => println()
    }
  }

  override protected def withFixture(test: OneArgTest): Outcome = {
    val spark = SparkSession.builder().master("local[2]").getOrCreate()

    try withFixture(test.toNoArgTest(spark))

    finally spark.stop()
  }

  override type FixtureParam = SparkSession
}