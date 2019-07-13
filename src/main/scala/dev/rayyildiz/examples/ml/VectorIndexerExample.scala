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
import dev.rayyildiz.utils.DownloadManager
import dev.rayyildiz.SparkSupport
import dev.rayyildiz.utils.DownloadManager
import org.apache.spark.ml.feature.VectorIndexer

/**
 * Created by rayyildiz on 6/12/2017.
 */
object VectorIndexerExample extends App with SparkSupport {
  DownloadManager.download(
    "https://raw.githubusercontent.com/apache/spark/master/data/mllib/sample_libsvm_data.txt",
    "./data/sample_libsvm_data.txt"
  )

  val data = spark.read.format("libsvm").load("./data/sample_libsvm_data.txt")

  val indexer = new VectorIndexer()
    .setInputCol("features")
    .setOutputCol("indexed")
    .setMaxCategories(10)

  val indexerModel = indexer.fit(data)

  val categoricalFeatures: Set[Int] = indexerModel.categoryMaps.keys.toSet
  log.info(
    s"Chose ${categoricalFeatures.size} categorical features: " +
      categoricalFeatures.mkString(", ")
  )

  // Create new column "indexed" with categorical values transformed to indices
  val indexedDF = indexerModel.transform(data)
  indexedDF.show()

  close()
}
