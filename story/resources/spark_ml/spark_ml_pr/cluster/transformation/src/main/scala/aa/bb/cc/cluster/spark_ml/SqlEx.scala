package aa.bb.cc.cluster.spark_ml

import org.apache.spark.sql.{SparkSession, DataFrame}
import org.apache.spark.sql.functions.udf

// https://spark.apache.org/docs/latest/sql-getting-started.html
// https://jaceklaskowski.gitbooks.io/mastering-spark-sql/content/spark-sql-udfs.html
// https://www.dofactory.com/sql/join
// https://www.educba.com/join-in-spark-sql/
// https://www.oreilly.com/library/view/high-performance-spark/9781491943199/ch04.html

object SqlEx {
  def createDf(spark: SparkSession): Unit = {
    import spark.implicits._

    val data = Seq(
      ("1", 11, 19),
      ("2", 12, 10),
      ("3", 25, 100)
    )

    val dataFrame: DataFrame = data.toDF("key", "param1", "param2")

    dataFrame.show()

    /**
     * +---+------+------+
     * |key|param1|param2|
     * +---+------+------+
     * |  1|    11|    19|
     * |  2|    12|    10|
     * |  3|    25|   100|
     * +---+------+------+
     */

    def toUpperFun(value: Int): String = {
      value.toString
    }

    val toUpper: Int => String = toUpperFun

    val toUpperUdf = udf(toUpper)

    val dfWithParam2t = dataFrame.withColumn("param2_t", toUpperUdf(dataFrame("param2")))

    dfWithParam2t.show()

    /**
     * +---+------+------+--------+
     * |key|param1|param2|param2_t|
     * +---+------+------+--------+
     * |  1|    11|    19|      19|
     * |  2|    12|    10|      10|
     * |  3|    25|   100|     100|
     * +---+------+------+--------+
     */
  }

  def operations(spark: SparkSession): Unit = {
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

    val r1 = spark.sql("select t1.*, t2.* from df1 t1 join df2 t2 on t1.key=t2.key")
    r1.show()

    /**
     * +---+------+------+---+------+------+
     * |key|param1|param2|key|param1|param2|
     * +---+------+------+---+------+------+
     * |  1|    11|    19|  1|    15|   201|
     * |  2|    12|    10|  2|    21|    31|
     * +---+------+------+---+------+------+
     */

    val r2 = df1.join(df2, df1("key") === df2("key"), "rightouter")
    r2.show()

    /**
     * +----+------+------+---+------+------+
     * | key|param1|param2|key|param1|param2|
     * +----+------+------+---+------+------+
     * |null|  null|  null|  5|    97|    35|
     * |   1|    11|    19|  1|    15|   201|
     * |   2|    12|    10|  2|    21|    31|
     * +----+------+------+---+------+------+
     */
  }
}
