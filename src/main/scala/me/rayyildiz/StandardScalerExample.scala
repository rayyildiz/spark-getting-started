package me.rayyildiz

import org.apache.spark.ml.feature.StandardScaler


/**
  * Created by rayyildiz on 6/12/2017.
  */
object StandardScalerExample extends App with SparkSupport {

  val dataFrame = spark.read.format("libsvm").load("data/mllib/sample_libsvm_data.txt")

  val scaler = new StandardScaler()
    .setInputCol("features")
    .setOutputCol("scaledFeatures")
    .setWithStd(true)
    .setWithMean(false)

  // Compute summary statistics by fitting the StandardScaler.
  val scalerModel = scaler.fit(dataFrame)

  // Normalize each feature to have unit standard deviation.
  val scaledData = scalerModel.transform(dataFrame)
  scaledData.show()

}
