import sbt.Keys.libraryDependencies

lazy val root = (project in file("."))
  .settings(
    version := "0.1",
    scalaVersion := "2.12.7",
    azfunZipName := "myFunctions",
    azfunJarName := "ScalaFunctions",
    assemblyOutputPath in assembly := azfunTargetFolder.value / s"${azfunJarName.value}.jar",
    libraryDependencies ++= Seq(
      "com.microsoft.azure.functions" % "azure-functions-java-library" % "1.3.1"
    )
  )
