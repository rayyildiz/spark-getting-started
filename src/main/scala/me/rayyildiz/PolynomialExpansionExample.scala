package me.rayyildiz

import org.apache.spark.ml.feature.PolynomialExpansion
import org.apache.spark.ml.linalg.Vectors

/**
  * Created by rayyildiz on 6/12/2017.
  */
object PolynomialExpansionExample extends App with SparkSupport {

  val data = Array(
    Vectors.dense(2.0, 1.0),
    Vectors.dense(0.0, 0.0),
    Vectors.dense(3.0, -1.0)
  )
  val df = spark.createDataFrame(data.map(Tuple1.apply)).toDF("features")

  val polyExpansion = new PolynomialExpansion()
    .setInputCol("features")
    .setOutputCol("polyFeatures")
    .setDegree(3)

  val polyDF = polyExpansion.transform(df)
  polyDF.show(false)

}
