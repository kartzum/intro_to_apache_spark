import unittest
import warnings

from pyspark.sql import SparkSession


class RunTest(unittest.TestCase):
    def test_run(self):
        spark = SparkSession.builder.getOrCreate()
        spark.sparkContext.setLogLevel("DEBUG")

        df = spark.range(10).filter("id = 1").selectExpr("id + 1")
        count = df.count()
        self.assertEqual(count, 1)

    def setUp(self):
        warnings.filterwarnings("ignore", category=ResourceWarning)
        warnings.filterwarnings("ignore", category=DeprecationWarning)


if __name__ == "__main__":
    unittest.main()
