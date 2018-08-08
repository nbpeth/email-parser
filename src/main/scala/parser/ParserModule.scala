package parser

import com.google.inject.{AbstractModule, Guice}
import parser.config.JobConfiguration
import parser.reader.{EmailFileReader, FileReader}
import parser.runner.FullLoadRunner
import parser.writer.{EmailFileLineWriter, LineWriter}

class ParserModule extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[LineWriter]).to(classOf[EmailFileLineWriter])
    bind(classOf[FileReader]).to(classOf[EmailFileReader])
    bind(classOf[JobConfiguration]).asEagerSingleton()
    bind(classOf[FullLoadRunner]).asEagerSingleton()
  }
}

object Injector {
  val injector = Guice.createInjector(new ParserModule)
  val runner = injector.getInstance(classOf[FullLoadRunner])
  val config = injector.getInstance(classOf[JobConfiguration])
}