package nl.codestar.azurefunctions

import sbt.File
import sbt.internal.util.ManagedLogger

class FunctionDeployFailedException(msg: String, cause: Throwable = None.orNull) extends sbt.FeedbackProvidedException

object DeployFunctionHelper {
  import scala.language.postfixOps
  import scala.sys.process._

  def createResourceGroup(rgName: String, location: String, log: ManagedLogger): Unit = {
    log.info(s"Checking if group ${rgName} exists...")

    val output = (s"az group exists -n ${rgName}" !!)
    val groupExists = output.startsWith("true")
    if (!groupExists) {
      log.info(s"Group ${rgName} does not yet exist, creating it")
      val result = (s"az group create -n ${rgName} -l ${location}" !)
      if (result != 0) {
        log.error("Failed to create resource group")
        throw new FunctionDeployFailedException("Failed to create resource group")
      } else {
        log.info("Resource group created")
      }
    } else {
      log.info(s"Group ${rgName} is present")
    }

  }

  def createStorageAccount(
      saName: String,
      rgName: String,
      location: String,
      skuValue: String,
      log: ManagedLogger
  ): Unit = {
    log.info(s"Checking if storage account ${saName} exists...")

    val output = (s"az storage account check-name -n ${saName} --query nameAvailable" !!)
    val saExists = output.startsWith("false")
    if (!saExists) {
      log.info(s"Storage Account ${saName} does not yet exist, creating it")

      val result = (s"az storage account create -n ${saName} -l ${location} -g ${rgName} --sku ${skuValue}" !)
      if (result != 0) {
        log.error("Failed to create storage account")
        throw new FunctionDeployFailedException("Failed to create storage account")
      } else {
        log.info("Storage account created")
      }
    } else {
      log.info(s"Storage account ${saName} is present")
    }

  }

  def createAppInsightsInstance(
      aiName: String,
      rgName: String,
      location: String,
      workspace: Option[String],
      log: ManagedLogger
  ): Unit = {
    log.info(s"Checking if app insights ${aiName} exists...")
    val output = (s"az monitor app-insights component show --app ${aiName} -g ${rgName}" !)
    if (output != 0) {
      log.info(s"App Insights ${aiName} does not yet exist, creating it")

      val workspacePart = workspace match {
        case None     => ""
        case Some(ws) => s"--workspace $ws"
      }
      val result = (s"""
        |az monitor app-insights component create
        |   --app $aiName
        |   --location $location
        |   --resource-group $rgName
        |   --application-type web
        |   $workspacePart
        |""".stripMargin !)
      if (result != 0) {
        log.error("Failed to create App Insights")
        throw new FunctionDeployFailedException("Failed to create App Insights")
      } else {
        log.info("App Insights created")
      }
    } else {
      log.info(s"App Insights $aiName is present")
    }
  }

  def createFunctionApp(
      appName: String,
      rgName: String,
      location: String,
      saName: String,
      aiName: String,
      log: ManagedLogger
  ): Unit = {
    log.info(s"Checking if function app $appName exists...")
    val output = (s"az functionapp show -g $rgName --name $appName" !)
    if (output != 0) {
      log.info(s"Function App $appName does not yet exist, creating it")
      val result = (s"""
           |az functionapp create
           |  --name $appName
           |  --resource-group $rgName
           |  --storage-account $saName
           |  --consumption-plan-location $location
           |  --app-insights $aiName
           |  --runtime java
           |""".stripMargin !)
      if (result != 0) {
        log.error("Failed to create Function App")
        throw new FunctionDeployFailedException("Failed to create Function App")
      } else {
        log.info("Function App created")
      }
    } else {
      log.info(s"Function App $appName exists")
    }
  }

  def deployZipFile(zipFile: File, appName: String, rgName: String, log: ManagedLogger): Unit = {
    val zipLocation = zipFile.getAbsolutePath
    log.info(s"Uploading $zipLocation into $appName")
    val result = (s"""az functionapp deployment source config-zip 
          |   -g $rgName 
          |   -n $appName 
          |   --src $zipLocation
          |""".stripMargin.replaceAll("\n", " ") !)
    if (result != 0) {
      log.error("Failed to upload zip file")
      throw new FunctionDeployFailedException("Failed to upload zip file")
    } else {
      log.info("Upload done")
    }
  }
}
