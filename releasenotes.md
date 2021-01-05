# Release notes

## Version 0.3.1
Identical to 0.3.0, but releasing is now done to the `code-star/sbt-plugins` repository on Bintray
(was `code-star/sbt-azure-functions`)

## Version 0.3.0
Fixes and other changes:
* Removed split setup (library was not included in plugin, so to use it, one needed to add separate dependency)

## Version 0.2.3 .. 0.2.5
Identical to 0.2.2, releases created to validate/fix project setup.

## Version 0.2.2
New Features:
* Dependency between assembly task and azfunCreateZipFile is now part of plugin definition
  
    No more need to call `sbt assembly` before `sbt azfunCreateZipFile`

Fixes and other changes:
* Restructure project to be in line with the `sbt/sbt-autoplugin.g8` template
* Move to multi-project setup (separate plugin code from library)


## Version 0.2.1
New features:
* Settings and Tasks now have proper types where relevant (instead of `String` and `Unit`)

Fixes and other changes:
* update Readme - releasing is done with `sbt release`, not `sbt publish`
* Several documentation updates to better describe usage
* Added LICENSE file
* setup CI through github actions

## Version 0.2.0
New features:
* Creates zip file that can be uploaded to Azure (`sbt azfunCreateZipFile`)

Fixes and other changes:
* rename all tasks and settings to use `azfun` prefix (prevent possible name clashes with other plugins)
* Change versioning to x.y.z (instead of x.y)

## Version 0.1
Experimental version, incomplete. Used to setup release process and first steps to produce `function.json` output.
