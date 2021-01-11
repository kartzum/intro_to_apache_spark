# spark_ml_pr

## SparkSql + RDD - inner structures

### Notes

See: InnerStructureSuite

---

Sample.

```scala
val df: DataFrame = spark.range(10).filter("id = 1").selectExpr("id + 1")
val count: Long = df.count()
```

---

org.apache.spark.sql.Dataset

```scala
/**
* Returns the number of rows in the Dataset.
* @group action
* @since 1.6.0
*/
def count(): Long = withAction("count", groupBy().count().queryExecution) { plan =>
plan.executeCollect().head.getLong(0)
}
```

```scala
/**
* Wrap a Dataset action to track the QueryExecution and time cost, then report to the
* user-registered callback functions.
*/
private def withAction[U](name: String, qe: QueryExecution)(action: SparkPlan => U) = {
SQLExecution.withNewExecutionId(qe, Some(name)) {
  qe.executedPlan.resetMetrics()
  action(qe.executedPlan)
}
}
```

---
```
count() -> withAction: SparkPlan -> executeCollect() from SparkPlan (.head.getLong(0))
```
---

org.apache.spark.sql.execution.SparkPlan

```scala
/**
* Runs this query returning the result as an array.
*/
def executeCollect(): Array[InternalRow] = {
val byteArrayRdd = getByteArrayRdd()

val results = ArrayBuffer[InternalRow]()
byteArrayRdd.collect().foreach { countAndBytes =>
  decodeUnsafeRows(countAndBytes._2).foreach(results.+=)
}
results.toArray
}
```

```scala
val byteArrayRdd = getByteArrayRdd()
```
```
byteArrayRdd: org.apache.spark.rdd.MapPartitionsRDD
```

```scala
/**
   * Packing the UnsafeRows into byte array for faster serialization.
   * The byte arrays are in the following format:
   * [size] [bytes of UnsafeRow] [size] [bytes of UnsafeRow] ... [-1]
   *
   * UnsafeRow is highly compressible (at least 8 bytes for any column), the byte array is also
   * compressed.
   */
  private def getByteArrayRdd(
      n: Int = -1, takeFromEnd: Boolean = false): RDD[(Long, Array[Byte])] = {
    execute().mapPartitionsInternal { iter =>
      var count = 0
      val buffer = new Array[Byte](4 << 10)  // 4K
      val codec = CompressionCodec.createCodec(SparkEnv.get.conf)
      val bos = new ByteArrayOutputStream()
      val out = new DataOutputStream(codec.compressedOutputStream(bos))

      if (takeFromEnd && n > 0) {
        // To collect n from the last, we should anyway read everything with keeping the n.
        // Otherwise, we don't know where is the last from the iterator.
        var last: Seq[UnsafeRow] = Seq.empty[UnsafeRow]
        val slidingIter = iter.map(_.copy()).sliding(n)
        while (slidingIter.hasNext) { last = slidingIter.next().asInstanceOf[Seq[UnsafeRow]] }
        var i = 0
        count = last.length
        while (i < count) {
          val row = last(i)
          out.writeInt(row.getSizeInBytes)
          row.writeToStream(out, buffer)
          i += 1
        }
      } else {
        // `iter.hasNext` may produce one row and buffer it, we should only call it when the
        // limit is not hit.
        while ((n < 0 || count < n) && iter.hasNext) {
          val row = iter.next().asInstanceOf[UnsafeRow]
          out.writeInt(row.getSizeInBytes)
          row.writeToStream(out, buffer)
          count += 1
        }
      }
      out.writeInt(-1)
      out.flush()
      out.close()
      Iterator((count, bos.toByteArray))
    }
  }
```

```scala
/**
* Returns the result of this query as an RDD[InternalRow] by delegating to `doExecute` after
* preparations.
*
* Concrete implementations of SparkPlan should override `doExecute`.
*/
final def execute(): RDD[InternalRow] = executeQuery {
if (isCanonicalizedPlan) {
  throw new IllegalStateException("A canonicalized plan is not supposed to be executed.")
}
doExecute()
}
```

SparkPlan: org.apache.spark.sql.execution.WholeStageCodegenExec

---

org.apache.spark.sql.execution.WholeStageCodegenExec

```scala

override def doExecute(): RDD[InternalRow] = {
    val (ctx, cleanedSource) = doCodeGen()
    // try to compile and fallback if it failed
    val (_, compiledCodeStats) = try {
      CodeGenerator.compile(cleanedSource)
    } catch {
      case NonFatal(_) if !Utils.isTesting && sqlContext.conf.codegenFallback =>
        // We should already saw the error message
        logWarning(s"Whole-stage codegen disabled for plan (id=$codegenStageId):\n $treeString")
        return child.execute()
    }

    // Check if compiled code has a too large function
    if (compiledCodeStats.maxMethodCodeSize > sqlContext.conf.hugeMethodLimit) {
      logInfo(s"Found too long generated codes and JIT optimization might not work: " +
        s"the bytecode size (${compiledCodeStats.maxMethodCodeSize}) is above the limit " +
        s"${sqlContext.conf.hugeMethodLimit}, and the whole-stage codegen was disabled " +
        s"for this plan (id=$codegenStageId). To avoid this, you can raise the limit " +
        s"`${SQLConf.WHOLESTAGE_HUGE_METHOD_LIMIT.key}`:\n$treeString")
      return child.execute()
    }

    val references = ctx.references.toArray

    val durationMs = longMetric("pipelineTime")

    // Even though rdds is an RDD[InternalRow] it may actually be an RDD[ColumnarBatch] with
    // type erasure hiding that. This allows for the input to a code gen stage to be columnar,
    // but the output must be rows.
    val rdds = child.asInstanceOf[CodegenSupport].inputRDDs()
    assert(rdds.size <= 2, "Up to two input RDDs can be supported")
    if (rdds.length == 1) {
      rdds.head.mapPartitionsWithIndex { (index, iter) =>
        val (clazz, _) = CodeGenerator.compile(cleanedSource)
        val buffer = clazz.generate(references).asInstanceOf[BufferedRowIterator]
        buffer.init(index, Array(iter))
        new Iterator[InternalRow] {
          override def hasNext: Boolean = {
            val v = buffer.hasNext
            if (!v) durationMs += buffer.durationMs()
            v
          }
          override def next: InternalRow = buffer.next()
        }
      }
    } else {
      // Right now, we support up to two input RDDs.
      rdds.head.zipPartitions(rdds(1)) { (leftIter, rightIter) =>
        Iterator((leftIter, rightIter))
        // a small hack to obtain the correct partition index
      }.mapPartitionsWithIndex { (index, zippedIter) =>
        val (leftIter, rightIter) = zippedIter.next()
        val (clazz, _) = CodeGenerator.compile(cleanedSource)
        val buffer = clazz.generate(references).asInstanceOf[BufferedRowIterator]
        buffer.init(index, Array(leftIter, rightIter))
        new Iterator[InternalRow] {
          override def hasNext: Boolean = {
            val v = buffer.hasNext
            if (!v) durationMs += buffer.durationMs()
            v
          }
          override def next: InternalRow = buffer.next()
        }
      }
    }
  }

```

```scala
rdds.head.mapPartitionsWithIndex { (index, iter) =>
    val (clazz, _) = CodeGenerator.compile(cleanedSource)
    val buffer = clazz.generate(references).asInstanceOf[BufferedRowIterator]
    buffer.init(index, Array(iter))
    new Iterator[InternalRow] {
      override def hasNext: Boolean = {
        val v = buffer.hasNext
        if (!v) durationMs += buffer.durationMs()
        v
      }
      override def next: InternalRow = buffer.next()
    }
}
```

```java
public Object generate(Object[] references) {
return new GeneratedIteratorForCodegenStage1(references);
}

/*wsc_codegenStageId*/
final class GeneratedIteratorForCodegenStage1 extends org.apache.spark.sql.execution.BufferedRowIterator {
private Object[] references;
private scala.collection.Iterator[] inputs;
private boolean agg_initAgg_0;
private boolean agg_bufIsNull_0;
private long agg_bufValue_0;
private boolean range_initRange_0;
private long range_nextIndex_0;
private TaskContext range_taskContext_0;
private InputMetrics range_inputMetrics_0;
private long range_batchEnd_0;
private long range_numElementsTodo_0;
private org.apache.spark.sql.catalyst.expressions.codegen.UnsafeRowWriter[] range_mutableStateArray_0 = new org.apache.spark.sql.catalyst.expressions.codegen.UnsafeRowWriter[3];

public GeneratedIteratorForCodegenStage1(Object[] references) {
this.references = references;
}

public void init(int index, scala.collection.Iterator[] inputs) {
partitionIndex = index;
this.inputs = inputs;

range_taskContext_0 = TaskContext.get();
range_inputMetrics_0 = range_taskContext_0.taskMetrics().inputMetrics();
range_mutableStateArray_0[0] = new org.apache.spark.sql.catalyst.expressions.codegen.UnsafeRowWriter(1, 0);
range_mutableStateArray_0[1] = new org.apache.spark.sql.catalyst.expressions.codegen.UnsafeRowWriter(1, 0);
range_mutableStateArray_0[2] = new org.apache.spark.sql.catalyst.expressions.codegen.UnsafeRowWriter(1, 0);

}

private void agg_doAggregateWithoutKey_0() throws java.io.IOException {
// initialize aggregation buffer
agg_bufIsNull_0 = false;
agg_bufValue_0 = 0L;

// initialize Range
if (!range_initRange_0) {
range_initRange_0 = true;
initRange(partitionIndex);
}

while (true) {
if (range_nextIndex_0 == range_batchEnd_0) {
long range_nextBatchTodo_0;
if (range_numElementsTodo_0 > 1000L) {
range_nextBatchTodo_0 = 1000L;
range_numElementsTodo_0 -= 1000L;
} else {
range_nextBatchTodo_0 = range_numElementsTodo_0;
range_numElementsTodo_0 = 0;
if (range_nextBatchTodo_0 == 0) break;
}
range_batchEnd_0 += range_nextBatchTodo_0 * 1L;
}

int range_localEnd_0 = (int)((range_batchEnd_0 - range_nextIndex_0) / 1L);
for (int range_localIdx_0 = 0; range_localIdx_0 < range_localEnd_0; range_localIdx_0++) {
long range_value_0 = ((long)range_localIdx_0 * 1L) + range_nextIndex_0;

do {
boolean filter_value_0 = false;
filter_value_0 = range_value_0 == 1L;
if (!filter_value_0) continue;

((org.apache.spark.sql.execution.metric.SQLMetric) references[1] /* numOutputRows */).add(1);

agg_doConsume_0();

} while(false);

// shouldStop check is eliminated
}
range_nextIndex_0 = range_batchEnd_0;
((org.apache.spark.sql.execution.metric.SQLMetric) references[0] /* numOutputRows */).add(range_localEnd_0);
range_inputMetrics_0.incRecordsRead(range_localEnd_0);
range_taskContext_0.killTaskIfInterrupted();
}

}

private void initRange(int idx) {
java.math.BigInteger index = java.math.BigInteger.valueOf(idx);
java.math.BigInteger numSlice = java.math.BigInteger.valueOf(2L);
java.math.BigInteger numElement = java.math.BigInteger.valueOf(10L);
java.math.BigInteger step = java.math.BigInteger.valueOf(1L);
java.math.BigInteger start = java.math.BigInteger.valueOf(0L);
long partitionEnd;

java.math.BigInteger st = index.multiply(numElement).divide(numSlice).multiply(step).add(start);
if (st.compareTo(java.math.BigInteger.valueOf(Long.MAX_VALUE)) > 0) {
range_nextIndex_0 = Long.MAX_VALUE;
} else if (st.compareTo(java.math.BigInteger.valueOf(Long.MIN_VALUE)) < 0) {
range_nextIndex_0 = Long.MIN_VALUE;
} else {
range_nextIndex_0 = st.longValue();
}
range_batchEnd_0 = range_nextIndex_0;

java.math.BigInteger end = index.add(java.math.BigInteger.ONE).multiply(numElement).divide(numSlice)
.multiply(step).add(start);
if (end.compareTo(java.math.BigInteger.valueOf(Long.MAX_VALUE)) > 0) {
partitionEnd = Long.MAX_VALUE;
} else if (end.compareTo(java.math.BigInteger.valueOf(Long.MIN_VALUE)) < 0) {
partitionEnd = Long.MIN_VALUE;
} else {
partitionEnd = end.longValue();
}

java.math.BigInteger startToEnd = java.math.BigInteger.valueOf(partitionEnd).subtract(
java.math.BigInteger.valueOf(range_nextIndex_0));
range_numElementsTodo_0  = startToEnd.divide(step).longValue();
if (range_numElementsTodo_0 < 0) {
range_numElementsTodo_0 = 0;
} else if (startToEnd.remainder(step).compareTo(java.math.BigInteger.valueOf(0L)) != 0) {
range_numElementsTodo_0++;
}
}

private void agg_doConsume_0() throws java.io.IOException {
// do aggregate
// common sub-expressions

// evaluate aggregate functions and update aggregation buffers

long agg_value_1 = -1L;

agg_value_1 = agg_bufValue_0 + 1L;

agg_bufIsNull_0 = false;
agg_bufValue_0 = agg_value_1;

}

protected void processNext() throws java.io.IOException {
while (!agg_initAgg_0) {
agg_initAgg_0 = true;
long agg_beforeAgg_0 = System.nanoTime();
agg_doAggregateWithoutKey_0();
((org.apache.spark.sql.execution.metric.SQLMetric) references[3] /* aggTime */).add((System.nanoTime() - agg_beforeAgg_0) / 1000000);

// output the result

((org.apache.spark.sql.execution.metric.SQLMetric) references[2] /* numOutputRows */).add(1);
range_mutableStateArray_0[2].reset();

range_mutableStateArray_0[2].zeroOutNullBytes();

range_mutableStateArray_0[2].write(0, agg_bufValue_0);
append((range_mutableStateArray_0[2].getRow()));
}
}

}
```

org.apache.spark.rdd.RDD

```scala
/**
* Return a new RDD by applying a function to each partition of this RDD, while tracking the index
* of the original partition.
*
* `preservesPartitioning` indicates whether the input function preserves the partitioner, which
* should be `false` unless this is a pair RDD and the input function doesn't modify the keys.
*/
def mapPartitionsWithIndex[U: ClassTag](
  f: (Int, Iterator[T]) => Iterator[U],
  preservesPartitioning: Boolean = false): RDD[U] = withScope {
val cleanedF = sc.clean(f)
new MapPartitionsRDD(
  this,
  (_: TaskContext, index: Int, iter: Iterator[T]) => cleanedF(index, iter),
  preservesPartitioning)
}
```

---

```scala
// `iter.hasNext` may produce one row and buffer it, we should only call it when the
// limit is not hit.
while ((n < 0 || count < n) && iter.hasNext) {
  val row = iter.next().asInstanceOf[UnsafeRow]
  out.writeInt(row.getSizeInBytes)
  row.writeToStream(out, buffer)
  count += 1
}
```

---