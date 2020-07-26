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
package dev.rayyildiz.intro

import dev.rayyildiz.SparkSupport
import dev.rayyildiz.utils.DownloadManager

object WordCount extends App with SparkSupport {

  // Let's download MASC data from American National Corpus
  DownloadManager.download(
    "http://www.anc.org/MASC/download/masc_500k_texts.zip",
    "./data/masc_500k_texts.zip"
  )

  // Read all txt files and cache
  val textFile = spark.read
    .textFile(
      "./data/masc_500k_texts/written/blog/*.txt",
      "./data/masc_500k_texts/written/email/*.txt",
      "./data/masc_500k_texts/written/essays/*.txt",
      "./data/masc_500k_texts/written/fiction/*.txt",
      "./data/masc_500k_texts/written/jokes/*.txt",
      "./data/masc_500k_texts/written/journal/*.txt",
      "./data/masc_500k_texts/written/spam/*.txt",
      "./data/masc_500k_texts/written/technical/*.txt",
      "./data/masc_500k_texts/written/twitter/*.txt",
      "./data/masc_500k_texts/spoken/*/*.txt"
    )
    .cache()

  // Let's count the 'and', 'or' and ' ' (SPACE)
  val numAnd = textFile.filter(line => line.contains("and")).count()
  val numOr = textFile.filter(line => line.contains("or")).count()
  val numSpace = textFile.filter(line => line.contains(" ")).count()

  log.info(s"'and': $numAnd, 'or': $numOr,  [SPACE]: $numSpace")

  // Wait for enter , so you can see the spark UI
  // waitForEnter

  close()
}
