import unittest
import warnings

from pyspark.sql import SparkSession

from spark_py import start


class RunTest(unittest.TestCase):
    def test_run(self):
        spark = SparkSession.builder.getOrCreate()

        data = [(1, 2, 0), (3, 4, 0), (7, 8, 1)]
        df = spark.createDataFrame(data=data, schema=["x1", "x2", "label"])

        r = start.run(df)

        self.assertEqual(r, 26.0)

    def setUp(self):
        warnings.filterwarnings("ignore", category=ResourceWarning)
        warnings.filterwarnings("ignore", category=DeprecationWarning)


if __name__ == "__main__":
    unittest.main()
