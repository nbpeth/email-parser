package parser.patterns

object HeaderPatterns {
  //  val fromPattern = "(?m)^From:\\s*.*\\s*.<?[a-zA-Z0-9@.-]*>?$"
  //  val subjectPattern = "(?m)^Subject:\\s*(.*)(\\n\\s+(.*))*"
  //  val datePattern = "(?m)^Date:\\s*(.*)"

  val emailAddressPattern = "[a-zA-Z\\.\\-\\_0-9]*@[a-zA-Z0-9-]*.[a-zA-Z]*\\.?[a-zA-Z]*"
  val headerPattern = "(?m)^[A-Za-z\\-]*:\\s*(.*)(\\n\\s+(.*))*"
  val headersSectionPattern = "\n\\s*\n"
}