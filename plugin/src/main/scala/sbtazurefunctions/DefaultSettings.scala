package sbtazurefunctions

import sbt._
import sbt.Keys.{baseDirectory, target}
import sbt.Setting
import sbtazurefunctions.AzureFunctionsKeys.{
  azfunAppInsightsName,
  azfunFunctionAppName,
  azfunHostJsonFile,
  azfunJarName,
  azfunLocalSettingsFile,
  azfunSKU,
  azfunTargetFolder,
  azfunZipName
}

object DefaultSettings {
  def settings: Seq[Setting[_]] =
    Seq(
      azfunAppInsightsName := azfunFunctionAppName.value,
      azfunHostJsonFile := (Compile / baseDirectory).value / "host.json",
      azfunJarName := "AzureFunction.jar",
      azfunLocalSettingsFile := (Compile / baseDirectory).value / "local.settings.json",
      azfunSKU := "Standard_LRS",
      azfunTargetFolder := (Compile / target).value / stripExtension(azfunZipName.value),
      azfunZipName := "AzureFunction.zip"
    )

  private def stripExtension(input: String): String = {
    input.lastIndexOf('.') match {
      case 0 => input
      case n => input.take(n)
    }
  }

}
