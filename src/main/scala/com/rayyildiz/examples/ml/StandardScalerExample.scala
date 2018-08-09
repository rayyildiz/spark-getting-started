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
package com.rayyildiz.examples.ml

import com.rayyildiz.SparkSupport
import com.rayyildiz.utils.DownloadManager
import org.apache.spark.ml.feature.StandardScaler

/**
 * Created by rayyildiz on 6/12/2017.
 */
object StandardScalerExample extends App with SparkSupport {

  DownloadManager.download(
    "https://raw.githubusercontent.com/apache/spark/master/data/mllib/sample_libsvm_data.txt",
    "./data/sample_libsvm_data.txt"
  )

  val dataFrame = spark.read.format("libsvm").load("./data/sample_libsvm_data.txt")

  val scaler = new StandardScaler()
    .setInputCol("features")
    .setOutputCol("scaledFeatures")
    .setWithStd(true)
    .setWithMean(false)

  // Compute summary statistics by fitting the StandardScaler.
  val scalerModel = scaler.fit(dataFrame)

  // Normalize each feature to have unit standard deviation.
  val scaledDF = scalerModel.transform(dataFrame)
  scaledDF.show()

  close()
}
