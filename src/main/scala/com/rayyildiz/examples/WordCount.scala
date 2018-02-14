package com.rayyildiz.examples

import com.rayyildiz.examples.utils.DownloadManager
import org.apache.spark.sql.SparkSession

import scala.io.StdIn

object WordCount extends App {

  if (System.getProperty("hadoop.home.dir") == null || System.getProperty("hadoop.home.dir").isEmpty) {

    System.setProperty("hadoop.home.dir", "D:\\Apps\\BigData\\hadoop\\hadoop-2.9.0\\")
  }

  DownloadManager.download("http://www.anc.org/MASC/download/masc_500k_texts.zip", "data/masc_500k_texts.zip")

  val spark = SparkSession.builder.appName("WordCount").master("local[*]").getOrCreate()


  val textFile = spark.read.textFile("data/masc_500k_texts/written/*/*.txt", "data/masc_500k_texts/spoken/*/*.txt").cache()

  val numAnd = textFile.filter(line => line.contains("and")).count()
  val numOr = textFile.filter(line => line.contains("or")).count()

  println(s"Lines with 'and': $numAnd, Lines with 'or': $numOr")


  print("To finish press [ENTER] button ")
  StdIn.readLine()

  spark.stop()
}
