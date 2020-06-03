package aa.bb.cc.cluster.spark_ml

import org.apache.spark.sql.SparkSession
import org.scalatest.{Matchers, Outcome, fixture}

class CountServiceSuite extends fixture.FunSuite with Matchers {
  test("count") { spark =>
    val data = Seq((1, 2, 3))
    import spark.implicits._
    val dataset = data.toDF("1", "2", "3")
    val r = CountService.calc(dataset)
    assert(r == 1)
  }

  override protected def withFixture(test: OneArgTest): Outcome = {
    val spark = SparkSession.builder().master("local[2]").getOrCreate()

    try withFixture(test.toNoArgTest(spark))

    finally spark.stop()
  }

  override type FixtureParam = SparkSession
}
