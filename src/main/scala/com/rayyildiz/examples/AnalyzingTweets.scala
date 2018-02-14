package com.rayyildiz.examples

import java.util.Properties

import org.apache.spark.streaming.twitter.TwitterUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}

object AnalyzingTweets extends App with SparkContext {
  val consumerKey = System.getenv("TWITTER_CONSUMER_KEY")
  val consumerSecret = System.getenv("TWITTER_CONSUMER_SECRET")
  val accessToken = System.getenv("TWITTER_ACCESS_TOKEN")
  val accessTokenSecret = System.getenv("TWITTER_ACCESS_SECRET")

  if (consumerKey == null || consumerSecret == null || accessTokenSecret == null || accessToken == null
    || consumerKey.isEmpty || consumerSecret.isEmpty || accessToken.isEmpty || accessTokenSecret.isEmpty) {
    log.error(s"you have to define 'TWITTER_CONSUMER_KEY' , 'TWITTER_CONSUMER_SECRET' , 'TWITTER_ACCESS_TOKEN', 'TWITTER_ACCESS_SECRET'")
    System.exit(0)
  }


  val props = new Properties
  props.setProperty("annotators", "tokenize, ssplit, pos, lemma, parse, sentiment")

  val ssc = new StreamingContext(spark.sparkContext, Seconds(1))

  import spark.sqlContext.implicits._

  System.setProperty("twitter4j.oauth.consumerKey", consumerKey)
  System.setProperty("twitter4j.oauth.consumerSecret", consumerSecret)
  System.setProperty("twitter4j.oauth.accessToken", accessToken)
  System.setProperty("twitter4j.oauth.accessTokenSecret", accessTokenSecret)

  val filters = Array("Google", "Apple")
  val stream = TwitterUtils.createStream(ssc, None, filters)

  val tweetsWithBasicInfo = stream.filter(!_.isRetweeted).map(t => (t.getId, t.getText, t.getRetweetCount, t.getUser.getScreenName, t.getLang))


  tweetsWithBasicInfo.foreachRDD(rdd => {
    val df = rdd.toDF("id", "text", "retweet_count", "user", "language")

    df.persist()
    //    df.createGlobalTempView("sentences")

    if (df != null) {
      df.show(5)
    }
  })


  ssc.start()
  ssc.awaitTermination()

  // Wait for enter , so you can see the spark UI
  // waitForEnter

  spark.close()


}
