package com.rayyildiz.examples

import com.rayyildiz.examples.utils.DownloadManager

object DataTable extends App with SparkContext {

  // Download the investor files
  DownloadManager.download("https://pkgstore.datahub.io/core/investor-flow-of-funds-us/investor-flow-of-funds-us_zip/data/9d8946be40bfdec10ec3d94e0543c75d/investor-flow-of-funds-us_zip.zip", "./data/investor-flow-of-funds-us_zip.zip")

  // Create a data frame
  val monthly = spark.read.json("./data/data/monthly_json.json").toDF("Date", "TotalEquity", "DomesticEquity", "WorldEquity", "Hybrid", "TotalBond", "TaxableBond", "MunicipalBond", "Total")

  // Create a view
  monthly.createTempView("monthly")

  monthly.show(10)

  // Let' look at the data types.
  monthly.dtypes.foreach(println)

  // top 10  records ordered by total column.
  val query = spark.sql("SELECT * FROM monthly WHERE total > 16000 ORDER BY total desc")

  query.show(10)

  // Wait for enter , so you can see the spark UI
  // waitForEnter

  spark.stop()

}
