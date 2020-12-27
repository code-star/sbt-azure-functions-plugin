package nl.codestar.sample

import com.microsoft.azure.functions.ExecutionContext
import com.microsoft.azure.functions.HttpMethod
import com.microsoft.azure.functions.HttpRequestMessage
import com.microsoft.azure.functions.HttpResponseMessage
import com.microsoft.azure.functions.HttpStatus
import com.microsoft.azure.functions.annotation.AuthorizationLevel
import com.microsoft.azure.functions.annotation.FunctionName
import com.microsoft.azure.functions.annotation.HttpTrigger

import java.util.Optional

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

  @FunctionName("SecondFunction")
  def runAnother(
           @HttpTrigger(
             name="req2",
             methods = Array(HttpMethod.GET),
             authLevel = AuthorizationLevel.ANONYMOUS) request: HttpRequestMessage[Optional[String]],
           context: ExecutionContext): HttpResponseMessage = {
    context.getLogger.info("Scala HTTP Trigger received a request")
    request.createResponseBuilder(HttpStatus.OK).body("Hello there").build()
  }
}
