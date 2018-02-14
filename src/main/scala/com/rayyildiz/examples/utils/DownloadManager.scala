package com.rayyildiz.examples.utils

import java.io.{File, FileInputStream, FileOutputStream}
import java.net.URL
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
        unzip(target, target.getParentFile)
        log.info(s"unzip finished. ${target.getAbsolutePath}")
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
