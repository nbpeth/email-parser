package parser.runner

import java.io.File
import java.util.concurrent.TimeUnit

import org.mockito.ArgumentMatchers._
import org.mockito.Mockito._
import org.scalatest.{Matchers, WordSpecLike}
import parser.model.EmailMessageData
import parser.util.FileProcessor
import parser.util.FileParserExecutionContext._
import parser.writer.LineWriter

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class FullLoadRunnerSpec extends WordSpecLike with Matchers {
  private var mockWriter: LineWriter = _
  private var mockFile: File = _
  private var mockEmailMessageData: EmailMessageData = _
  private var mockFieldExtractor: FileProcessor = _

  "process load" should {
    "call the field extractor for every file in batch and send them to the writer" in {
      setupMocks()
      val load = FullLoad.apply(mockFiles(10))
      val runner = new FullLoadRunner(mockWriter, mockFieldExtractor, 10)

      runner.process(load)

      verify(mockFieldExtractor, times(load.initialLoad)).apply(mockFile)
      verify(mockWriter, times(1)).dump(any())
    }

    "call the field extractor for each file and submit sub-batches to writer" in {
      setupMocks()
      val load = FullLoad.apply(mockFiles(20))
      val runner = new FullLoadRunner(mockWriter, mockFieldExtractor, 10)

      runner.process(load)

      verify(mockFieldExtractor, times(load.initialLoad)).apply(mockFile)
      verify(mockWriter, times(2)).dump(any())
    }

    "call the field extractor for each file and submit sub-batches to writer with a different batch size" in {
      setupMocks()
      val load = FullLoad.apply(mockFiles(30))
      val runner = new FullLoadRunner(mockWriter, mockFieldExtractor, 10)

      runner.process(load)

      verify(mockFieldExtractor, times(load.initialLoad)).apply(mockFile)
      verify(mockWriter, times(3)).dump(any())
    }

    "process nothing and proceed gracefully if the initial full load is empty" in {
      setupMocks()
      val load = FullLoad.apply(mockFiles(0))
      val runner = new FullLoadRunner(mockWriter, mockFieldExtractor, 10)

      runner.process(load)

      verify(mockFieldExtractor, never()).apply(mockFile)
      verify(mockWriter, never()).dump(any())
    }

    "call the field extractor for each file and submit sub-batches to writer with a large and uneven file size" in {
      setupMocks()
      val batch = FullLoad.apply(mockFiles(1152))
      val runner = new FullLoadRunner(mockWriter, mockFieldExtractor, 200)

      runner.process(batch)

      verify(mockFieldExtractor, times(batch.initialLoad)).apply(mockFile)
      verify(mockWriter, times(6)).dump(any())

    }
  }

  "nextFileFrom(batch)" should {
    "return Future of EmailMessage data when a file is passed in" in {
      setupMocks()
      val batch = FullLoad.apply(mockFiles(1))
      val runner = FullLoadRunner(mockWriter, mockFieldExtractor)

      val result = Await.result(runner.nextFileFrom(batch), Duration.apply(2, TimeUnit.SECONDS))

      assert(result == mockEmailMessageData)
    }
  }

  "nextFileFrom(batch)" should {
    "return Future of None when not a file is passed in" in {
      setupMocks()
      val batch = FullLoad.apply(mockFiles(0))
      val runner = FullLoadRunner(mockWriter, mockFieldExtractor)

      val result = Await.result(runner.nextFileFrom(batch), Duration.apply(2, TimeUnit.SECONDS))

      assert(result == None)
    }
  }

  "powerDownTheMachine" should {
    "close file writer and shutdown executor" in {
      setupMocks()
      val runner = FullLoadRunner(mockWriter, mockFieldExtractor)

      runner.powerDownTheMachine()

      verify(mockWriter, times(1)).shutItDown()
      assert(ec.isShutdown)
    }
  }

  def setupMocks(): Unit = {
    mockWriter = mock(classOf[LineWriter])
    mockFile = mock(classOf[File])
    mockEmailMessageData = mock(classOf[EmailMessageData])
    mockFieldExtractor = mock(classOf[FileProcessor])

    when(mockFieldExtractor.apply(mockFile)).thenReturn(mockEmailMessageData)
  }

  def mockFiles(n: Int): Array[File] =
    (1 to n).foldLeft(Array[File]()) { (r, _) => r :+ mockFile }

}
