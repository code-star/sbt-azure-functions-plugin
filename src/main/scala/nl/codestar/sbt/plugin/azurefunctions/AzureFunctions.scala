package nl.codestar.sbt.plugin.azurefunctions

import org.reflections.util.ClasspathHelper
import sbt._

import java.nio.file.Paths
import scala.collection.JavaConverters.collectionAsScalaIterableConverter


object AzureFunctions extends AutoPlugin {
  override def trigger = AllRequirements

  object autoImport {
    val azfunTargetFolder = settingKey[String]("Target folder that receives the Azure function definitions")
    val azfunJarName = settingKey[String]("Name of the jar that holds the function definitions")

    val azureFunctions = taskKey[Unit]("Generates the function.json files for all annotated function entry points")
  }

  import autoImport._

  override lazy val projectSettings: Seq[Setting[_]] = Seq(
    azureFunctions := {
      val log = sbt.Keys.streams.value.log

      val folder = azfunTargetFolder.value

      log.info(s"Running azureFunctions task. Generating to $folder")

      val urls = ClasspathHelper.forManifest(Paths.get(azfunTargetFolder.value + "/" + azfunJarName.value).toUri.toURL).asScala.toList
      val configs = FunctionConfigGenerator.getConfigs(urls)

      FunctionConfigGenerator.generateFunctionJsons(azfunJarName.value, Paths.get(folder), configs, Some(log))
    }
  )


}
