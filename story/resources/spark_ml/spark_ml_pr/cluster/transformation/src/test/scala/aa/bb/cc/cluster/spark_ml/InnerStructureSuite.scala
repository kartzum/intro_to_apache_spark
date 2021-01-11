package aa.bb.cc.cluster.spark_ml

import java.lang

import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}
// import org.apache.spark.sql.execution.WholeStageCodegenExec
import org.scalatest.matchers.should.Matchers
import org.scalatest.{Outcome, fixture}

// org.apache.spark.sql.execution.WholeStageCodegenSuite

class InnerStructureSuite extends fixture.FunSuite with Matchers {
  /*test("check_find") { spark =>
    val df = spark.range(10).filter("id = 1").selectExpr("id + 1")
    val plan = df.queryExecution.executedPlan
    assert(plan.find(_.isInstanceOf[WholeStageCodegenExec]).isDefined)
    assert(df.collect() === Array(Row(2)))
  }*/

  test("check_execute") { spark =>
    val df: DataFrame = spark.range(10).filter("id = 1").selectExpr("id + 1")
    val count: Long = df.count()
    assert(count == 1)

    val df1: Dataset[lang.Long] = spark.range(10)
    val df2: Dataset[lang.Long] = df1.filter("id = 1")
    val df3: DataFrame = df2.selectExpr("id + 1")
    val count2: Long = df3.count()
    assert(count2 == 1)
  }

  override protected def withFixture(test: OneArgTest): Outcome = {
    val spark = SparkSession.builder().master("local[2]").getOrCreate()

    try withFixture(test.toNoArgTest(spark))

    finally spark.stop()
  }

  override type FixtureParam = SparkSession
}
