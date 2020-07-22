package aa.bb.cc.cluster.spark_ml

import org.apache.spark.sql.SparkSession

// https://spark.apache.org/examples.html

object RddEx {
  val logsData = Seq(
    "time1\t message1 server1\t error",
    "time2\t message2 server2\t info",
    "time3\t message3 server3\t info",
    "time4\t message4 server2\t error",
    "time5\t message5 server1\t info",
    "time6\t message6 server1\t error"
  )

  def rddCreateSimple(spark: SparkSession): Unit = {
    val data = Seq(
      "С C++ Java C# Assembler",
      "Python Assembler Java Objective-C Swift",
      "Kotlin C++ Java"
    )
    val rdd = spark.sparkContext.parallelize(data)

    val counts = rdd.flatMap(line => line.split(" "))
      .map(word => (word, 1))
      .reduceByKey(_ + _)

    counts.collect().foreach(x => {
      println(x)
    })
  }

  def rddCreateAndCount(spark: SparkSession): Unit = {
    val data = spark.sparkContext.parallelize(logsData)

    val errorCount = data.map(line => line.contains("error")).filter(f => f).count()

    print(errorCount) // 2
  }

  def rddTAndA(spark: SparkSession): Unit = {
    val data = spark.sparkContext.parallelize(logsData)

    // Transformations.
    val errors = data.filter(line => line.contains("error"))
    val messages = errors.map(line => {
      val lines = line.split("\t")
      lines(1)
    })

    // Actions.
    val errorMessagesFromServer1 = messages.filter(_.contains("server1"))
    val count1 = errorMessagesFromServer1.count()
    val errorMessagesFromServer2 = messages.filter(_.contains("server2"))
    val count2 = errorMessagesFromServer2.count()

    println(count1) // 2
    println(count2) // 1
  }
}
