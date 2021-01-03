addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.10")

// build root project
lazy val root = Project("showcase", file(".")) dependsOn (ProjectRef(pluginParent, "plugin"))

// depends on the plugin project
lazy val pluginParent = file("..").getAbsoluteFile
