package nl.codestar.azurefunctions

import com.microsoft.azure.common.function.bindings.BindingEnum
import com.microsoft.azure.functions._
import com.microsoft.azure.functions.annotation.{AuthorizationLevel, FunctionName, HttpTrigger}
import org.reflections.util.ClasspathHelper
import org.scalatest.flatspec.AnyFlatSpec

import java.nio.file.{Files, Paths}
import java.util.Optional

class FunctionConfigGeneratorTest extends AnyFlatSpec {

  class SampleAzureFunctions {
    @FunctionName("ScalaFunction")
    def run(
        @HttpTrigger(
          name = "req",
          methods = Array(HttpMethod.GET),
          authLevel = AuthorizationLevel.ANONYMOUS
        ) request: HttpRequestMessage[Optional[String]],
        context: ExecutionContext
    ): HttpResponseMessage = {
      context.getLogger.info("Scala HTTP Trigger received a request")
      request.createResponseBuilder(HttpStatus.OK).body("Hello there").build()
    }
  }

  private def getClassUrl = ClasspathHelper.forPackage("nl.codestar.azurefunctions").iterator.next

  "An annotated function" should "be found" in {
    val result = FunctionConfigGenerator.getFunctions(List(getClassUrl))
    assert(result.isEmpty == false)
  }

  "An annotated function" should "produce a configuration" in {
    val result = FunctionConfigGenerator.getConfigs(List(getClassUrl))
    assert(result.isEmpty == false)
    assert(result.get("ScalaFunction") != null)
    val config = result.get("ScalaFunction").get
    assert(config.getEntryPoint == "nl.codestar.azurefunctions.FunctionConfigGeneratorTest.SampleAzureFunctions.run")
    assert(config.getScriptFile == null)
    val binding = config.getBindings.get(0)
    assert(binding.getBindingEnum == BindingEnum.HttpTrigger)
    assert(binding.getAttribute("name") == "req")
  }

  "An annotated function" should "produce function.json" in {
    val configs = FunctionConfigGenerator.getConfigs(List(getClassUrl))
    FunctionConfigGenerator.generateFunctionJsons("MyJarname", Paths.get("target/functions"), configs)
    assert(Files.exists(Paths.get("target/functions")))
    assert(Files.exists(Paths.get("target/functions/ScalaFunction")))
    assert(Files.exists(Paths.get("target/functions/ScalaFunction/function.json")))
  }
}
