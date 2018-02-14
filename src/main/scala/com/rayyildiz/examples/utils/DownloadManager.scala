package com.rayyildiz.examples.utils

import java.io.{File, FileInputStream, FileOutputStream, InputStream}
import java.net.URL
import java.nio.file.Path
import java.util.zip.{GZIPInputStream, ZipInputStream}

import scala.sys.process._

object DownloadManager {

  /**
    * Download file from internet.
    *
    * @param url        Full path.
    * @param targetFile Target local path.
    */
  def download(url: String, targetFile: String): Unit = {
    val target = new File(targetFile)
    if (target.exists()) {
      println(s"file ${target.getName} exist.")
    } else {
      println(s"downloading $url as $targetFile")
      new URL(url) #> target !!

      println(s"download finished. Unzip file.")
      unzip(target, target.getParentFile)
      println(s"unzip finished. ${target.getAbsolutePath}")
    }
  }


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
