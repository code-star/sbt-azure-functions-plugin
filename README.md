# SBT Azure Functions Plugin

Experimental plugin for sbt to create Azure Function artefacts (function.json) needed to publish code as an Azure Function.

## Setup and Usage

* Setup
  
    in your `project/plugins.sbt` add sbt-assembly and sbt-azure-functions:
  
      addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.10")
      addSbtPlugin("nl.codestar" % "sbt-azure-functions" % "0.1")

    in your `build.sbt` provide values for the assembly and azure-functions plugins:

        lazy val root = (project in file("."))
        .settings(
            ...
        
            azfunTargetFolder := "target/myFunctions",
            azfunJarName := "ScalaFunctions.jar",
        
            assemblyOutputPath in assembly := baseDirectory.value / "target" / "myFunctions" / functionsJar.value,
        
            // you need this dependency to be able to use the annotations
            libraryDependencies ++= Seq(
              "com.microsoft.azure.functions" % "azure-functions-java-library" % "1.3.1"
            )
        
        )

* Usage

    `sbt assembly azureFunctions`

    This will generate the fat jar that you want to upload to Azure (`assembly`), and then generates the function
    specifications (`function.json` in separate folders for each method that has been annotated with an `@FunctionName`
    annotation, and further detailed with `@HttpTrigger` annotation)

##TODO: 
1. include copying the `host.json` and possibly the `local.settings.json`
1. create the zip file that holds the jar + json files
1. add task to upload to Azure
1. provide a sample project showing plugin usage


## Cross compiling and testing
Since this is an sbt plugin, it should be usable with multiple versions of sbt. This means it needs to be built and
released for different scala versions and different sbt versions. I have not (yet) found a good resource that documents
what Scala version is used for each sbt release, except for https://github.com/sbt/sbt/issues/5032, 
so I am also keeping track here:

| SBT release(s)| Scala version     | Remarks                                          |
|---------------|-------------------|--------------------------------------------------|
| 0.x           | 2.10.x            |
| 1.x           | 2.12.x            |
| 2.x           | 2.13.x or 3.0.x   |
| 3.x           | 3.0.x or 3.1.x    |

For now, I will focus only on sbt 1.x and Scala 2.12.x

## Releasing
To release a new version:
* Get a [bintray](https://bintray.com) account and make sure you're a member of the [`code-star`](https://bintray.com/code-star) organization.
* Set your credentials - you can use `sbt bintrayChangeCredentials`, but when run from the interactive sbt prompt
  you will not see the requests for username and password. So blindly first type your username, press enter, then
  paste your API key and press enter again.

    (found a workaround that shows the prompt again: add to build.sbt: `ThisBuild / useSuperShell := false`)
* reload to make new settings known to sbt
* Run `sbt publish`

