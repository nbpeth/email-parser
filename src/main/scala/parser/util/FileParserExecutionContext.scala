package parser.util

import java.util.concurrent.Executors

import scala.concurrent.{ExecutionContext, ExecutionContextExecutorService}

object FileParserExecutionContext {
  private val e = Executors.newFixedThreadPool(sys.runtime.availableProcessors())
  implicit val ec: ExecutionContextExecutorService = ExecutionContext.fromExecutorService(e)
}
