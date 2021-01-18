package sbtazurefunctions

import sbt._
import sbt.Keys.target
import sbt.io.Path.allSubpaths
import sbt.{IO, Setting}
import sbtazurefunctions.AzureFunctionsKeys._

object CreateZipFileTask {
  def settings: Seq[Setting[_]] =
    Seq(
      azfunCreateZipFile := {
        // depend on the steps that provide the contents of the zip
        val _ = {
          azfunCopyHostJson.value
          azfunCopyLocalSettingsJson.value
          azfunGenerateFunctionJsons.value
        }

        val log = sbt.Keys.streams.value.log

        val tgtFolder = (target in Compile).value

        log.info("Running azfunCreateZipFile task...")
        log.info(
          s"Creating Azure Function zip file in target folder ($tgtFolder) ..."
        )

        val src = azfunTargetFolder.value
        val tgt = tgtFolder / ensureExtension(azfunZipName.value, "zip")
        IO.zip(allSubpaths(src), tgt)
        tgt
      }
    )

  private def ensureExtension(input: String, extension: String): String = {
    input.lastIndexOf('.') match {
      case 0                          => s"${input}.${extension}"
      case n if (n == input.size - 1) => s"${input}${extension}"
      case _                          => input
    }
  }
}
