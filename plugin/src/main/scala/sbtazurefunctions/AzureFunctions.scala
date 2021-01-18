package sbtazurefunctions

import sbt._

object AzureFunctionsKeys {
  val azfunHostJsonFile =
    settingKey[File]("Location of the host.json file")
  val azfunJarName =
    settingKey[String]("Name of the jar that holds the function definitions (default: AzureFunction.jar)")
  val azfunLocalSettingsFile =
    settingKey[File]("Location of the local.settings.json")
  val azfunTargetFolder = settingKey[File](
    "Target folder that receives the Azure function definitions"
  )
  val azfunZipName =
    settingKey[String]("Name of the zip file that will contain the results (default: AzureFunction.zip)")

  // settings for Azure resources
  val azfunAppInsightsName =
    settingKey[String]("Azure Application Insights instance (defaults to azfunFunctionAppName)")
  val azfunFunctionAppName = settingKey[String]("Function App Name for the Azure Function")
  val azfunLocation = settingKey[String]("Azure Location for the Azure Function")
  val azfunResourceGroup = settingKey[String]("Resource group that will hold the Azure Function")
  val azfunSKU = settingKey[String]("The Stock Keeping Unit (SKU) for the storage account (defaults to Standard_LRS)")
  val azfunStorageAccount = settingKey[String]("Storage Account for the Azure Function")

  val azfunCreateZipFile = taskKey[File](
    "Generate the zip file containing the complete Azure Function definition"
  )
  val azfunCopyHostJson = taskKey[File]("Copies host.json file")
  val azfunCopyLocalSettingsJson = taskKey[File]("Copies host.json file")
  val azfunDeploy = taskKey[Unit]("Deploys the function to Azure")
  val azfunGenerateFunctionJsons =
    taskKey[File]("Generates the function.json files for all annotated function entry points")

}

object AzureFunctions extends AutoPlugin {
  override def trigger = AllRequirements
  override def requires = sbt.plugins.JvmPlugin

  object autoImport {
    // settings available to users
    val azfunJarName = sbtazurefunctions.AzureFunctionsKeys.azfunJarName
    val azfunZipName = sbtazurefunctions.AzureFunctionsKeys.azfunZipName
    val azfunTargetFolder = sbtazurefunctions.AzureFunctionsKeys.azfunTargetFolder

    val azfunAppInsightsName = sbtazurefunctions.AzureFunctionsKeys.azfunAppInsightsName
    val azfunFunctionAppName = sbtazurefunctions.AzureFunctionsKeys.azfunFunctionAppName
    val azfunLocation = sbtazurefunctions.AzureFunctionsKeys.azfunLocation
    val azfunResourceGroup = sbtazurefunctions.AzureFunctionsKeys.azfunResourceGroup
    val azfunStorageAccount = sbtazurefunctions.AzureFunctionsKeys.azfunStorageAccount
    val azfunSKU = sbtazurefunctions.AzureFunctionsKeys.azfunSKU

    // tasks available to users
    val azfunCreateZipFile = sbtazurefunctions.AzureFunctionsKeys.azfunCreateZipFile
    val azfunDeploy = sbtazurefunctions.AzureFunctionsKeys.azfunDeploy
  }

  override lazy val projectSettings: Seq[Setting[_]] =
    DefaultSettings.settings ++ CopyTask.settings ++ CreateZipFileTask.settings ++ DeployTask.settings ++ GenerateFunctionsJsonTask.settings

}
