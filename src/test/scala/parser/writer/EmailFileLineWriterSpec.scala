package parser.writer

import java.io.BufferedWriter

import org.mockito.Mockito._
import org.scalatest.{MustMatchers, WordSpecLike}
import parser.config.Constants._
import parser.config.JobConfiguration
import parser.model.EmailMessageData

class EmailFileLineWriterSpec extends WordSpecLike with MustMatchers {
  private val config = mock(classOf[JobConfiguration])
  private val writerFactory = mock(classOf[WriterFactory])
  private var mockBufferedWriter: BufferedWriter = _
  private val emailData1 = EmailMessageData("file", "date", "address", "subject")
  private val emailData2 = EmailMessageData("file2", "date2", "address2", "subject2")
  private val emailData3 = EmailMessageData("file3", "date3", "address3", "subject3")

  "shut it down" should {
    "invoke writer.close" in {
      setupMocks()
      val lineWriter = EmailFileLineWriter(config, writerFactory)

      lineWriter.shutItDown()

      verify(writerFactory.getWriter, times(1)).close()
    }
  }

  "lineWriter" should {
    "be using the writer that is injected" in {
      setupMocks()

      val actualWriter = EmailFileLineWriter(config, writerFactory).writer

      actualWriter mustEqual writerFactory.getWriter

    }

    "write data and flush the buffer" in {
      setupMocks()
      val lineWriter = EmailFileLineWriter(config, writerFactory)
      val data = Seq[EmailMessageData](emailData1, emailData2, emailData3)

      lineWriter.dump(data)

      verify(mockBufferedWriter).append(expectedLineFrom(emailData1))
      verify(mockBufferedWriter).append(expectedLineFrom(emailData2))
      verify(mockBufferedWriter).append(expectedLineFrom(emailData3))
      verify(mockBufferedWriter).flush()
    }

    "only write email data if something nasty sneaks in" in {
      setupMocks()
      val badSneakyData1 = "Nope!"
      val badSneakyData2 = "Hold on, now!"
      val lineWriter = EmailFileLineWriter(config, writerFactory)
      val data = Seq[Any](emailData1, badSneakyData1, emailData2, badSneakyData2, emailData3)

      lineWriter.dump(data)

      verify(mockBufferedWriter).append(expectedLineFrom(emailData1))
      verify(mockBufferedWriter).append(expectedLineFrom(emailData2))
      verify(mockBufferedWriter).append(expectedLineFrom(emailData3))
      verify(mockBufferedWriter, never()).append(badSneakyData1)
      verify(mockBufferedWriter, never()).append(badSneakyData2)
      verify(mockBufferedWriter).flush()
    }
  }

  def setupMocks(): Unit = {
    mockBufferedWriter = mock(classOf[BufferedWriter])
    when(writerFactory.getWriter).thenReturn(mockBufferedWriter)
    when(config.outputFileDelimiter).thenReturn("*")
  }

  def expectedLineFrom(emailMessageData: EmailMessageData) =
    s"${emailMessageData.toLine(config.outputFileDelimiter)}$LINE_BREAK"
}
