package sbtazurefunctions

import sbt._
import sbt.{CopyOptions, IO, Setting}
import sbtazurefunctions.AzureFunctionsKeys.{
  azfunCopyHostJson,
  azfunCopyLocalSettingsJson,
  azfunHostJsonFile,
  azfunLocalSettingsFile,
  azfunTargetFolder
}

object CopyTask {
  def settings: Seq[Setting[_]] =
    Seq(
      azfunCopyHostJson := {
        val log = sbt.Keys.streams.value.log
        val folder = azfunTargetFolder.value
        log.info(s"Placing host.json in $folder ...")

        val src = azfunHostJsonFile.value
        val tgt = azfunTargetFolder.value / "host.json"

        IO.copy(
          Seq((src, tgt)),
          CopyOptions.apply(
            overwrite = true,
            preserveLastModified = true,
            preserveExecutable = false
          )
        )
        tgt
      },
      azfunCopyLocalSettingsJson := {
        val log = sbt.Keys.streams.value.log
        val folder = azfunTargetFolder.value
        log.info(s"Placing local.settings.json in $folder ...")

        val src = azfunLocalSettingsFile.value
        val tgt = azfunTargetFolder.value / "local.settings.json"

        IO.copy(
          Seq((src, tgt)),
          CopyOptions.apply(
            overwrite = true,
            preserveLastModified = true,
            preserveExecutable = false
          )
        )
        tgt
      }
    )
}
