package parser.util

import java.io.{File, FileNotFoundException}

import scala.collection.Iterator
import scala.io.Source
import parser.config.Constants.LINE_BREAK

object FileUtils {

  def getFilesFrom(path: String): Array[File] = {
    try{
      new File(path).listFiles()
    }
    catch {
      case _: Exception =>
        Array[File]()
    }

  }
}

object FileContent {
  def apply(file: File): String = {
    loadFileData(file.getAbsolutePath)
      .mkString(LINE_BREAK)
  }

  @throws(classOf[FileNotFoundException])
  def loadFileData(path: String): Iterator[String]  =
    Source.fromFile(path)
      .getLines()
}