package parser.config

import javax.inject.Singleton

@Singleton
class JobConfiguration {
  val batchSize = 500
  val inputPath = "./src/main/resources/sample"
  val outputFile = "./src/main/resources/output/output.txt"
  val outputFileDelimiter = "|"

}
