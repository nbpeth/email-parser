package parser.util

import java.io.{BufferedWriter, File, FileNotFoundException, FileWriter}

import org.scalatest.{MustMatchers, WordSpecLike}
import parser.config.Constants.LINE_BREAK

class FileContentSpec extends WordSpecLike with MustMatchers {

  "loadFileData" should {
    "throw FileNotFoundException if the path to file is invalid" in {
      val fakePath = "/some/fake/path/to/file.txt"

      try {
        FileContent.loadFileData(fakePath)
      }
      catch {
        case e: FileNotFoundException =>
          e.getMessage mustEqual s"$fakePath (No such file or directory)"
        case _ =>
          fail("Should have thrown exception.")
      }
    }
  }

  "FileContent" should {
    "get file contents as lines with line breaks as a string" in {
      val linesForFile = Array("line1","line2","line3")
      val tempFile = buildTempFileUsing(linesForFile)
      val expected = linesForFile.mkString(LINE_BREAK)

      val result = FileContent.apply(tempFile)

      result mustEqual expected

      cleanUp(tempFile)
    }
  }

  private def buildTempFileUsing(linesForFile: Array[String]): File = {
    val tempFileName = "tempFile.txt"
    val tempFile = new File(tempFileName)
    writeContentToFile(tempFile)(linesForFile)

    tempFile
  }

  private def cleanUp(file: File): Boolean =
    file.delete()


  private def writeContentToFile(file: File)(lines: Array[String]): Unit ={
    val writer = new BufferedWriter(new FileWriter(file))
    lines.foreach(line => writer.append(s"$line$LINE_BREAK"))
    writer.close()
  }
}
