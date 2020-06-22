package aa.bb.cc.cluster.spark_ml

import org.apache.spark.sql.SparkSession
import org.scalatest.{Matchers, Outcome, fixture}

class BsClusteringSuite extends fixture.FunSuite with Matchers {
  test("simple") { spark =>
    import spark.implicits._

    val trainData = Seq(
      ("1", 1, 9),
      ("1", 2, 10),
      ("1", 3, 15),
      ("1", 4, 11),
      ("2", 1, 6),
      ("2", 2, 7),
      ("2", 3, 5),
      ("2", 4, 2),
    )
    val trainInTableName = "train_in_table"
    val trainDataset = trainData.toDF("key", "period", "k")
    trainDataset.createOrReplaceTempView(trainInTableName)

    val service = new BsClustering(spark)
    service.fit(trainInTableName)

    val testData = Seq(
      ("3", 1, 1),
      ("3", 2, 2),
      ("3", 3, 4),
      ("3", 4, 2),
      ("4", 1, 11),
      ("4", 2, 12),
      ("4", 3, 14),
      ("4", 4, 10),
    )
    val testInTableName = "test_in_table"
    val testDataset = testData.toDF("key", "period", "k")
    testDataset.createOrReplaceTempView(testInTableName)

    val result = service.transform(testInTableName)

    result.collect().foreach(r => println(r))

    // [3,WrappedArray([1,1], [2,2], [3,4], [4,2]),[1.0,2.0,4.0,2.0],[1.0,2.0,4.0,2.0],0]
    // [4,WrappedArray([1,11], [2,12], [3,14], [4,10]),[11.0,12.0,14.0,10.0],[11.0,12.0,14.0,10.0],1]
  }

  override protected def withFixture(test: OneArgTest): Outcome = {
    val spark = SparkSession.builder().master("local[2]").getOrCreate()

    try withFixture(test.toNoArgTest(spark))

    finally spark.stop()
  }

  override type FixtureParam = SparkSession

}
