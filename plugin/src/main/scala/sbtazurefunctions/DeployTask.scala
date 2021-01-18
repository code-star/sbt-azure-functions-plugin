package sbtazurefunctions

import sbt.Setting
import sbtazurefunctions.AzureFunctionsKeys.{
  azfunCreateZipFile,
  azfunDeploy,
  azfunLocation,
  azfunResourceGroup,
  azfunSKU,
  azfunStorageAccount
}

object DeployTask {
  def settings: Seq[Setting[_]] =
    Seq(
      azfunDeploy := {
        // depend on having the zip available
        val _ = azfunCreateZipFile.value
        //val artefact = azfunCreateZipFile.value

        val log = sbt.Keys.streams.value.log
        log.info("Running azfunDeploy task")

        import scala.language.postfixOps
        import scala.sys.process._

        val resourceGroup = azfunResourceGroup.value
        log.info(s"Checking if group ${resourceGroup} exists...")
        val output = (s"az group exists -n ${resourceGroup}" !!)
        val groupExists = output.startsWith("true")
        if (!groupExists) {
          log.info(s"Group ${resourceGroup} does not yet exist, creating it")
          val result = (s"az group create -n ${resourceGroup} -l ${azfunLocation.value}" !)
          if (result != 0) {
            log.error("Failed to create resource group")
          } else {
            log.info("Resource group created")
          }
        } else {
          log.info(s"Group ${resourceGroup} is present")
        }

        val storageAccount = azfunStorageAccount.value
        log.info(s"Checking if storage account ${storageAccount} exists...")
        val output2 = (s"az storage account check-name -n ${storageAccount} --query nameAvailable" !!)
        val saExists = output2.startsWith("false")
        if (!saExists) {
          log.info(s"Storage Account ${storageAccount} does not yet exist, creating it")
          val result =
            s"az storage account create -n ${storageAccount} -l ${azfunLocation.value} -g ${resourceGroup} --sku ${azfunSKU.value}" !

          if (result != 0) {
            log.error("Failed to create storage account")
          } else {
            log.info("Storage account created")
          }
        } else {
          log.info(s"Storage account ${storageAccount} is present")
        }

      }
    )
}
