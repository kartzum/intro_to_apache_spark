package aa.bb.cc.cluster.spark_ml

import org.apache.spark.sql.SparkSession
import org.scalatest.{Matchers, Outcome, fixture}

class BsClassificationSuite extends fixture.FunSuite with Matchers {
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

    val service = new BsClassification(spark)
    service.fit(trainInTableName)

    val predictData = Seq(
      (-10.0, -10.0, -5.0, -10.0),
      (21.0, 21.0, 5.0, 15.0)
    )
    val predictInTableName = "predict_in_table"
    val predictDataset = predictData.toDF("x1", "x2", "x3", "x4")
    predictDataset.createOrReplaceTempView(predictInTableName)

    val result = service.transform(predictInTableName)

    result.collect().foreach(r => println(r))

    // [-10.0,-10.0,[-10.0,-10.0],[-1.1484110355377197,1.1484110355377197],[0.20882153511047363,0.7911784648895264],1.0]
    // [21.0,21.0,[21.0,21.0],[0.49817168712615967,-0.49817168712615967],[0.8766543194651604,0.12334568053483963],0.0]

    // result.show()
  }

  override protected def withFixture(test: OneArgTest): Outcome = {
    val spark = SparkSession.builder().master("local[2]").getOrCreate()

    try withFixture(test.toNoArgTest(spark))

    finally spark.stop()
  }

  override type FixtureParam = SparkSession
}
