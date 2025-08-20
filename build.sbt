lazy val commonSettings = Seq(
  organization := "nl.codestar",
  homepage := Some(url("https://github.com/code-star/sbt-azure-functions-plugin")),
  // version is set by sbt-dynver plugin (included through sbt-ci-release)
  description := "SBT Plugin to generate function.json artefacts needed to publish code as an Azure Function",
  organization := "nl.codestar",
  organizationName := "Codestar powered by Sopra Steria",
  organizationHomepage := Some(url("https://codestar.nl")),
  homepage := Some(url("https://codestar.nl/sbt-azure-functions-plugin")),
  licenses += ("MIT", url("https://opensource.org/licenses/MIT")),
  developers := List(
    Developer(
      "jeanmarc",
      "Jean-Marc van Leerdam",
      "jean-marc.vanleerdam@soprasteria.com",
      url("https://soprasteria.com")
    )
  ),
  scmInfo := Some(
    ScmInfo(
      url("https://github.com/code-star/sbt-azure-functions-plugin"),"scm:git@github.com:code-star/sbt-azure-functions-plugin.git")
  ),
  credentials ++= Seq(
    Credentials(
      "GnuPG Key ID",
      "gpg",
      System.getenv("PGP_KEYID"), // key identifier
      "ignored" // this field is ignored; passwords are supplied by pinentry
    ),
    Credentials(
      "Sonatype Nexus Repository Manager",
      "oss.sonatype.org",
      System.getenv("SONATYPE_USER"),
      System.getenv("SONATYPE_PASSWORD") // Use environment variable for security
    ),
    Credentials(
      "Sonatype Nexus Repository Manager",
      "central.sonatype.com",
      System.getenv("SONATYPE_USER"),
      System.getenv("SONATYPE_PASSWORD") // Use environment variable for security
    ),
    Credentials(
      "central-snapshots",
      "central.sonatype.com",
      System.getenv("SONATYPE_USER"),
      System.getenv("SONATYPE_PASSWORD") // Use environment variable for security
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
    sonaDeploymentName := {
      val o = organization.value
      val n = name.value
      val v = version.value
      val dt = java.time.LocalDateTime.now.format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"))
      s"$o:$n:$v:$dt"
    },

    scalaVersion := "2.12.18",
    pluginCrossBuild / sbtVersion := {
      scalaBinaryVersion.value match {
        case "2.12" => "1.11.4" // set minimum version
      }
    },
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

ThisBuild / pomIncludeRepository := { _ => false }
ThisBuild / publishMavenStyle := true

ThisBuild / publishTo := {
  val centralSnapshots = "https://central.sonatype.com/repository/maven-snapshots/"
  if (isSnapshot.value) Some("central-snapshots" at centralSnapshots)
  else localStaging.value
}
