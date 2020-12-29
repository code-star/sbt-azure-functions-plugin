import sbt.Keys.libraryDependencies

lazy val root = (project in file("."))
  .settings(
    version := "0.1",
    scalaVersion := "2.12.7",

    azfunTargetFolder := "myFunctions",
    azfunJarName := "ScalaFunctions.jar",

    assemblyOutputPath in assembly := target.value / "myFunctions" / azfunJarName.value,

    libraryDependencies ++= Seq(
      "com.microsoft.azure.functions" % "azure-functions-java-library" % "1.3.1"
    )

  )

