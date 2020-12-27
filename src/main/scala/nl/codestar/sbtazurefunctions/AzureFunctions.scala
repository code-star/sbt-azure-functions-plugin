package nl.codestar.sbtazurefunctions

//import java.io.File

import sbt._

object AzureFunctions extends AutoPlugin {
  override def trigger = AllRequirements

  val targetFunctionsFolder = settingKey[String]("Target folder that receives the Azure function definitions")

}
