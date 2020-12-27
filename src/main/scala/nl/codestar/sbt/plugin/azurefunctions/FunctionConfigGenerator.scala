package nl.codestar.sbt.plugin.azurefunctions

import com.microsoft.azure.common.function.configurations.FunctionConfiguration
import com.microsoft.azure.common.function.handlers.AnnotationHandlerImpl

import java.lang.reflect.Method
import java.net.URL
import collection.JavaConverters._

object FunctionConfigGenerator {
  val handler = new AnnotationHandlerImpl()

  def getFunctions(urls: List[URL]): Set[Method] = {
    handler.findFunctions(urls.asJava).asScala.toSet
  }

  def getConfigs(urls: List[URL]): Map[String, FunctionConfiguration] = {
    val functions = getFunctions(urls)
    handler.generateConfigurations(functions.asJava).asScala.toMap
  }
}
