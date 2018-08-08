package parser.model

trait ReadableFile
case class EmailFile(content: String, fileName: String) extends ReadableFile
