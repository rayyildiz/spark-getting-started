/*
 * Copyright (c) 2018 Ramazan AYYILDIZ
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
package com.rayyildiz.examples
import com.rayyildiz.SparkSupport
import org.apache.spark.ml.feature.ChiSqSelector
import org.apache.spark.ml.linalg.Vectors

/**
 * Created by rayyildiz on 6/12/2017.
 */
object ChiSqSelectorExample extends App with SparkSupport {

  val data = Seq(
    (7, Vectors.dense(0.0, 0.0, 18.0, 1.0), 1.0),
    (8, Vectors.dense(0.0, 1.0, 12.0, 0.0), 0.0),
    (9, Vectors.dense(1.0, 0.0, 15.0, 0.1), 0.0)
  )

  import spark.implicits._

  val df = spark.createDataset(data).toDF("id", "features", "clicked")

  val selector = new ChiSqSelector()
    .setNumTopFeatures(1)
    .setFeaturesCol("features")
    .setLabelCol("clicked")
    .setOutputCol("selectedFeatures")

  val result = selector.fit(df).transform(df)

  println(s"ChiSqSelector output with top ${selector.getNumTopFeatures} features selected")
  result.show()

}
