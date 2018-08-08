package parser.reader

import java.io.File

import org.mockito.Mockito._
import org.scalatest.{MustMatchers, WordSpecLike}
import parser.model.EmailFile

class EmailFileReaderSpec extends WordSpecLike with MustMatchers {
  private val fileName = "file.txt"
  private val filePath = s"/path/to/some/$fileName"
  private val mockFile = mock(classOf[File])

  "read" should {
    "create EmailFile with file content and filename" in {
      val reader = spy(EmailFileReader())
      val expectedContent = "line1\nline2"
      setupMocks(expectedContent, reader)

      val result = reader.read(mockFile)

      result mustBe EmailFile(expectedContent, fileName)

    }

    "create EmailFile with different content and filename" in {
      val reader = spy(EmailFileReader())
      val expectedContent = "otherContent\nmoreContent"
      setupMocks(expectedContent, reader)

      val result = reader.read(mockFile)

      result mustBe EmailFile(expectedContent, fileName)
    }
  }


  def setupMocks(expectedContent: String, reader: EmailFileReader): Unit = {
    when(mockFile.getAbsolutePath).thenReturn(filePath)
    when(mockFile.getName).thenReturn(fileName)
    doReturn(expectedContent, expectedContent).when(reader).getContentsFrom(mockFile)
  }
}
