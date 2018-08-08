package parser.model

import org.scalatest.{MustMatchers, WordSpecLike}

class EmailMessageDataSpec extends WordSpecLike with MustMatchers {

  "toLine" should {
    val fileName = "some_file.txt"
    val dateSent = "12/31/99"
    val fromAddress = "great@times.com"
    val subject = "re: gamma ray bursts"
    val emailMessageData = EmailMessageData(
      fileName, dateSent, fromAddress, subject
    )

    "assemble a string the file properties separated by a delimiter" in {
      val expected = s"$fileName*$dateSent*$fromAddress*$subject"

      val result = emailMessageData.toLine("*")

      result mustEqual expected

    }

    "assemble a string the file properties separated by a different delimiter" in {
      val expected = s"$fileName@$dateSent@$fromAddress@$subject"

      val result = emailMessageData.toLine("@")

      result mustEqual expected
    }
  }

  "EmailMessageData" should {
    val fileName = "some_file.txt"
    val dateSent = "12/31/99\n\n"
    val fromAddress = "great@times.com\n\n\n"
    val subject = "re: gamma ray bursts\n And other things\n To keep you up at night"
    val emailMessageData = EmailMessageData(
      fileName, dateSent, fromAddress, subject
    )

    "remove line breaks from all fields when withLineBreaksRemoved invoked" in {
      val expected = EmailMessageData(
        "some_file.txt",
        "12/31/99",
        "great@times.com",
        "re: gamma ray bursts And other things To keep you up at night"
      )

      val result = emailMessageData.withLineBreaksRemoved

      result mustEqual expected
    }
  }

}
