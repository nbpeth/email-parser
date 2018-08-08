package parser

object TestData {

  val message1 =
    """
      |Resent-From: Mary Smith <mary@example.net>
      |Resent-To: Jane Brown <j-brown@other.example>
      |Resent-Date: Mon, 24 Nov 1997 14:22:01 -0800
      |Resent-Message-ID: <78910@example.net>
      |From: John Doe <jdoe@machine.example>
      |To: Mary Smith <mary@example.net>
      |Subject: Saying Hello
      |Date: Fri, 21 Nov 1997 09:55:06 -0600
      |Message-ID: <1234@local.machine.example>
      |
      |This is a message just to say hello.
      |So, "Hello".
    """.stripMargin

  val message2 =
    """
      |From: Mary Smith <bary@example.net>
      |To: John Doe <jdoe@machine.example>
      |Reply-To: "Mary Smith: Personal Account" <smith@home.example>
      |Subject: Re: Saying Hello
      |Date: Fri, 21 Nov 1997 10:01:10 -0600
      |Message-ID: <3456@example.net>
      |In-Reply-To: <1234@local.machine.example>
      |References: <1234@local.machine.example>
      |
      |This is a reply to your hello.
      |<html><body>
      |Subject: cats:From:Bags
      |From:cheese@sandwich.com, subject: Subject
      |Date: 123Date
      |</html></body>
    """.stripMargin

  val messageWithMissingFromHeader =
    """
      |To: John Doe <jdoe@machine.example>
      |Reply-To: "Mary Smith: Personal Account" <smith@home.example>
      |Subject: Re: Saying Hello
      |Date: Fri, 21 Nov 1997 10:01:10 -0600
      |Message-ID: <3456@example.net>
      |In-Reply-To: <1234@local.machine.example>
      |References: <1234@local.machine.example>
      |
      |This is a reply to your hello.
    """.stripMargin

  val messageWithMissingSubject =
    """
      |From: "Joe Q. Public" <john.q.public@example.com>
      |To: Mary Smith <mary@x.test>, jdoe@example.org, Who? <one@y.test>
      |Cc: <boss@nil.test>, "Giant; \"Big\" Box" <sysservices@example.net>
      |Date: Tue, 1 Jul 2003 10:52:37 +0200
      |Message-ID: <5678.21-Nov-1997@example.com>
      |
      |Hi everyone.
    """.stripMargin

  val messageWithMissingDateHeader =
    """
      |From: John Doe <jdoe@machine.example>
      |To: Mary Smith <mary@example.net>
      |Subject: Saying Hello
      |Message-ID: <1234@local.machine.example>
      |
      |This is a message just to say hello.
      |So, "Hello".
    """.stripMargin

  val messageWithMultilineSubject =
    """
      |From: John Doe <jdoe@machine.example>
      |To: Mary Smith <mary@example.net>
      |Subject: Saying Hello
      | on a new line
      | and another new line
      | of course.
      |Date: Fri, 21 Nov 1997 09:55:06 -0600
      |Message-ID: <1234@local.machine.example>
      |
      |This is a message just to say hello.
      |So, "Hello".
    """.stripMargin
}
