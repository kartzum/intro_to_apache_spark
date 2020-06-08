package aa.bb.cc.cluster.spark_ml

import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.sql.SparkSession
import org.scalatest.{Matchers, Outcome, fixture}

class GBTClassifierExSuite extends fixture.FunSuite with Matchers {
  test("simple") { spark =>
    // https://spark.apache.org/docs/latest/ml-classification-regression.html#gradient-boosted-tree-classifier
    import spark.implicits._
    val data = Seq(
      (1.0, 1.0, 0),
      (1.0, 2.0, 0),
      (2.0, 1.0, 0),
      (3.0, 1.0, 1),
      (2.0, 2.0, 1),
      (1.0, 3.0, 1)
    )
    val dataset = data.toDF("x1", "x2", "label")
    val trainingVA = new VectorAssembler()
    trainingVA.setInputCols(Array("x1", "x2"))
    trainingVA.setOutputCol("features")
    val training = trainingVA.transform(dataset)
    val service = GBTClassifierEx
    service.fit(training)

    val model = service.model
    println(s"Learned classification GBT model:\n ${model.toDebugString}")
    // Learned classification GBT model:
    // GBTClassificationModel: uid = gbtc_2a53b615c5fe, numTrees=10, numClasses=2, numFeatures=2
    //  Tree 0 (weight 1.0):
    //    If (feature 0 <= 2.5)
    //  ...

    val dataPredict = Seq(
      (1.0, 1.5),
      (4.0, 2.0)
    )
    val datasetPredict = dataPredict.toDF("x1", "x2")
    val predictVA = new VectorAssembler()
    predictVA.setInputCols(Array("x1", "x2"))
    predictVA.setOutputCol("features")
    val predict = predictVA.transform(datasetPredict)
    val result = service.transform(predict)
    result.collect().foreach(r => println(r))
    // [1.0,1.5,[1.0,1.5],[1.325902679220332,-1.325902679220332],[0.9341221756527827,0.06587782434721734],0.0]
    //[4.0,2.0,[4.0,2.0],[-1.325902679220332,1.325902679220332],[0.06587782434721742,0.9341221756527825],1.0]
  }

  override protected def withFixture(test: OneArgTest): Outcome = {
    val spark = SparkSession.builder().master("local[2]").getOrCreate()

    try withFixture(test.toNoArgTest(spark))

    finally spark.stop()
  }

  override type FixtureParam = SparkSession
}
