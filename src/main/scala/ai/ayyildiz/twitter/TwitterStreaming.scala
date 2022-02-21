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
package ai.ayyildiz.twitter

import ai.ayyildiz.SparkSupport
import ai.ayyildiz.utils.DownloadManager
import org.apache.spark.streaming.twitter.TwitterUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}

object TwitterStreaming extends App with SparkSupport {

  DownloadManager.download("http://www2.imm.dtu.dk/pubdb/views/edoc_download.php/6010/zip/imm6010.zip", "data/imm6010.zip")
  val consumerKey       = System.getenv("TWITTER_CONSUMER_KEY")
  val consumerSecret    = System.getenv("TWITTER_CONSUMER_SECRET")
  val accessToken       = System.getenv("TWITTER_ACCESS_TOKEN")
  val accessTokenSecret = System.getenv("TWITTER_ACCESS_SECRET")

  if (
    consumerKey == null || consumerSecret == null || accessTokenSecret == null || accessToken == null
    || consumerKey.isEmpty || consumerSecret.isEmpty || accessToken.isEmpty || accessTokenSecret.isEmpty
  ) {
    log.error(s"you have to define 'TWITTER_CONSUMER_KEY' , 'TWITTER_CONSUMER_SECRET' , 'TWITTER_ACCESS_TOKEN', 'TWITTER_ACCESS_SECRET'")
    System.exit(0)
  }

  System.setProperty("twitter4j.oauth.consumerKey", consumerKey)
  System.setProperty("twitter4j.oauth.consumerSecret", consumerSecret)
  System.setProperty("twitter4j.oauth.accessToken", accessToken)
  System.setProperty("twitter4j.oauth.accessTokenSecret", accessTokenSecret)

  val ssc     = new StreamingContext(spark.sparkContext, Seconds(1))
  val filters = Array("PSG", "Real Madrid", "RealMadrid")
  val stream  = TwitterUtils.createStream(ssc, None, filters)

  val wordSentimentFilePath = "./data/AFINN/AFINN-111.txt"
  val wordSentiments = spark.sparkContext
    .textFile(wordSentimentFilePath)
    .map { line =>
      val Array(word, happinessValue) = line.split("\t")
      (word, happinessValue.toInt)
    }
    .cache()

  //  val hashTags = stream.flatMap(status => status.getText.split(" ").filter(_.startsWith("#")))
  val words =
    stream.flatMap(status => status.getText.split(" ").filter(!_.isEmpty))

  val happiest60 = words
    .map(word => (word.tail, 1))
    .reduceByKeyAndWindow(_ + _, Seconds(60))
    .transform { topicCount =>
      wordSentiments.join(topicCount)
    }
    .map { case (topic, tuple) => (topic, tuple._1 * tuple._2) }
    .map { case (topic, happinessValue) => (happinessValue, topic) }
    .transform(_.sortByKey(false))

  val happiest10 = words
    .map(word => (word.tail, 1))
    .reduceByKeyAndWindow(_ + _, Seconds(10))
    .transform { topicCount =>
      wordSentiments.join(topicCount)
    }
    .map { case (topic, tuple) => (topic, tuple._1 * tuple._2) }
    .map { case (topic, happinessValue) => (happinessValue, topic) }
    .transform(_.sortByKey(false))

  happiest60.foreachRDD(rdd => {
    val topList = rdd.take(10)
    println("\nHappiest topics in last 60 seconds (%s total):".format(rdd.count()))
    topList.foreach { case (happiness, tag) =>
      println("%s (%s happiness)".format(tag, happiness))
    }
  })

  happiest10.foreachRDD(rdd => {
    val topList = rdd.take(10)
    println("\nHappiest topics in last 10 seconds (%s total):".format(rdd.count()))
    topList.foreach { case (happiness, tag) =>
      println("%s (%s happiness)".format(tag, happiness))
    }
  })

  ssc.start()
  ssc.checkpoint("./data/checkpoint")
  ssc.awaitTermination()

  // Wait for enter , so you can see the spark UI
  // waitForEnter

  close()
}
