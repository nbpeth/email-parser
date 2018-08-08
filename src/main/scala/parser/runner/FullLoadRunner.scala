package parser.runner

import java.io.File
import java.util.concurrent.TimeUnit
import java.util.logging.Logger
import java.util.concurrent.TimeUnit.SECONDS
import javax.inject.{Inject, Singleton}
import parser.util.FileProcessor
import parser.util.FileParserExecutionContext._
import parser.writer.LineWriter

import scala.concurrent.duration.{Duration, FiniteDuration}
import scala.concurrent.{Await, Awaitable, Future}

@Singleton
case class FullLoadRunner @Inject()(writer: LineWriter, fileProcessor: FileProcessor) {
  private val logger = Logger.getLogger(getClass.getName)
  private var batchSize = 500

  def this(writer: LineWriter, fileProcessor: FileProcessor, batchSize: Int) = {
    this(writer, fileProcessor)
    this.batchSize = batchSize
  }

  def submit(load: FullLoad[File]): Unit = {
    process(load)
    powerDownTheMachine()
  }

  def process(load: FullLoad[File]): Unit ={
    while (load.hasRemaining) {
      val start = System.currentTimeMillis()

      val processedBatch = processNextBatchFrom(load)

      writer.dump(processedBatch)

      logger.info(s"Batch complete in ${System.currentTimeMillis() - start} millis")
    }
  }

  def processNextBatchFrom(load: FullLoad[File]): Seq[Any] ={
    val batchProcess = Future.sequence(
      gatherNextBatchFrom(load)
    )

    pauseToLet(batchProcess)(andWaitFor(10)(SECONDS))
  }

  def gatherNextBatchFrom(load: FullLoad[File]): Seq[Future[Any]] = {
    (0 to ceilingFor(load)).map(_ => {
      nextFileFrom(load)
    })
  }

  def nextFileFrom(load: FullLoad[File]): Future[Any] ={
    load.next match {
      case Some(nextFile) =>
        Future {
          fileProcessor.apply(nextFile)
        }
      case _ =>
        Future(None)
    }
  }

  def powerDownTheMachine(): Unit = {
    logger.info(s"Shutting down executor service and closing the file writer.")

    ec.shutdown()
    writer.shutItDown()
  }

  private def ceilingFor[T](batch: FullLoad[T]) =
    if(batch.remaining > batchSize) batchSize else batch.remaining

  private def pauseToLet(serviceCall: Awaitable[Seq[Any]])(duration: FiniteDuration): Seq[Any] =
    Await.result(serviceCall, duration)

  private def andWaitFor(l: Long)(t: TimeUnit) = Duration.apply(l, t)
}