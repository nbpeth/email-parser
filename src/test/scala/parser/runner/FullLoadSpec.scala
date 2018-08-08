package parser.runner

import java.io.File

import org.scalatest.{MustMatchers, WordSpecLike}

class FullLoadSpec extends WordSpecLike with MustMatchers {

  "FullLoad" should {
    "accept an array of Strings and enqueue them" in {
      val strings = Array[String]("1", "2", "3")
      val load = FullLoad[String](strings)

      load.initialLoad mustBe 3
      load.remaining mustBe 3
      load.hasRemaining mustBe true
    }

    "accept an array of Integers and enqueue them" in {
      val numbers = Array[Int](3, 2, 1, 0)
      val load = FullLoad[Int](numbers)

      load.initialLoad mustBe 4
      load.remaining mustBe 4
      load.hasRemaining mustBe true
    }

    "be totally OK with accepting an empty list" in {
      val emptyFileArray = Array[File]()
      val load = FullLoad[File](emptyFileArray)

      load.initialLoad mustBe 0
      load.remaining mustBe 0
      load.hasRemaining mustBe false
    }

    "behave like a queue" in {
      val numbers = Array[Int](1, 2, 3, 4)
      val load = FullLoad[Int](numbers)

      load.next.get mustBe 1
      load.hasRemaining mustBe true
      load.remaining mustBe 3

      load.next.get mustBe 2
      load.hasRemaining mustBe true
      load.remaining mustBe 2

      load.next.get mustBe 3
      load.hasRemaining mustBe true
      load.remaining mustBe 1

      load.next.get mustBe 4
      load.hasRemaining mustBe false
      load.remaining mustBe 0
      load.next.getOrElse(-1) mustBe -1

    }

    "not throw an exception when calling next if the inner queue is empty and return None" in {
      val emptyFileArray = Array[File]()
      val load = FullLoad[File](emptyFileArray)

      try {
        val result = load.next
        result mustBe None
      }
      catch {
        case _ : Exception =>
          fail("Should not throw an exception!")
      }
    }
  }
}
