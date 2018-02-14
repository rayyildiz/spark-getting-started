package com.rayyildiz.examples

import com.rayyildiz.examples.utils.DownloadManager
import org.apache.spark.streaming._
import org.apache.spark.streaming.twitter._


object TwitterStreaming extends App with SparkContext {

  DownloadManager.download("http://www2.imm.dtu.dk/pubdb/views/edoc_download.php/6010/zip/imm6010.zip","data/imm6010.zip")
  val consumerKey = System.getenv("TWITTER_CONSUMER_KEY")
  val consumerSecret = System.getenv("TWITTER_CONSUMER_SECRET")
  val accessToken = System.getenv("TWITTER_ACCESS_TOKEN")
  val accessTokenSecret = System.getenv("TWITTER_ACCESS_SECRET")

  if (consumerKey == null || consumerSecret == null || accessTokenSecret == null || accessToken == null
    || consumerKey.isEmpty || consumerSecret.isEmpty || accessToken.isEmpty || accessTokenSecret.isEmpty) {
    log.error(s"you have to define 'TWITTER_CONSUMER_KEY' , 'TWITTER_CONSUMER_SECRET' , 'TWITTER_ACCESS_TOKEN', 'TWITTER_ACCESS_SECRET'")
    System.exit(0)
  }

  System.setProperty("twitter4j.oauth.consumerKey", consumerKey)
  System.setProperty("twitter4j.oauth.consumerSecret", consumerSecret)
  System.setProperty("twitter4j.oauth.accessToken", accessToken)
  System.setProperty("twitter4j.oauth.accessTokenSecret", accessTokenSecret)


  val ssc = new StreamingContext(spark.sparkContext, Seconds(1))
  val filters = Array("PSG", "Real Madrid","RealMadrid")
  val stream = TwitterUtils.createStream(ssc, None, filters)

  val wordSentimentFilePath = "./data/AFINN/AFINN-111.txt"
  val wordSentiments = spark.sparkContext.textFile(wordSentimentFilePath).map { line =>
    val Array(word, happinessValue) = line.split("\t")
    (word, happinessValue.toInt)
  }.cache()


  //  val hashTags = stream.flatMap(status => status.getText.split(" ").filter(_.startsWith("#")))
  val words = stream.flatMap(status => status.getText.split(" ").filter(!_.isEmpty))


  val happiest60 = words.map(word => (word.tail, 1))
    .reduceByKeyAndWindow(_ + _, Seconds(60))
    .transform { topicCount => wordSentiments.join(topicCount) }
    .map { case (topic, tuple) => (topic, tuple._1 * tuple._2) }
    .map { case (topic, happinessValue) => (happinessValue, topic) }
    .transform(_.sortByKey(false))

  val happiest10 = words.map(word => (word.tail, 1))
    .reduceByKeyAndWindow(_ + _, Seconds(10))
    .transform { topicCount => wordSentiments.join(topicCount) }
    .map { case (topic, tuple) => (topic, tuple._1 * tuple._2) }
    .map { case (topic, happinessValue) => (happinessValue, topic) }
    .transform(_.sortByKey(false))

  happiest60.foreachRDD(rdd => {
    val topList = rdd.take(10)
    println("\nHappiest topics in last 60 seconds (%s total):".format(rdd.count()))
    topList.foreach { case (happiness, tag) => println("%s (%s happiness)".format(tag, happiness)) }
  })

  happiest10.foreachRDD(rdd => {
    val topList = rdd.take(10)
    println("\nHappiest topics in last 10 seconds (%s total):".format(rdd.count()))
    topList.foreach { case (happiness, tag) => println("%s (%s happiness)".format(tag, happiness)) }
  })

  ssc.start()
  ssc.checkpoint("./data/checkpoint")
  ssc.awaitTermination()

  // Wait for enter , so you can see the spark UI
  // waitForEnter
  spark.close()
}
