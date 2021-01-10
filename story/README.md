# intro_to_apache_spark

## Starting

### Main themes

* [1] Scala Basic (data structures, algorithms, functions)
* [2] Python Basic
* [3] Spark Installation
* [4] Spark Shell
* [5] Spark Basic (map reduce)
* [6] Spark Libraries
* [7] Spark ML
* [8] Spark ML Examples
* [9] Spark Streaming
* [10] Resources

## [1] Scala Basic

### Install

* [Install Scala](https://www.scala-lang.org/download/) 
* [Install IDEA](https://www.jetbrains.com/ru-ru/idea/download/)
* [Scala in browser](https://scastie.scala-lang.org/)
* [Maven](https://maven.apache.org/)

### Resources

* [Scala-resources](https://docs.scala-lang.org/getting-started/index.html) 
* [Tasks](https://www.scala-exercises.org/std_lib/asserts) 
* [scala-maven](https://github.com/mlidal/scala-maven)

### Basic structures

#### scala_basic_pr

##### Introduction
##### Basics
##### UnifiedTypes
##### Classes
##### HigherOrderFunctions
##### MultipleParameterListsCurrying
##### Collections
##### GenericClasses
##### Operators
##### Variances
##### MapReduce
##### Transformers

## [2] Python Basic

### Basic structures

#### python_basic_pr

## [3] Spark Installation

### Resources 

* [Documentation](http://spark.apache.org/docs/latest/index.html)

### Install

#### Java

* [JDK](https://www.oracle.com/java/technologies/javase-jdk8-downloads.html)
* java - help

#### Spark

* [Description](https://spark.apache.org/news/spark-3.0.0-preview.html)
* [spark-3.0.0](https://www.apache.org/dyn/closer.lua/spark/spark-3.0.0-preview2/spark-3.0.0-preview2-bin-hadoop3.2.tgz)
* Unpack tar -xvzf ./spark-3.0.0-preview2-bin-hadoop3.2.tgz
* Run ./bin/spark-shell

## [4] Spark Shell

### Resources 

* [Documentation](https://spark.apache.org/docs/latest/quick-start.html)
* [Guide-Spark](https://www.tutorialspoint.com/apache_spark/apache_spark_quick_guide.htm)
* [rdd-programming](https://spark.apache.org/docs/latest/rdd-programming-guide.html)

### Check

```
scala> val data = Array(1, 2, 3, 4, 5)
data: Array[Int] = Array(1, 2, 3, 4, 5)

scala> val distData = sc.parallelize(data)
distData: org.apache.spark.rdd.RDD[Int] = ParallelCollectionRDD[0] at parallelize at <console>:26

scala>
```

## [5] Spark Basic

### Resources 

* [rdd-programming](https://spark.apache.org/docs/latest/rdd-programming-guide.html)
* [sql-programming](https://spark.apache.org/docs/latest/sql-programming-guide.html)

### spark_ml_pr

#### RddEx
#### SqlEx

### spark_py

## [6] Spark Libraries

### Resources 

* [Documentation](https://spark.apache.org/)

## [7] Spark ML

### Resources 

* [Documentation](https://spark.apache.org/docs/latest/ml-guide.html)

### spark_ml_pr

#### LinearRegressionEx
#### LogisticRegressionEx
#### GBTClassifierEx
#### GBTRegressorEx
#### KMeansEx
#### PipelinesEx

## [8] Spark ML Examples

### Resources 

* [Documentation](https://spark.apache.org/docs/latest/ml-guide.html)

### spark_ml_pr

#### BsClustering
#### BsClassification

## [9] Spark Streaming

### Resources 

* [Documentation](https://spark.apache.org/streaming/)

### spark_ml_pr

#### StreamingEx

## [10] Resources

* [geogebra](https://www.geogebra.org/)
