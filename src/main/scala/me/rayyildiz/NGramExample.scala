package me.rayyildiz

import org.apache.spark.ml.feature.NGram

/**
  * Created by rayyildiz on 6/12/2017.
  */
object NGramExample extends App with SparkSupport{

  val wordDataFrame = spark.createDataFrame(Seq(
    (0, Array("Hi", "I", "heard", "about", "Spark")),
    (1, Array("I", "wish", "Java", "could", "use", "case", "classes")),
    (2, Array("Logistic", "regression", "models", "are", "neat"))
  )).toDF("id", "words")

  val ngram = new NGram().setN(2).setInputCol("words").setOutputCol("ngrams")

  val ngramDataFrame = ngram.transform(wordDataFrame)
  ngramDataFrame.select("ngrams").show(false)

}
