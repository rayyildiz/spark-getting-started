package me.rayyildiz

import org.apache.spark.sql.SparkSession

/**
  * Created by rayyildiz on 6/12/2017.
  */
trait SparkSupport {
  val spark = SparkSession.builder.master("local").appName(getClass.getName).config("spark.sql.warehouse.dir", "file:///C:/target/").getOrCreate()

}
