import sbt.Keys.libraryDependencies

lazy val root = (project in file("."))
  .settings(
    version := "0.1",
    scalaVersion := "2.12.18",
    libraryDependencies ++= Seq(
      "com.microsoft.azure.functions" % "azure-functions-java-library" % "3.1.0"
    ),
    assembly / assemblyJarName := "ScalaFunctions.jar",
    azfunFunctionAppName := "rd-scala-functions",
    azfunLocation := "westeurope",
    azfunResourceGroup := "rg-rd-scala-functions",
    azfunStorageAccount := "a77a749630954151919e"
  )
