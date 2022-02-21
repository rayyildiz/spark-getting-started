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
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.ml.linalg.Vectors

/** Created by rayyildiz on 6/12/2017.
  */
object VectorAssemblerExample extends App with SparkSupport {

  val dataset = spark
    .createDataFrame(Seq((0, 18, 1.0, Vectors.dense(0.0, 10.0, 0.5), 1.0)))
    .toDF("id", "hour", "mobile", "user_features", "clicked")

  val assembler = new VectorAssembler()
    .setInputCols(Array("hour", "mobile", "user_features"))
    .setOutputCol("features")

  val output = assembler.transform(dataset)
  log.info("Assembled columns 'hour', 'mobile', 'userFeatures' to vector column 'features'")
  val resultDF = output.select("features", "clicked")

  resultDF.show(false)

  close()
}
