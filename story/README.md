# intro_to_apache_spark

# Приступая к работе

## Основные темы

* Scala Basic (data structures, algorithms, functions)
* Spark Installation
* Spark Shell
* Spark Basic (map reduce)
* Spark Libraries
* Spark ML
* Spark ML Examples
* Spark Streaming

# Scala Basic

## Установка

* [Установка Scala](https://www.scala-lang.org/download/) 
* [Установка IDEA](https://www.jetbrains.com/ru-ru/idea/download/)
* [Scala в браузере](https://scastie.scala-lang.org/)
* [Maven](https://maven.apache.org/)

## Материалы для изучения

* [Ресурсы для изучения от разработчиков](https://docs.scala-lang.org/getting-started/index.html) 
* [Упражнения для обучения](https://www.scala-exercises.org/std_lib/asserts) 
* [scala-maven](https://github.com/mlidal/scala-maven)

## Базовые структуры языка

### scala_basic_pr

#### Introduction
#### Basics
#### UnifiedTypes
#### Classes
#### HigherOrderFunctions
#### MultipleParameterListsCurrying
#### Collections
#### GenericClasses
#### Operators
#### Variances
#### MapReduce
#### Transformers

# Spark Installation

## Материалы для изучения 

* [Документация по установке Spark](http://spark.apache.org/docs/latest/index.html)

## Установка

### Java

* [JDK](https://www.oracle.com/java/technologies/javase-jdk8-downloads.html)
* java - help

### Spark

* [Описание](https://spark.apache.org/news/spark-3.0.0-preview.html)
* [spark-3.0.0](https://www.apache.org/dyn/closer.lua/spark/spark-3.0.0-preview2/spark-3.0.0-preview2-bin-hadoop3.2.tgz)
* Распокавать tar -xvzf ./spark-3.0.0-preview2-bin-hadoop3.2.tgz
* Запустить ./bin/spark-shell

# Spark Shell

## Материалы для изучения 

* [Документация](https://spark.apache.org/docs/latest/quick-start.html)
* [Знакомство со Spark](https://www.tutorialspoint.com/apache_spark/apache_spark_quick_guide.htm)
* [rdd-programming](https://spark.apache.org/docs/latest/rdd-programming-guide.html)

### Работа

```
scala> val data = Array(1, 2, 3, 4, 5)
data: Array[Int] = Array(1, 2, 3, 4, 5)

scala> val distData = sc.parallelize(data)
distData: org.apache.spark.rdd.RDD[Int] = ParallelCollectionRDD[0] at parallelize at <console>:26

scala>
```

# Spark Basic

## Материалы для изучения 

* [rdd-programming](https://spark.apache.org/docs/latest/rdd-programming-guide.html)
* [sql-programming](https://spark.apache.org/docs/latest/sql-programming-guide.html)

### spark_ml_pr

#### RddEx

# Spark Libraries

## Материалы для изучения 

* [Документация](https://spark.apache.org/)

# Spark ML

## Материалы для изучения 

* [Документация](https://spark.apache.org/docs/latest/ml-guide.html)

### spark_ml_pr

#### LinearRegressionEx
#### LogisticRegressionEx
#### GBTClassifierEx
#### GBTRegressorEx
#### KMeansEx

# Spark ML Examples

## Материалы для изучения 

* [Документация](https://spark.apache.org/docs/latest/ml-guide.html)

### spark_ml_pr

#### BsClustering

# Spark Streaming

## Материалы для изучения 

* [Документация](https://spark.apache.org/streaming/)

# Ресурсы

* [geogebra](https://www.geogebra.org/)
