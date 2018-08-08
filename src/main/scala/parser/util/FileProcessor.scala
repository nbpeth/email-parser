package parser.util

import java.io.File

import javax.inject.Inject
import parser.model.{EmailFile, EmailMessageData}
import parser.patterns.HeaderPatterns
import parser.reader.EmailFileReader
import parser.config.Constants._

import scala.concurrent.ExecutionContext

case class FileProcessor @Inject()(reader: EmailFileReader) {
  import parser.patterns.HeaderPatterns._

  def apply(file: File)(implicit executionContext: ExecutionContext): EmailMessageData = {
    val emailFile = reader.read(file)
    val emailFileWithBodyRemovedFromContent = stripBodyFromContentIn(emailFile)
    val headers = mapHeaders(emailFileWithBodyRemovedFromContent)

    createRecordFrom(headers)(emailFileWithBodyRemovedFromContent)
  }

  def createRecordFrom(headers: Map[String, String])(emailFile: EmailFile): EmailMessageData = {
    val from = getFrom(headers)
    val subject = getSubject(headers)
    val date = getDate(headers)

    EmailMessageData(emailFile.fileName, date, from, subject).withLineBreaksRemoved
  }

  def stripBodyFromContentIn(emailFile: EmailFile): EmailFile = {
    emailFile.content.split(headersSectionPattern) match {
      case headers if headers.nonEmpty =>
        emailFile.copy(content = headers(0))
      case _ =>
        emailFile
    }
  }

  def mapHeaders(emailFile: EmailFile): Map[String, String] = {
    val headers = HeaderPatterns.headerPattern.r.findAllIn(emailFile.content)

    headers.foldLeft(Map[String, String]())(extractAndFormatHeaders)
  }

  private val extractAndFormatHeaders = (r: Map[String, String], n: String) => {
    val fields = n.split(HEADER_DELIMITER)
    r + (getKeyFrom(fields) -> getValueFrom(fields))
  }

  private def getSubject(headers: Map[String, String])() =
    headers.getOrElse(SUBJECT_HEADER, BLANK).trim()

  private def getDate(headers: Map[String, String]) =
    headers.getOrElse(DATE_HEADER, BLANK).trim()

  private def getFrom(headers: Map[String, String]) =
    findEmailInFromHeader(headers.getOrElse(FROM_HEADER, BLANK))

  private def findEmailInFromHeader(from: String) =
    emailAddressPattern.r.findFirstIn(from).getOrElse(BLANK)

  private def getKeyFrom(fields: Array[String]) =
    if(fields.length > 0) fields(0).trim() else BLANK

  private def getValueFrom(a: Array[String]) =
    a.tail.mkString(HEADER_DELIMITER)

}



