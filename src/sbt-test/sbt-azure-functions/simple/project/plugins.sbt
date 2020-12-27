addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.15.0")

sys.props.get("plugin.version") match {
  case Some(x) => addSbtPlugin("nl.codestar" % "sbt-azure-functions" % x)
  case _ => sys.error("""|The system property 'plugin.version' is not defined.
                         |Specify this property using the scriptedLaunchOpts -D.""".stripMargin)
}
