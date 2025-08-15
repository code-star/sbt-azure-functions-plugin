lazy val commonSettings = Seq(
  organization := "nl.codestar",
  homepage := Some(url("https://github.com/code-star/sbt-azure-functions-plugin")),
  // version is set by sbt-dynver plugin (included through sbt-ci-assembly)
  description := "SBT Plugin to generate function.json artefacts needed to publish code as an Azure Function",
  licenses += ("MIT", url("https://opensource.org/licenses/MIT")),
  developers := List(
    Developer(
      "jeanmarc",
      "Jean-Marc van Leerdam",
      "jean-marc.vanleerdam@soprasteria.com",
      url("https://soprasteria.com")
    )
  )
)

lazy val root = project.in(file("."))
  .aggregate(plugin)
  .settings(
    name := "sbt-azure-functions-plugin",
    commonSettings,
    // the root project should not produce any artifacts
    publishArtifact := false,
    publish := {}
  )

lazy val plugin = project.in(file("plugin"))
  .enablePlugins(SbtPlugin)
  .settings(
    name := "sbt-azure-functions",
    commonSettings,
    scalacOptions ++= Seq(
      "-encoding",
      "UTF8",
      "-Xfatal-warnings",
      "-deprecation",
      "-feature",
      "-unchecked",
      "-Xlint",
      "-Ywarn-dead-code",
      "-Ywarn-adapted-args"
    ),
    libraryDependencies ++= Seq(
      "com.fasterxml.jackson.core" % "jackson-databind" % "2.19.2",
      "com.microsoft.azure" % "azure-tools-common" % "0.14.0",
      "com.typesafe.scala-logging" %% "scala-logging" % "3.9.5",
      "com.microsoft.azure.functions" % "azure-functions-java-library" % "3.1.0" % "test",
      "org.scalatest" %% "scalatest" % "3.2.19" % "test",
      "org.scala-sbt" %% "scripted-plugin" % sbtVersion.value
    ),
    addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "2.3.1"),
    scriptedLaunchOpts := {
      scriptedLaunchOpts.value ++
        Seq("-Xmx1024M", "-Dplugin.version=" + version.value)
    },
    scriptedBufferLog := false,
    Test / logBuffered := false,
    Test / publishArtifact := false
  )

// workaround for interactive sessions that do not echo the user input (https://github.com/sbt/sbt-bintray/issues/177)
ThisBuild / useSuperShell := false
