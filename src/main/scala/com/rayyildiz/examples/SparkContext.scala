package com.rayyildiz.examples

import org.apache.log4j.LogManager
import org.apache.spark.sql.SparkSession

import scala.io.StdIn

trait SparkContext {
  lazy val log = LogManager.getLogger(getClass)
  private val EnvHadoopHomeDir = "hadoop.home.dir"

  if (System.getProperty(EnvHadoopHomeDir) == null || System.getProperty(EnvHadoopHomeDir).isEmpty) {
    log.error(s"not defined '$EnvHadoopHomeDir' system environment")
  }

  lazy val spark = SparkSession.builder.appName("GettingStarted").master("local[*]").getOrCreate()

  /**
    * Wait for enter.
    */
  def waitForEnter = {
    Console.println("To finish press [ENTER] key")
    StdIn.readLine()
  }

}
