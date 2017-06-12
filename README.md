Getting Started 
===

[![Build Status](https://travis-ci.org/rayyildiz/spark-getting-started.svg?branch=master)](https://travis-ci.org/rayyildiz/spark-getting-started)

[Word Count](src/main/scala/com/rayyildiz/examples/WordCount.scala) 
---

Download [Manually Annotated Sub-Corpus (MASC) ](http://www.anc.org/data/masc/) 500k file and count by word. 

```scala
  val textFile = spark.read.textFile("data/test.txt")

  val num = textFile.filter(line => line.contains("and")).count()
```


[Data Table](src/main/scala/com/rayyildiz/examples/DataTable.scala) 
---

Download [US Investor Flow of Funds into Investment Classes](http://datahub.io/core/investor-flow-of-funds-us), create a table and list top 10 rows order by total.

```scala
spark.read.json("data/data/monthly_json.json").toDF("Date", "Total").createTempView("monthly")

spark.sql("SELECT * FROM monthly WHERE total > 10000 ORDER BY total desc").show(10)
```


|      Date|TotalEquity|DomesticEquity|WorldEquity|Hybrid|TotalBond|TaxableBond|MunicipalBond|Total|
|---------:|----------:|-------------:|----------:|-----:|--------:|----------:|------------:|----:|
|2007-01-31|       5723|          5321|       2834| 12453|    47972|      15287|        27364|21641|
|2013-01-31|      18076|         10158|       7181| 25506|    79929|      32687|        37084|19009|
|2007-02-28|       8411|          5164|       3137| 11926|    45533|      15064|        25306|16895|
|2014-01-31|       6966|          2760|        433|  2051|    29005|       2484|        23761|16795|
|2007-04-30|       -163|          4384|       1355| 12346|    34148|      13701|        16063|16225|
|2007-10-31|      -6602|          3636|        710| 10896|    24580|      11605|         9339|15941|
|2013-11-30|       6407|          1868|      -4459| -9689|    10046|     -14147|        22326|15918|
|2013-02-28|      -1289|          9983|       2506| 17363|    44318|      19869|        14465|15754|
|2007-07-31|      -5089|          2587|        198|  4431|    17118|       4630|         9901|14990|
|2010-12-31|     -12537|          3889|     -13307| -8008|   -16144|     -21314|         1281|13818|


[Analysing Tweets](src/main/scala/com/rayyildiz/examples/AnalyzingTweets.scala)
---

Define ```TWITTER_CONSUMER_KEY``` , ```TWITTER_CONSUMER_SECRET``` , ```TWITTER_ACCESS_TOKEN``` and ```TWITTER_ACCESS_SECRET``` as  system environment  before running. 



[Twitter Streaming](src/main/scala/com/rayyildiz/examples/TwitterStreaming.scala)
---
 
Define ```TWITTER_CONSUMER_KEY``` , ```TWITTER_CONSUMER_SECRET```, ```TWITTER_ACCESS_TOKEN```, ```TWITTER_ACCESS_SECRET``` as enviropment variable before running program.
You need to create a [Twitter App](https://apps.twitter.com/)  and [generate token](https://developer.twitter.com/en/docs/basics/authentication/guides/access-tokens)
