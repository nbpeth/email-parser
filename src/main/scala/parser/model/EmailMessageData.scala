package parser.model
import parser.config.Constants.{BLANK, LINE_FEED}
trait MessageData {
  def toLine(delimiter: String): String
}

case class EmailMessageData(var fileName: String, var dateSent: String, var fromAddress: String, var subject: String) extends MessageData {

  def toLine(delimiter: String): String =
    props.mkString(delimiter)

  def withLineBreaksRemoved: EmailMessageData = {
    subject = subject.replaceAll(LINE_FEED, BLANK)
    dateSent = dateSent.replaceAll(LINE_FEED, BLANK)
    fromAddress = fromAddress.replaceAll(LINE_FEED, BLANK)
    this
  }

  private def props: Seq[String] =
    Seq(fileName, dateSent, fromAddress, subject)
}
