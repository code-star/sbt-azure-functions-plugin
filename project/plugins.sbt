
addSbtPlugin("org.foundweekends" % "sbt-bintray" % "0.5.5")


libraryDependencies += {"org.scala-sbt" %% "scripted-plugin" % sbtVersion.value}
libraryDependencies += {"com.microsoft.azure.functions" % "azure-functions-java-library" % "1.3.1"}
