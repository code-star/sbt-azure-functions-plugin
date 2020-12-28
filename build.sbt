
lazy val root = (project in file("."))
  .enablePlugins(SbtPlugin)
  .settings(
    name := "sbt-azure-functions",
    organization := "nl.codestar",
    // version is set in version.sbt
    description := "SBT Plugin to generate function.json artefacts needed to publish code as an Azure Function",
    licenses += ("MIT", url("https://opensource.org/licenses/MIT")),

    scalacOptions ++= Seq("-encoding", "UTF8", "-Xfatal-warnings",
      "-deprecation", "-feature", "-unchecked", "-Xlint",
      "-Ywarn-dead-code", "-Ywarn-adapted-args"
    ),

    libraryDependencies ++= Seq(
      "org.scala-sbt" %% "scripted-plugin" % sbtVersion.value,

      "com.fasterxml.jackson.core" % "jackson-databind" % "2.12.0",
      "com.microsoft.azure.functions" % "azure-functions-java-library" % "1.3.1",
      "com.microsoft.azure" % "azure-functions-maven-plugin" % "1.9.0",
      "com.microsoft.azure" % "azure-tools-common" % "0.10.0",

      "org.scalatest" %% "scalatest" % "3.2.2" % "test"

    ),

    scriptedLaunchOpts := { scriptedLaunchOpts.value ++
      Seq("-Xmx1024M", "-Dplugin.version=" + version.value)
    },
    scriptedBufferLog := false,

    logBuffered in Test := false,

    bintrayRepository := "sbt-azure-functions",
    bintrayOrganization := Some("code-star"),


    publishMavenStyle := false,
    publishArtifact in Test := false
  )
