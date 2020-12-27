
enablePlugins(SbtPlugin)

name := "sbt-azure-functions"
organization := "nl.codestar"
version := "0.1-SNAPSHOT"
description := "SBT Plugin to generate function.json artefacts needed to publish code as an Azure Function"

scalaVersion := "2.12.12"
scalacOptions ++= Seq("-encoding", "UTF8", "-Xfatal-warnings",
  "-deprecation", "-feature", "-unchecked", "-Xlint",
  "-Ywarn-dead-code", "-Ywarn-adapted-args"
)

pluginCrossBuild / sbtVersion := {
  scalaBinaryVersion.value match {
    case "2.12" => "1.2.8"
  }
}

scriptedLaunchOpts := { scriptedLaunchOpts.value ++
  Seq("-Xmx1024M", "-Dplugin.version=" + version.value)
}
scriptedBufferLog := false

bintrayRepository := "sbt-azure-functions"
bintrayOrganization := Some("code-star")

licenses += ("MIT", url("https://opensource.org/licenses/MIT"))

publishMavenStyle := false
publishArtifact in Test := false
