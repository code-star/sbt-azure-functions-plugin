# SBT Azure Functions Plugin 
<<TODO: add download link from new location (once moved from bintray)>>

Develop branch: ![Scala CI](https://github.com/code-star/sbt-azure-functions-plugin/workflows/Scala%20CI/badge.svg?branch=develop)

Experimental plugin for sbt to create Azure Function artefacts (function.json) needed to publish code as an Azure Function.

## Setup and Usage

### Setup
  
in your `project/plugins.sbt` add sbt-assembly and sbt-azure-functions:
  
    addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "2.3.1")
    addSbtPlugin("nl.codestar" % "sbt-azure-functions" % "<latest version>")

in your `build.sbt` provide values for the assembly and azure-functions plugins:

    lazy val root = (project in file("."))
      .settings(
          ...
          // replace these values with appropriate values for your Azure Function
          azfunFunctionAppName := "ScalaFunction",
          azfunLocation := "azure-location", // e.g. "westeurope", "eastus", etc.
          azfunResourceGroup := "your-resource-group",
          azfunStorageAccount := "yourstorageaccount"

          // optional: override the zip and/or jar name (defaults are AzureFunction.zip and AzureFunction.jar)
          azfunZipName := "myFunctions.zip",
          azfunJarName := "ScalaFunctions.jar",
      
          // you need this dependency to be able to use the annotations
          libraryDependencies ++= Seq(
            "com.microsoft.azure.functions" % "azure-functions-java-library" % "3.1.0"
          )
        
      )

### Usage

* `sbt azfunCreateZipFile`

    This will generate the fat jar that you want to upload to Azure (`assembly`), and then generates the function
    specifications (`function.json` in separate folders for each method that has been annotated with an `@FunctionName`
    annotation, and further detailed with `@HttpTrigger` annotation).
  
    The `azfunCreateZipFile` task will automatically trigger the following intermediate tasks that could also be
    called individually:
  
    * `azfunGenerateFunctionJsons` - to make the `function.json` files (implicitly calls `assembly` task)
    * `azfunCopyHostJson` - to copy the `host.json` file
    * `azfunCopyLocalSettingsJson` - to copy the `local.settings.json` file

* `sbt azfunDeploy`

    This will deploy the function to Azure, using the azure CLI, which is expected to be available on your path
    and logged in to the correct Azure Subscription.
    You will also have to install the app-insights extension to the CLI, by running `az extension add -n application-insights`
  
    You must provide the following settings to determine the destination:
    * `azfunResourceGroup`
    * `azfunStorageAccount`
    

## TODO: 
1. add support for App Insights workspaces
1. add tests against multiple Java versions (java 8 and Java 11)


## Cross compiling and testing
### SBT and Scala
Since this is an sbt plugin, it should be usable with multiple versions of sbt. This means it needs to be built and
released for different scala versions and different sbt versions. I have not (yet) found a good resource that documents
what Scala version is used for each sbt release, except for https://github.com/sbt/sbt/issues/5032, 
so I am also keeping track here:

| SBT release(s)  | Scala version   | Remarks |
|-----------------|-----------------|---------|
| 0.x             | 2.10.x          |         |
| 1.x             | 2.12.x          |         |
| 2.x             | 2.13.x or 3.0.x |         |
| 3.x             | 3.0.x or 3.1.x  |         |

For now, I will focus only on sbt 1.x and Scala 2.12.x

### Microsoft Azure Dependencies
This plugin uses artifacts from Microsoft:
* `"com.microsoft.azure" % "azure-tools-common" % "0.10.0"`
* `"com.microsoft.azure.functions" % "azure-functions-java-library" % "1.3.1" % "test"`

For now I will use these versions

### Testing
#### Unit tests
* `sbt clean test`
#### Scripted tests
* `sbt scripted`

Note: to successfully run the `deploy` scripted test, you need to have the Azure CLI installed and logged in to Azure with proper access
to a subscription that has a resource group and storage account as specified in the `sbt-test/sbt-azure-functions/deploy/build.sbt` file.

## Releasing (for plugin maintainers)
To release a new version, make sure you have:
* proper access to the `nl.codestar` namespace on Sonatype.
* GnuPG (`gpg`) installed and a signing key configured.
    * We use `sbt-pgp` plugin to sign, which relies on the `gpg` command line tool
* create a `.env` file in the project root with the following variables:
  ```
  PGP_KEYID=<id of the signing key>
  PGP_PASSPHRASE=<your PGP passphrase>
  SONATYPE_USER=<user id or token id>
  SONATYPE_PASSWORD=<password or token>

  ```

Note: The `.env` file needs to be kept out of the git repository (it is `.gitignore`d).

See [Using Sonatype](https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html) in the SBT documentation.

### SNAPSHOT versions
Steps to release SNAPSHOT version:
1. Make sure HEAD is not directly pointing to a tag
2. `sbt publishSigned`
3. make note of the SNAPSHOT version that is used (Sonatype does not allow searching/browsing for SNAPSHOT versions)

### Production versions
Steps to release production version:
1. Tag the current commit with the new version number, e.g. `git tag v0.5.0`
2. `sbt publishSigned`
3. `sbt sonaUpload`
4. Go to https://central.sonatype.com/publishing/deployments and publish the deployment.
    * or run `sbt sonaRelease` to publish the deployment automatically
