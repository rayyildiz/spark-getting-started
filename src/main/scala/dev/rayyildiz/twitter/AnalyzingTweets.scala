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
package dev.rayyildiz.twitter

import java.util.Properties

import dev.rayyildiz.SparkSupport
import org.apache.spark.streaming.twitter.TwitterUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}

object AnalyzingTweets extends App with SparkSupport {
  val consumerKey = System.getenv("TWITTER_CONSUMER_KEY")
  val consumerSecret = System.getenv("TWITTER_CONSUMER_SECRET")
  val accessToken = System.getenv("TWITTER_ACCESS_TOKEN")
  val accessTokenSecret = System.getenv("TWITTER_ACCESS_SECRET")

  if (consumerKey == null || consumerSecret == null || accessTokenSecret == null || accessToken == null
      || consumerKey.isEmpty || consumerSecret.isEmpty || accessToken.isEmpty || accessTokenSecret.isEmpty) {
    log.error(
      s"you have to define 'TWITTER_CONSUMER_KEY' , 'TWITTER_CONSUMER_SECRET' , 'TWITTER_ACCESS_TOKEN', 'TWITTER_ACCESS_SECRET'"
    )
    System.exit(0)
  }

  import spark.implicits._

  val props = new Properties
  props.setProperty(
    "annotators",
    "tokenize, ssplit, pos, lemma, parse, sentiment"
  )

  val ssc = new StreamingContext(spark.sparkContext, Seconds(10))

  System.setProperty("twitter4j.oauth.consumerKey", consumerKey)
  System.setProperty("twitter4j.oauth.consumerSecret", consumerSecret)
  System.setProperty("twitter4j.oauth.accessToken", accessToken)
  System.setProperty("twitter4j.oauth.accessTokenSecret", accessTokenSecret)

  val filters = Array("Google", "Apple")
  val stream = TwitterUtils.createStream(ssc, None, filters)

  val tweetsWithBasicInfo =
    stream
      .filter(!_.isRetweeted)
      .map(
        t =>
          (
            t.getId,
            t.getText,
            t.getRetweetCount,
            t.getUser.getScreenName,
            t.getLang
        ))

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

  close()

}
