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
package dev.rayyildiz.examples.ml

import dev.rayyildiz.SparkSupport
import org.apache.spark.ml.feature.MaxAbsScaler
import org.apache.spark.ml.linalg.Vectors

/**
  * Created by rayyildiz on 6/12/2017.
  */
object MaxAbsScalerExample extends App with SparkSupport {

  val dataFrame = spark
    .createDataFrame(
      Seq(
        (0, Vectors.dense(1.0, 0.1, -8.0)),
        (1, Vectors.dense(2.0, 1.0, -4.0)),
        (2, Vectors.dense(4.0, 10.0, 8.0))
      )
    )
    .toDF("id", "features")

  val scaler = new MaxAbsScaler()
    .setInputCol("features")
    .setOutputCol("scaledFeatures")

  // Compute summary statistics and generate MaxAbsScalerModel
  val scalerModel = scaler.fit(dataFrame)

  // rescale each feature to range [-1, 1]
  val scaledData = scalerModel.transform(dataFrame)
  val resultDF   = scaledData.select("features", "scaledFeatures")
  resultDF.show()

  close()
}
