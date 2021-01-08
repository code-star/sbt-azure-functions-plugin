lazy val root = (project in file("."))
  .settings(
    name := "showcase",
    version := "1.0",
    libraryDependencies ++= Seq(
      "com.microsoft.azure.functions" % "azure-functions-java-library" % "1.3.1"
    )
  )
