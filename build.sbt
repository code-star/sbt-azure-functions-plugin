lazy val root = (project in file("."))
  .aggregate(plugin, library)
  .settings(
    bintrayRepository := "sbt-azure-functions",
    bintrayOrganization := Some("code-star"),
    bintrayPackageLabels := Seq("sbt", "plugin"),
    publishMavenStyle := false,
    publishArtifact in Test := false,
    // make sure the library is published locally before running scripted
    scripted := (scripted dependsOn publishLocal)
  )

lazy val library = (project in file("library"))
  .settings(
    name := "azure-functions-library",
    organization := "nl.codestar",
    libraryDependencies ++= Seq(
      "com.fasterxml.jackson.core" % "jackson-databind" % "2.12.0",
      "com.microsoft.azure" % "azure-tools-common" % "0.10.0",
      "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
      "com.microsoft.azure.functions" % "azure-functions-java-library" % "1.3.1" % "test",
      "org.scalatest" %% "scalatest" % "3.2.2" % "test"
    ),
    logBuffered in Test := false
  )

lazy val plugin = (project in file("plugin"))
  .enablePlugins(SbtPlugin)
  .settings(
    name := "sbt-azure-functions",
    organization := "nl.codestar",
    // version is set in version.sbt
    description := "SBT Plugin to generate function.json artefacts needed to publish code as an Azure Function",
    licenses += ("MIT", url("https://opensource.org/licenses/MIT")),
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
      "org.scala-sbt" %% "scripted-plugin" % sbtVersion.value
    ),
    addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.10"),
    scriptedLaunchOpts := {
      scriptedLaunchOpts.value ++
        Seq("-Xmx1024M", "-Dplugin.version=" + version.value)
    },
    scriptedBufferLog := false,
    logBuffered in Test := false
  )
  .dependsOn(library)

// workaround for interactive sessions that do not echo the user input (https://github.com/sbt/sbt-bintray/issues/177)
ThisBuild / useSuperShell := false
