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
import org.apache.spark.ml.feature.Word2Vec
import org.apache.spark.sql.Row
import org.apache.spark.ml.linalg.Vector

/**
 * Created by rayyildiz on 6/12/2017.
 */
object Word2VecExample extends App with SparkSupport {

  // Input data: Each row is a bag of words from a sentence or document.
  val documentDF = spark
    .createDataFrame(
      Seq(
        "Hi I heard about Spark".split(" "),
        "I wish Java could use case classes".split(" "),
        "Logistic regression models are neat".split(" ")
      ).map(Tuple1.apply)
    )
    .toDF("text")

  // Learn a mapping from words to Vectors.
  val word2Vec = new Word2Vec()
    .setInputCol("text")
    .setOutputCol("result")
    .setVectorSize(300)
    .setMinCount(0)

  val model = word2Vec.fit(documentDF)

  val result = model.transform(documentDF)
  result.collect().foreach {
    case Row(text: Seq[_], features: Vector) =>
      println(s"Text: [${text.mkString(", ")}] => \nVector: $features\n")
  }

}
