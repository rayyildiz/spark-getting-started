package me.rayyildiz

import org.apache.spark.ml.feature.DCT
import org.apache.spark.ml.linalg.Vectors


/**
  * Created by rayyildiz on 6/12/2017.
  */
object DiscreteCosineTransformExample extends App with SparkSupport {

  val data = Seq(
    Vectors.dense(0.0, 1.0, -2.0, 3.0),
    Vectors.dense(-1.0, 2.0, 4.0, -7.0),
    Vectors.dense(14.0, -2.0, -5.0, 1.0))

  val df = spark.createDataFrame(data.map(Tuple1.apply)).toDF("features")

  val dct = new DCT()
    .setInputCol("features")
    .setOutputCol("featuresDCT")
    .setInverse(false)

  val dctDf = dct.transform(df)
  dctDf.select("featuresDCT").show(false)
}
