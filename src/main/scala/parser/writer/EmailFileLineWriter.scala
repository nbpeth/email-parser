package parser.writer

import java.io.BufferedWriter

import javax.inject.{Inject, Singleton}
import parser.config.JobConfiguration
import parser.config.Constants._
import parser.model.EmailMessageData

trait LineWriter {
  def writer: BufferedWriter
  def shutItDown(): Unit
  def dump(data: Seq[Any])
}

@Singleton
case class EmailFileLineWriter @Inject()(config: JobConfiguration, writerFactory: WriterFactory) extends LineWriter {
  def writer = writerFactory.getWriter
  def shutItDown(): Unit = writer.close()

  def dump(data: Seq[Any]): Unit = {
    data.foreach {
      case data @ EmailMessageData(_, _, _, _) =>
        val line = data.toLine(config.outputFileDelimiter)
        write(line)
      case _ =>
    }

    writer.flush()
  }

  private def write(line: String): Unit =
    writer.append(s"$line$LINE_BREAK")

}
