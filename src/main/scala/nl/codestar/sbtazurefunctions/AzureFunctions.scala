package nl.codestar.sbtazurefunctions

//import java.io.File

import nl.codestar.sbtazurefunctions.AzureFunctions.autoImport.azureFunctions
import sbt.{Def, _}

object AzureFunctions extends AutoPlugin {
  override def trigger = AllRequirements

  object autoImport {
    val targetFunctionsFolder = settingKey[String]("Target folder that receives the Azure function definitions")

    val azureFunctions = taskKey[Unit]("Generates the function.json files for all annotated function entry points")
  }

  override def buildSettings: Seq[Def.Setting[_]] = { Seq[Def.Setting[_]](
    azureFunctions := {
        val log = sbt.Keys.streams.value.log

        //val folder = targetFunctionsFolder.value

        log.debug("debug message")
        //log.info(s"info message. We will generate to $folder")
        log.info("info message")
        log.warn("warning message")
        log.error("error message")
      }
    ) }


  import autoImport._

  azureFunctions := {
    val log = sbt.Keys.streams.value.log

    val folder = targetFunctionsFolder.value

    log.debug("debug message")
    log.info(s"info message. We will generate to $folder")
    log.warn("warning message")
    log.error("error message")
  }

}
