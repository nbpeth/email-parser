package parser.util

import java.io.File

import org.mockito.Mockito._
import org.scalatest.{MustMatchers, WordSpecLike}
import parser.TestData
import parser.model.{EmailFile, EmailMessageData}
import parser.reader.EmailFileReader
import parser.util.FileParserExecutionContext._

class FileProcessorSpec extends WordSpecLike with MustMatchers {
  private val reader = mock(classOf[EmailFileReader])
  private val mockFile = mock(classOf[File])
  private val filename = "filename.txt"

  "Extractor" should {
    "get all the fields from the email header" in {
      val emailFile = EmailFile(TestData.message1, filename)
      when(reader.read(mockFile)).thenReturn(emailFile)
      val expectedResult = EmailMessageData(filename, "Fri, 21 Nov 1997 09:55:06 -0600", "jdoe@machine.example","Saying Hello")

      val result = FileProcessor(reader).apply(mockFile)

      result mustEqual expectedResult

    }

    "get all the fields from a different email header" in {
      val emailFile = EmailFile(TestData.message2, filename)
      when(reader.read(mockFile)).thenReturn(emailFile)
      val expectedResult = EmailMessageData(filename, "Fri, 21 Nov 1997 10:01:10 -0600", "bary@example.net", "Re: Saying Hello")

      val result = FileProcessor(reader).apply(mockFile)

      result mustEqual expectedResult

    }

    "return empty string for missing 'subject' header" in {
      val emailFile = EmailFile(TestData.messageWithMissingSubject, filename)
      when(reader.read(mockFile)).thenReturn(emailFile)

      val result = FileProcessor(reader).apply(mockFile)

      result.subject mustEqual ""

    }

    "find a complete multiline 'subject' header" in {
      val emailFile = EmailFile(TestData.messageWithMultilineSubject, filename)
      when(reader.read(mockFile)).thenReturn(emailFile)
      val expectedSubject = """Saying Hello on a new line and another new line of course."""

      val result = FileProcessor(reader).apply(mockFile)

      result.subject mustEqual expectedSubject

    }

    "return empty string for missing 'date' header" in {
      val emailFile = EmailFile(TestData.messageWithMissingDateHeader, filename)
      when(reader.read(mockFile)).thenReturn(emailFile)

      val result = FileProcessor(reader).apply(mockFile)

      result.dateSent mustEqual ""

    }

    "return empty string for missing 'from' header" in {
      val emailFile = EmailFile(TestData.messageWithMissingFromHeader, filename)
      when(reader.read(mockFile)).thenReturn(emailFile)

      val result = FileProcessor(reader).apply(mockFile)

      result.fromAddress mustEqual ""
    }

  }
}
