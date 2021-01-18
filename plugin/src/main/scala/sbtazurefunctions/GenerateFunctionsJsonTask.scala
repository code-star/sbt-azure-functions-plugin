package sbtazurefunctions

import sbt._
import scala.collection.JavaConverters.collectionAsScalaIterableConverter

import nl.codestar.azurefunctions.FunctionConfigGenerator
import org.reflections.util.ClasspathHelper
import sbt.{IO, Setting}
import sbtassembly.AssemblyKeys.assembly
import sbtazurefunctions.AzureFunctionsKeys.{azfunGenerateFunctionJsons, azfunJarName, azfunTargetFolder}

object GenerateFunctionsJsonTask {
  def settings: Seq[Setting[_]] =
    Seq(
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
}
