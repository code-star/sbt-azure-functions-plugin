package nl.codestar.sbt.plugin.azurefunctions

import com.microsoft.azure.common.function.bindings.BindingEnum
import com.microsoft.azure.functions.annotation.{AuthorizationLevel, FunctionName, HttpTrigger}
import com.microsoft.azure.functions._
import org.reflections.util.ClasspathHelper
import org.scalatest.flatspec.AnyFlatSpec

import java.util.Optional

class FunctionConfigGeneratorTest extends AnyFlatSpec {

  class SampleAzureFunctions {
    @FunctionName("ScalaFunction")
    def run(
             @HttpTrigger(
               name="req",
               methods = Array(HttpMethod.GET),
               authLevel = AuthorizationLevel.ANONYMOUS) request: HttpRequestMessage[Optional[String]],
             context: ExecutionContext): HttpResponseMessage = {
      context.getLogger.info("Scala HTTP Trigger received a request")
      request.createResponseBuilder(HttpStatus.OK).body("Hello there").build()
    }
  }

  private def getClassUrl = ClasspathHelper.forPackage("nl.codestar.sbt.plugin.azurefunctions").iterator.next

  "An annotated function" should "be found" in {
      val result = FunctionConfigGenerator.getFunctions(List(getClassUrl))
      assert (result.isEmpty == false)
    }

  "An annotated function" should "produce a configuration" in {
      val result = FunctionConfigGenerator.getConfigs(List(getClassUrl))
      assert (result.isEmpty == false)
      assert ( result.get("ScalaFunction") != null)
      val config = result.get("ScalaFunction").get
      assert (config.getEntryPoint == "nl.codestar.sbt.plugin.azurefunctions.FunctionConfigGeneratorTest.SampleAzureFunctions.run")
      assert (config.getScriptFile == null)
      val binding = config.getBindings.get(0)
      assert ( binding.getBindingEnum == BindingEnum.HttpTrigger)
      assert ( binding.getAttribute("name") == "req")
    }
}
