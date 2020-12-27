import sbt.Keys.libraryDependencies

lazy val root = (project in file("."))
  .settings(
    version := "0.1",
    scalaVersion := "2.10.6",

    targetFunctionsFolder := "target/myFunctions",

    assemblyOutputPath in assembly := baseDirectory.value / "target" / "myFunctions" / "ScalaFunctions.jar",

    libraryDependencies ++= Seq(
      "com.microsoft.azure.functions" % "azure-functions-java-library" % "1.3.1"
    )

  )

