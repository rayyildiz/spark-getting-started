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
package dev.rayyildiz.intro

import dev.rayyildiz.SparkSupport
import dev.rayyildiz.utils.DownloadManager
import dev.rayyildiz.SparkSupport
import dev.rayyildiz.utils.DownloadManager

object DataTable extends App with SparkSupport {

  // Download the investor files
  DownloadManager.download(
    "https://pkgstore.datahub.io/core/investor-flow-of-funds-us/investor-flow-of-funds-us_zip/data/9d8946be40bfdec10ec3d94e0543c75d/investor-flow-of-funds-us_zip.zip",
    "./data/investor-flow-of-funds-us_zip.zip"
  )

  // Create a data frame
  val monthly = spark.read
    .json("./data/data/monthly_json.json")
    .toDF("Date", "TotalEquity", "DomesticEquity", "WorldEquity", "Hybrid", "TotalBond", "TaxableBond", "MunicipalBond", "Total")

  // Create a view
  monthly.createTempView("monthly")

  monthly.show()

  // Let' look at the data types.
  monthly.dtypes.foreach(println)

  // top 10  records ordered by total column.
  val query = spark.sql("SELECT * FROM monthly WHERE total > 16000 ORDER BY total desc")

  query.show()

  // Wait for enter , so you can see the spark UI
  // waitForEnter

  close()
}
