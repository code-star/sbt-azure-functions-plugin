package sbtazurefunctions

import nl.codestar.azurefunctions.FunctionConfigGenerator
import org.reflections.util.ClasspathHelper
import sbt.Keys.{baseDirectory, target}
import sbt._
import sbt.io.Path.allSubpaths
import sbtassembly.AssemblyKeys.assembly

import scala.collection.JavaConverters.collectionAsScalaIterableConverter

object AzureFunctionsKeys {
  val azfunTargetFolder = settingKey[File](
    "Target folder that receives the Azure function definitions"
  )
  val azfunHostJsonFile =
    settingKey[File]("Location of the host.json file")
  val azfunJarName =
    settingKey[String]("Name of the jar that holds the function definitions (default: AzureFunction.jar)")
  val azfunLocalSettingsFile =
    settingKey[File]("Location of the local.settings.json")
  val azfunZipName =
    settingKey[String]("Name of the zip file that will contain the results (default: AzureFunction.zip)")

  val azfunCreateZipFile = taskKey[File](
    "Generate the zip file containing the complete Azure Function definition"
  )
  val azfunCopyHostJson = taskKey[File]("Copies host.json file")
  val azfunCopyLocalSettingsJson = taskKey[File]("Copies host.json file")
  val azfunGenerateFunctionJsons =
    taskKey[File]("Generates the function.json files for all annotated function entry points")

}

object AzureFunctions extends AutoPlugin {
  override def trigger = AllRequirements
  override def requires = sbt.plugins.JvmPlugin

  object autoImport {
    val AzureFunctionsKeys = sbtazurefunctions.AzureFunctionsKeys
    val azfunJarName = sbtazurefunctions.AzureFunctionsKeys.azfunJarName
    val azfunZipName = sbtazurefunctions.AzureFunctionsKeys.azfunZipName
    val azfunCreateZipFile = sbtazurefunctions.AzureFunctionsKeys.azfunCreateZipFile
  }

  import AzureFunctionsKeys._

  override lazy val projectSettings: Seq[Setting[_]] = Seq(
    azfunHostJsonFile := (baseDirectory in Compile).value / "host.json",
    azfunLocalSettingsFile := (baseDirectory in Compile).value / "local.settings.json",
    azfunTargetFolder := (target in Compile).value / stripExtension(azfunZipName.value),
    azfunZipName := "AzureFunction.zip",
    azfunJarName := "AzureFunction.jar",
    azfunCopyHostJson := {
      val log = sbt.Keys.streams.value.log
      val folder = azfunTargetFolder.value
      log.info(s"Placing host.json in $folder ...")

      val src = azfunHostJsonFile.value
      val tgt = azfunTargetFolder.value / "host.json"

      IO.copy(
        Seq((src, tgt)),
        CopyOptions.apply(
          overwrite = true,
          preserveLastModified = true,
          preserveExecutable = false
        )
      )
      tgt
    },
    azfunCopyLocalSettingsJson := {
      val log = sbt.Keys.streams.value.log
      val folder = azfunTargetFolder.value
      log.info(s"Placing local.settings.json in $folder ...")

      val src = azfunLocalSettingsFile.value
      val tgt = azfunTargetFolder.value / "local.settings.json"

      IO.copy(
        Seq((src, tgt)),
        CopyOptions.apply(
          overwrite = true,
          preserveLastModified = true,
          preserveExecutable = false
        )
      )
      tgt
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
      log.info(
        s"Creating Azure Function zip file in target folder ($tgtFolder) ..."
      )

      val src = azfunTargetFolder.value
      val tgt = tgtFolder / ensureExtension(azfunZipName.value, "zip")
      IO.zip(allSubpaths(src), tgt)
      tgt
    },
    azfunGenerateFunctionJsons := {
      // depend on assembly step
      val assemblyJar = assembly.value
      val log = sbt.Keys.streams.value.log

      val folder = azfunTargetFolder.value

      log.info("Running azureFunctions task...")
      log.info(s"Generating function.json files to $folder ...")

      val fatJarFile = folder / azfunJarName.value

      // copy the assembly jar into the folder that will be zipped eventually
      IO.copy(
        Seq((assemblyJar, fatJarFile))
      )
      val urls = ClasspathHelper.forManifest(fatJarFile.toURI.toURL).asScala.toList
      val configs = FunctionConfigGenerator.getConfigs(urls)

      val baseFolder = azfunTargetFolder.value
      FunctionConfigGenerator.generateFunctionJsons(azfunJarName.value, baseFolder.toPath, configs)
      azfunTargetFolder.value
    }
  )

  private def ensureExtension(input: String, extension: String): String = {
    input.lastIndexOf('.') match {
      case 0                          => s"${input}.${extension}"
      case n if (n == input.size - 1) => s"${input}${extension}"
      case _                          => input
    }
  }

  private def stripExtension(input: String): String = {
    input.lastIndexOf('.') match {
      case 0 => input
      case n => input.take(n)
    }
  }

}
