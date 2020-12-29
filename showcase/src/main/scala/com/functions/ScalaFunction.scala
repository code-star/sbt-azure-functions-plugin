package com.functions

import com.microsoft.azure.functions.ExecutionContext
import com.microsoft.azure.functions.HttpMethod
import com.microsoft.azure.functions.HttpRequestMessage
import com.microsoft.azure.functions.HttpResponseMessage
import com.microsoft.azure.functions.HttpStatus
import com.microsoft.azure.functions.annotation.AuthorizationLevel
import com.microsoft.azure.functions.annotation.FunctionName
import com.microsoft.azure.functions.annotation.HttpTrigger

import java.util.Optional

class ScalaFunction{
  @FunctionName("ScalaFunction")
  def run(
           @HttpTrigger(
             name = "req",
             methods = Array(HttpMethod.GET, HttpMethod.POST),
             authLevel = AuthorizationLevel.ANONYMOUS) request: HttpRequestMessage[
             Optional[String]],
           context: ExecutionContext): HttpResponseMessage = {
    val query = request.getQueryParameters.get("name")
    val name: String = request.getBody.orElse(query)

    if (name == null) {
      request
        .createResponseBuilder(HttpStatus.BAD_REQUEST)
        .body("Please pass a name on the query string or in the request body")
        .build()
    } else {
      request
        .createResponseBuilder(HttpStatus.OK)
        .body("Hello from ScalaFunction, " + name)
        .build()
    }
  }
}