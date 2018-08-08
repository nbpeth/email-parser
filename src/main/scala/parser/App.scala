package parser

import java.io.File
import java.util.logging.Logger

import parser.runner.FullLoad
import parser.util.FileUtils._

object App {
  private val logger = Logger.getLogger(getClass.getName)
  private val injector = Injector

  def main(args: Array[String]): Unit = {
    logger.info("Warming up the machine...")

    val files = getFilesFrom(injector.config.inputPath)

    run(files)
  }

  def run(files: Array[File]): Unit = {
    val start = System.currentTimeMillis()

    val fullLoad = FullLoad[File](files)
    injector.runner.submit(fullLoad)

    val end = System.currentTimeMillis() - start
    logger.info(s"${fullLoad.initialLoad} files processed in $end milliseconds")
  }
}

