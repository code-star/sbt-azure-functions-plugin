import sbt.Keys.libraryDependencies

lazy val root = (project in file("."))
  .settings(
    version := "0.1",
    scalaVersion := "2.12.7",
    libraryDependencies ++= Seq(
      "com.microsoft.azure.functions" % "azure-functions-java-library" % "1.3.1"
    ),
    assemblyJarName in assembly := "ScalaFunctions.jar"
  )
