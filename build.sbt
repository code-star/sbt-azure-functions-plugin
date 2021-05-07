lazy val root = (project in file("."))
  .aggregate(plugin)
  .settings(
    name := "sbt-azure-functions-plugin",
    // the root project should not produce any artifacts
    publishArtifact := false,
    publish := {}
  )

lazy val plugin = (project in file("plugin"))
  .enablePlugins(SbtPlugin)
  .settings(
    name := "sbt-azure-functions",
    organization := "nl.codestar",
    homepage := Some(url("https://github.com/code-star/sbt-azure-functions-plugin")),
    // version is set by sbt-dynver plugin (included through sbt-ci-assembly)
    description := "SBT Plugin to generate function.json artefacts needed to publish code as an Azure Function",
    licenses += ("MIT", url("https://opensource.org/licenses/MIT")),
    developers := List(
      Developer(
        "jeanmarc",
        "Jean-Marc van Leerdam",
        "jean-marc.van.leerdam@ordina.nl",
        url("https://github.com/jeanmarc")
      )
    ),
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
      "com.fasterxml.jackson.core" % "jackson-databind" % "2.12.0",
      "com.microsoft.azure" % "azure-tools-common" % "0.10.0",
      "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
      "com.microsoft.azure.functions" % "azure-functions-java-library" % "1.3.1" % "test",
      "org.scalatest" %% "scalatest" % "3.2.2" % "test",
      "org.scala-sbt" %% "scripted-plugin" % sbtVersion.value
    ),
    addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.10"),
    scriptedLaunchOpts := {
      scriptedLaunchOpts.value ++
        Seq("-Xmx1024M", "-Dplugin.version=" + version.value)
    },
    scriptedBufferLog := false,
    logBuffered in Test := false,
    publishArtifact in Test := false
  )

// workaround for interactive sessions that do not echo the user input (https://github.com/sbt/sbt-bintray/issues/177)
ThisBuild / useSuperShell := false
