/*
 * Copyright (c) 2017 Ramazan AYYILDIZ
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package ai.ayyildiz.examples.ml

import ai.ayyildiz.SparkSupport
import org.apache.spark.ml.feature.Bucketizer

/** Created by rayyildiz on 6/12/2017.
  */
object BucketizerExample extends App with SparkSupport {

  val splits =
    Array(Double.NegativeInfinity, -0.5, 0.0, 0.5, Double.PositiveInfinity)

  val data      = Array(-999.9, -0.5, -0.3, 0.0, 0.2, 999.9)
  val dataFrame = spark.createDataFrame(data.map(Tuple1.apply)).toDF("features")

  val bucketizer = new Bucketizer()
    .setInputCol("features")
    .setOutputCol("bucketed_features")
    .setSplits(splits)

  // Transform original data into its bucket index.
  val bucketedDF = bucketizer.transform(dataFrame)

  log.info(s"Bucketizer output with ${bucketizer.getSplits.length - 1} buckets")
  bucketedDF.show()

  close()
}
