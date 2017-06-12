package me.rayyildiz

import org.apache.spark.ml.feature.{OneHotEncoder, StringIndexer}

/**
  * Created by rayyildiz on 6/12/2017.
  */
object OneHotEncoderExample extends App with SparkSupport {

  val df = spark
    .createDataFrame(
      Seq(
        (0, "a"),
        (1, "b"),
        (2, "c"),
        (3, "a"),
        (4, "a"),
        (5, "c")
      ))
    .toDF("id", "category")

  val indexer = new StringIndexer()
    .setInputCol("category")
    .setOutputCol("categoryIndex")
    .fit(df)
  val indexed = indexer.transform(df)

  val encoder = new OneHotEncoder()
    .setInputCol("categoryIndex")
    .setOutputCol("categoryVec")

  val encoded = encoder.transform(indexed)
  encoded.show()

}
