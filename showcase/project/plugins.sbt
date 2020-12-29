addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.10")

// build root project
lazy val root = Project("plugins", file(".")) dependsOn(ProjectRef(pluginParent, "root"))

// depends on the plugin project
lazy val pluginParent = file("..").getAbsoluteFile
