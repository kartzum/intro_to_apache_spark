import sys

from pyspark.sql import SparkSession


def convert(items):
    for value in items:
        for element in value:
            yield element


def run(df):
    return sum(convert(df.groupBy().sum().collect()))


def main(args):
    result = 0
    spark = None
    try:
        spark = SparkSession.builder.enableHiveSupport().getOrCreate()
        data = [(1, 2, 0), (3, 4, 0), (7, 8, 1)]
        df = spark.createDataFrame(data=data, schema=["x1", "x2", "label"])
        r = run(df)
        print(r)
    except Exception as e:
        result = 1
    finally:
        if spark is not None:
            spark.stop()
    return result


if __name__ == "__main__":
    sys.exit(main(sys.argv[1:]))
