package com.rayyildiz.examples

import com.rayyildiz.examples.utils.DownloadManager

object WordCount extends App with SparkContext {

  // Let's download MASC data from American National Corpus
  DownloadManager.download("http://www.anc.org/MASC/download/masc_500k_texts.zip", "./data/masc_500k_texts.zip")

  // Read all txt files and cache
  val textFile = spark.read.textFile("./data/masc_500k_texts/spoken/*/*.txt", "./data/masc_500k_texts/spoken/*/*.txt").cache()

  // Let's count the 'and', 'or' and ' ' (SPACE)
  val numAnd = textFile.filter(line => line.contains("and")).count()
  val numOr = textFile.filter(line => line.contains("or")).count()
  val numSpace = textFile.filter(line => line.contains(" ")).count()

  log.info(s"'and': $numAnd, 'or': $numOr,  [SPACE]: $numSpace")


  // Wait for enter , so you can see the spark UI
  // waitForEnter

  spark.stop()
}
