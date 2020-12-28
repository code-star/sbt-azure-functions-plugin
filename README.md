# SBT Azure Functions Plugin

Experimental plugin for sbt to create Azure Function artefacts (function.json) needed to publish code as an Azure Function.

## Usage

TODO: add this

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
* Run `sbt publish`

