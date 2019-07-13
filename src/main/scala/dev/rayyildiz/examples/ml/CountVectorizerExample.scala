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
import org.apache.spark.ml.feature.{CountVectorizer, CountVectorizerModel}

/**
 * Created by rayyildiz on 6/12/2017.
 */
object CountVectorizerExample extends App with SparkSupport {

  val df = spark
    .createDataFrame(
      Seq(
        (0, Array("a", "b", "c")),
        (1, Array("a", "b", "b", "c", "a"))
      )
    )
    .toDF("id", "words")

  // fit a CountVectorizerModel from the corpus
  val cvModel: CountVectorizerModel = new CountVectorizer()
    .setInputCol("words")
    .setOutputCol("features")
    .setVocabSize(3)
    .setMinDF(2)
    .fit(df)

  // alternatively, define CountVectorizerModel with a-priori vocabulary
  val cvm = new CountVectorizerModel(Array("a", "b", "c"))
    .setInputCol("words")
    .setOutputCol("features")

  val resultDF = cvModel.transform(df)

  resultDF.show(false)

  close()
}
