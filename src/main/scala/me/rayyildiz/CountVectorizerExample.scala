package me.rayyildiz

import org.apache.spark.ml.feature.{CountVectorizer, CountVectorizerModel}

/**
  * Created by rayyildiz on 6/12/2017.
  */
object CountVectorizerExample extends App with SparkSupport {

  val df = spark.createDataFrame(Seq(
    (0, Array("a", "b", "c")),
    (1, Array("a", "b", "b", "c", "a"))
  )).toDF("id", "words")

  // fit a CountVectorizerModel from the corpus
  val cvModel: CountVectorizerModel = new CountVectorizer()
    .setInputCol("words")
    .setOutputCol("features")
    .setVocabSize(3)
    .setMinDF(2)
    .fit(df)

  // alternatively, define CountVectorizerModel with a-priori vocabulary
  val cvm = new CountVectorizerModel(Array("a", "b", "c"))
    .setInputCol("words")
    .setOutputCol("features")

  cvModel.transform(df).show(false)

}
