package nl.codestar.sbt.plugin.azurefunctions

import com.fasterxml.jackson.databind.{ObjectMapper, ObjectWriter}
import com.microsoft.azure.common.function.configurations.FunctionConfiguration
import com.microsoft.azure.common.function.handlers.AnnotationHandlerImpl
import sbt.internal.util.ManagedLogger

import java.lang.reflect.Method
import java.net.URL
import java.nio.file.{Files, Path}
import scala.collection.JavaConverters._

object FunctionConfigGenerator {
  val handler = new AnnotationHandlerImpl()

  def getFunctions(urls: List[URL]): Set[Method] = {
    handler.findFunctions(urls.asJava).asScala.toSet
  }

  def getConfigs(urls: List[URL]): Map[String, FunctionConfiguration] = {
    val functions = getFunctions(urls)
    handler.generateConfigurations(functions.asJava).asScala.toMap
  }

  def generateFunctionJsons( jarName: String, baseFolder: Path, configs: Map[String, FunctionConfiguration]): Unit = {
    generateFunctionJsons(jarName, baseFolder, configs, None)
  }

  def generateFunctionJsons( jarName: String, baseFolder: Path, configs: Map[String, FunctionConfiguration], log: Option[ManagedLogger]): Unit = {
    // ensure baseFolder exists
    log.foreach(_.info(s"Ensuring $baseFolder exists..."))
    Files.createDirectories(baseFolder)

    // add ScriptFilePath to the configs (fill it with '../$jarName.jar')
    log.foreach(_.info(s"Setting scriptFile parameter on ${configs.size} configs..."))
    configs.foreach(config => {
      config._2.setScriptFile(s"../$jarName")
    })

    // for each K->V in map write function.json to folder K, using values from V
    log.foreach(_.info(s"Writing ${configs.size} configs..."))
    val myWriter = getWriter
    configs.foreach(config =>{
      log.foreach(_.info(s"Processing ${config._1}..."))
      val folder = baseFolder.resolve(config._1)
      Files.createDirectories(folder)
      val jsonFile = folder.resolve("function.json")
      myWriter.writeValue(jsonFile.toFile, config._2)
    })
  }

  private def getWriter: ObjectWriter = {
    new ObjectMapper().writerWithDefaultPrettyPrinter()
  }

}
