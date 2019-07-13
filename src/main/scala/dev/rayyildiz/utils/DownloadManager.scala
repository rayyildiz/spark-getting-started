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
package dev.rayyildiz.utils

import java.io.{File, FileInputStream, FileOutputStream}
import java.net.URL
import java.nio.file.InvalidPathException
import java.util.zip.ZipInputStream

import org.apache.log4j.LogManager

import scala.sys.process._

object DownloadManager {

  private val log = LogManager.getLogger(DownloadManager.getClass)

  /**
   * Download file from internet.
   *
   * @param url        Full path.
   * @param targetFile Target local path.
   */
  def download(url: String, targetFile: String): Unit = {
    val target = new File(targetFile)
    if (target.exists()) {
      log.info(s"file ${target.getName} exist.")
    } else {
      log.info(s"downloading $url as $targetFile")
      new URL(url) #> target !!

      log.info(s"download finished.")
      if (target.getName.endsWith("zip")) {
        try {
          unzip(target, target.getParentFile)
          log.info(s"unzip finished. ${target.getAbsolutePath}")
        } catch {
          case e: InvalidPathException => log.error(s"could not unzip for $targetFile", e)
        }
      }
    }
  }

  /**
   * Unzip zip file to destination folder.
   *
   * @param file            ZipFile
   * @param destinationFile destination path.
   */
  def unzip(file: File, destinationFile: File): Unit = {
    val zipFile = new FileInputStream(file)
    val destination = destinationFile.toPath

    val zis = new ZipInputStream(zipFile)

    Stream.continually(zis.getNextEntry).takeWhile(_ != null).foreach { file =>
      if (!file.isDirectory) {
        val outPath = destination.resolve(file.getName)
        val outPathParent = outPath.getParent
        if (!outPathParent.toFile.exists()) {
          outPathParent.toFile.mkdirs()
        }

        val outFile = outPath.toFile
        val out = new FileOutputStream(outFile)
        val buffer = new Array[Byte](4096)
        Stream.continually(zis.read(buffer)).takeWhile(_ != -1).foreach(out.write(buffer, 0, _))
      }
    }
  }

}
