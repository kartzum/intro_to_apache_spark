package aa.bb.cc.cluster.spark_ml

import org.apache.spark.sql.SparkSession
import org.scalatest.{Matchers, Outcome, fixture}

class RddExSuite extends fixture.FunSuite with Matchers {

  test("simple") { spark =>
    // RddEx.rddCreateSimple(spark)
    // RddEx.rddCreateAndCount(spark)
    RddEx.rddTAndA(spark)
  }

  override protected def withFixture(test: OneArgTest): Outcome = {
    val spark = SparkSession.builder().master("local[2]").getOrCreate()

    try withFixture(test.toNoArgTest(spark))

    finally spark.stop()
  }

  override type FixtureParam = SparkSession

}
