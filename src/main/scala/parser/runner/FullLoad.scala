package parser.runner

import scala.collection.mutable

case class FullLoad[T](objects: Array[T]) {
  private val queue = mutable.Queue[T]()
  objects.foreach(queue.enqueue(_))

  val initialLoad: Int = queue.size

  def next: Option[T] = if(hasRemaining) Option(queue.dequeue()) else None
  def remaining: Int = queue.size
  def isEmpty: Boolean = queue.isEmpty
  def hasRemaining: Boolean = queue.nonEmpty

}