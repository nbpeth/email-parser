package parser.config

import javax.inject.Singleton

@Singleton
class JobConfiguration {
  val batchSize = 500
  val inputPath = "/Users/npeth/Downloads/email-parser/src/main/resources/sample"
//  val inputPath = "/Users/budmanstrong/Documents/email-parser/src/main/resources/sample"
  val outputFile = "/Users/npeth/Downloads/email-parser/src/main/resources/output/output.txt"
  val outputFileDelimiter = "|"

}
