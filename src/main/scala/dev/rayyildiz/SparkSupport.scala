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
package dev.rayyildiz

import org.apache.log4j.{LogManager, Logger}
import org.apache.spark.sql.SparkSession

import scala.io.StdIn

trait SparkSupport {
  lazy val log: Logger         = LogManager.getLogger(getClass)
  private val EnvHadoopHomeDir = "hadoop.home.dir"

  if (
    System.getProperty(EnvHadoopHomeDir) == null || System
      .getProperty(EnvHadoopHomeDir)
      .isEmpty
  ) {
    log.info(s"not defined '$EnvHadoopHomeDir' system environment")
  }

  /**
    * Create or get spark session.
    */
  lazy val spark: SparkSession = SparkSession.builder
    .appName("GettingStarted")
    .master("local[*]")
    .getOrCreate()

  /**
    * Wait for enter.
    */
  def waitForEnter: String = {
    Console.println("To finish press [ENTER] key")
    StdIn.readLine()
  }

  def close(): Unit = {
    log.info("Stopping spark...")
    spark.close()
    log.info("Spark server stopped")
  }
}
