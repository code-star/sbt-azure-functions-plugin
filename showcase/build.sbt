lazy val root = (project in file("."))
  .settings(
    name := "showcase",
    version := "1.0",
    azfunZipName := "myFunctions",
    azfunJarName := "ScalaFunctions",
    assemblyOutputPath in assembly := azfunTargetFolder.value / s"${azfunJarName.value}.jar",
    libraryDependencies ++= Seq(
      "com.microsoft.azure.functions" % "azure-functions-java-library" % "1.3.1"
    ),
    azfunCreateZipFile := (azfunCreateZipFile dependsOn assembly).value
  )
