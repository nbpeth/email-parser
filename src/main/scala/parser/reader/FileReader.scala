package parser.reader

import java.io.File
import javax.inject.Singleton

import parser.model.{EmailFile, ReadableFile}
import parser.util.FileContent

trait FileReader {
  def read(file: File): ReadableFile
}

@Singleton
case class EmailFileReader() extends FileReader {
  def read(file: File): EmailFile = {
    val content = getContentsFrom(file)
    val fileName = file.getName

    EmailFile(content, fileName)
  }

  def getContentsFrom(file: File) =
    FileContent.apply(file)
}
