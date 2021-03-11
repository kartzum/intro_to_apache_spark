package aa.bb.cc.cluster.spark_ml

import org.apache.spark.sql.SparkSession
import org.scalatest.{Outcome, fixture}

class SamplesSuite extends fixture.FunSuite with org.scalatest.matchers.should.Matchers {

  test("rdd1") { spark =>
    val rdd1 = spark.sparkContext.makeRDD(Seq(1, 2, 3));
    rdd1.collect().map(x => println(x))
  }

  test("rdd2") { spark =>
    val rdd1 = spark.sparkContext.makeRDD(Seq("1_a", "2_b", "3_c"))
    val rdd1Transformed = rdd1.map(x => {
      val t = x.split("_")
      (t(0).toInt, t(1))
    }).filter(a => a._1 % 2 == 0)
    rdd1Transformed.foreach(a => println(a._1 + ", " + a._2))
  }

  test("rdd3") { spark =>
    val s1 = Seq((1, "a"), (2, "b"), (3, "c"))
    val s2 = Seq((1, "a"), (3, "b"), (5, "c"))
    val rdd1 = spark.sparkContext.makeRDD(s1)
    val rdd2 = spark.sparkContext.makeRDD(s2)
    val rdd3 = rdd1.join(rdd2)
    rdd3.foreach(r => println(r))
  }

  test("df1") { spark =>
    import spark.implicits._

    val data1 = Seq(
      ("1", 11, 19),
      ("2", 12, 10),
      ("3", 25, 100)
    )
    val df1 = spark.sparkContext.parallelize(data1).toDF("key", "param1", "param2")
    df1.createOrReplaceTempView("df1")

    val data2 = Seq(
      ("1", 15, 201),
      ("2", 21, 31),
      ("5", 97, 35)
    )
    val df2 = spark.sparkContext.parallelize(data2).toDF("key", "param1", "param2")
    df2.createOrReplaceTempView("df2")

    val r1 = spark.sql("select t1.key, t1.param1, t1.param2, t2.param1, t2.param2 from df1 t1 join df2 t2 on t1.key=t2.key")
    r1.show()

    //+---+------+------+------+------+
    //|key|param1|param2|param1|param2|
    //+---+------+------+------+------+
    //|  1|    11|    19|    15|   201|
    //|  2|    12|    10|    21|    31|
    //+---+------+------+------+------+
  }

  override protected def withFixture(test: OneArgTest): Outcome = {
    val spark = SparkSession.builder().master("local[2]").getOrCreate()

    try withFixture(test.toNoArgTest(spark))

    finally spark.stop()
  }

  override type FixtureParam = SparkSession

}
