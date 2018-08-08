package parser.writer

import java.io.{BufferedWriter, File, FileWriter}
import javax.inject.{Inject, Singleton}
import parser.config.JobConfiguration

@Singleton
case class WriterFactory @Inject()(config: JobConfiguration) {
  private val outputFile = new File(config.outputFile)

  val getWriter = new BufferedWriter(new FileWriter(outputFile))

}
