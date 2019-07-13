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
import org.apache.spark.ml.feature.{RegexTokenizer, Tokenizer}
import org.apache.spark.sql.functions.{col, udf}

/**
 * Created by rayyildiz on 6/12/2017.
 */
object TokenizerExample extends App with SparkSupport {

  val sentenceDataFrame = spark
    .createDataFrame(
      Seq(
        (0, "Hi I heard about Spark"),
        (1, "I wish Java could use data/case classes"),
        (2, "Logistic,regression,models,are,neat")
      )
    )
    .toDF("id", "sentence")

  val tokenizer = new Tokenizer().setInputCol("sentence").setOutputCol("words")
  val regexTokenizer = new RegexTokenizer()
    .setInputCol("sentence")
    .setOutputCol("words")
    .setPattern("\\W")

  val countTokens = udf { words: Seq[String] =>
    words.length
  }

  val tokenized = tokenizer.transform(sentenceDataFrame)
  tokenized
    .select("sentence", "words")
    .withColumn("tokens", countTokens(col("words")))
    .show(false)

  val regexTokenized = regexTokenizer.transform(sentenceDataFrame)
  val resultDF = regexTokenized
    .select("sentence", "words")
    .withColumn("tokens", countTokens(col("words")))

  resultDF.show(false)

  close()
}
