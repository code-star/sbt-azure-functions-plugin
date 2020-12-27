
// build the local version of the plugin
lazy val root = Project("plugins", file(".")) dependsOn(ProjectRef(parentFolder, "sbt-azure-functions-plugin"))

// depends on the parent project
lazy val parentFolder = file("..").getAbsoluteFile
