package nl.codestar.sbt.plugin.azurefunctions

//import java.io.File

import sbt._

object AzureFunctions extends AutoPlugin {
  override def trigger = AllRequirements

  object autoImport {
    val targetFunctionsFolder = settingKey[String]("Target folder that receives the Azure function definitions")

    val azureFunctions = taskKey[Unit]("Generates the function.json files for all annotated function entry points")
  }

  import autoImport._

  override lazy val projectSettings: Seq[Setting[_]] = Seq(
    azureFunctions := {
        val log = sbt.Keys.streams.value.log

        val folder = targetFunctionsFolder.value

        log.info(s"Running azureFunctions task. Generating to $folder")
      }
    )


}
