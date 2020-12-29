package nl.codestar.sbt.plugin.azurefunctions

import org.reflections.util.ClasspathHelper
import sbt.Keys.{baseDirectory, target}
import sbt._
import sbt.io.Path.allSubpaths

import scala.collection.JavaConverters.collectionAsScalaIterableConverter


object AzureFunctions extends AutoPlugin {
  override def trigger = AllRequirements

  object autoImport {
    val azfunTargetFolder = settingKey[String]("Target folder that receives the Azure function definitions")
    val azfunJarName = settingKey[String]("Name of the jar that holds the function definitions")
    val azfunHostJsonFolder = settingKey[String]("Location of the host.json file")
    val azfunLocalSettingsFolder = settingKey[String]("Location of the local.settings.json")

    val azfunCreateZipFile = taskKey[Unit]("Generate the zip file containing the complete Azure Function definition")
    val azfunCopyHostJson = taskKey[Unit]("Copies host.json file")
    val azfunCopyLocalSettingsJson = taskKey[Unit]("Copies host.json file")
    val azfunGenerateFunctionJsons = taskKey[Unit]("Generates the function.json files for all annotated function entry points")
  }

  import autoImport._

  override lazy val projectSettings: Seq[Setting[_]] = Seq(
    azfunHostJsonFolder := ".",
    azfunLocalSettingsFolder := ".",

    azfunCopyHostJson := {
      val log = sbt.Keys.streams.value.log
      val folder = azfunTargetFolder.value
      log.info(s"Placing host.json in $folder ...")

      val src = (baseDirectory in Compile).value / azfunHostJsonFolder.value / "host.json"
      val tgt = (target in Compile).value / folder / "host.json"

      IO.copy(Seq((src, tgt)), CopyOptions.apply(overwrite = true, preserveLastModified = true, preserveExecutable = false))
    },

    azfunCopyLocalSettingsJson := {
      val log = sbt.Keys.streams.value.log
      val folder = azfunTargetFolder.value
      log.info(s"Placing local.settings.json in $folder ...")

      val src = (baseDirectory in Compile).value / azfunLocalSettingsFolder.value / "local.settings.json"
      val tgt = (target in Compile).value / folder / "local.settings.json"

      IO.copy(Seq((src, tgt)), CopyOptions.apply(overwrite = true, preserveLastModified = true, preserveExecutable = false))
    },

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
      log.info(s"Creating Azure Function zip file in target folder ($tgtFolder) ...")

      val folderName = azfunTargetFolder.value
      val src = tgtFolder / folderName
      val tgt = tgtFolder / s"$folderName.zip"
      IO.zip(allSubpaths(src), tgt)
    },

    azfunGenerateFunctionJsons := {
      val log = sbt.Keys.streams.value.log

      val folder = azfunTargetFolder.value

      log.info("Running azureFunctions task...")
      log.info(s"Generating function.json files to $folder ...")

      val fatJarFile = (target in Compile).value / azfunTargetFolder.value / azfunJarName.value
      val urls = ClasspathHelper.forManifest(fatJarFile.toURI.toURL).asScala.toList
      val configs = FunctionConfigGenerator.getConfigs(urls)

      val baseFolder = (target in Compile).value / azfunTargetFolder.value
      FunctionConfigGenerator.generateFunctionJsons(azfunJarName.value, baseFolder.toPath, configs, Some(log))
    }
  )

}
