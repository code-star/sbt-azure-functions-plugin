package sbtazurefunctions

import nl.codestar.azurefunctions.DeployFunctionHelper
import sbt.Setting
import sbtazurefunctions.AzureFunctionsKeys._

object DeployTask {
  def settings: Seq[Setting[_]] =
    Seq(
      azfunDeploy := {
        // depend on having the zip available
        val artefact = azfunCreateZipFile.value

        val log = sbt.Keys.streams.value.log
        log.info("Running azfunDeploy task")

        val resourceGroup = azfunResourceGroup.value
        val location = azfunLocation.value
        DeployFunctionHelper.createResourceGroup(resourceGroup, location, log)

        val storageAccount = azfunStorageAccount.value
        val skuValue = azfunSKU.value
        DeployFunctionHelper.createStorageAccount(storageAccount, resourceGroup, location, skuValue, log)

        val aiName = azfunAppInsightsName.value
        DeployFunctionHelper.createAppInsightsInstance(aiName, resourceGroup, location, None, log)

        val appName = azfunFunctionAppName.value
        DeployFunctionHelper
          .createFunctionApp(appName, resourceGroup, location, storageAccount, aiName, log)

        DeployFunctionHelper.deployZipFile(artefact, appName, resourceGroup, log)
      }
    )
}
