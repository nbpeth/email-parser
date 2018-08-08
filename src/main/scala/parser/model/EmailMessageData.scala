package parser.model
import parser.config.Constants.{BLANK, LINE_FEED}
trait MessageData {
  def toLine(delimiter: String): String
}

case class EmailMessageData(fileName: String, dateSent: String, fromAddress: String, var subject: String) extends MessageData {
  subject = subject.replaceAll(LINE_FEED, BLANK)

  def toLine(delimiter: String): String =
    props.mkString(delimiter)

  private def props: Seq[String] = Seq(fileName, dateSent, fromAddress, subject)
}
