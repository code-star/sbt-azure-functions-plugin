import sbt.Keys.libraryDependencies

lazy val root = (project in file("."))
  .settings(
    version := "0.1",
    scalaVersion := "2.10.6",

    targetFunctionsFolder := "target/myFunctions",
    functionsJar := "ScalaFunctions.jar",

    assemblyOutputPath in assembly := baseDirectory.value / "target" / "myFunctions" / functionsJar.value,

    libraryDependencies ++= Seq(
      "com.microsoft.azure.functions" % "azure-functions-java-library" % "1.3.1"
    )

  )

