### Version 0.10 (September 6, 2013)
* Added support for `version` and `appid` to override any other version or appid specified in appengine xml
* Moved `version` and `appid` properties from `app` closure into `appcfg` closure
* Renamed `app` closure to `download`

### Version 0.9 (August 18, 2013)

* Fixed deprecation warnings.
* Upgrade to Gradle Nexus plugin 0.3 and Gradle FatJar plugin 0.2.
* Upgrade to Gradle Wrapper 1.7.

### Version 0.8.1 (August 4, 2013)

* Expose convention property for setting the host name of development server - [Issue 52](https://github.com/bmuschko/gradle-gae-plugin/issues/52).

### Version 0.8 (December 9, 2012)

* Expose convention property for setting Java 7 compatibility - [Issue 33](https://github.com/bmuschko/gradle-gae-plugin/issues/33).
* Expose convention properties for OAuth2 and no cookies flags - [Issue 35](http://github.com/bmuschko/gradle-gae-plugin/issues/35).
* Renamed tasks `gaeUpload`/`gaeUploadAll` to `gaeUpdate`/`gaeUpdateAll` - [Issue 36](http://github.com/bmuschko/gradle-gae-plugin/issues/36). _Note_: The old task names
  will not be available anymore.
* Using the Gradle logger instead of the Slf4J AST transformation.
* Upgrade to Gradle Wrapper 1.3.
* Using Gradle Nexus plugin to simplify upload code.

### Version 0.7.6 (July 15, 2012)

* Added main source set output to functional test classpath - [Issue 29](https://github.com/bmuschko/gradle-gae-plugin/issues/29).

### Version 0.7.5 (July 14, 2012)

* Fix bug where war explosing is skipped during gaeUpload - [Issue 28](https://github.com/bmuschko/gradle-gae-plugin/issues/28).

### Version 0.7.4 (July 11, 2012)

* Fix functionalTest source set dependencies for Eclipse projects - [Issue 27](https://github.com/bmuschko/gradle-gae-plugin/issues/27).

### Version 0.7.3 (July 1, 2012)

* Use onlyIf for skipping gaeExplodeWar if warDir is specified - [Issue 26](https://github.com/bmuschko/gradle-gae-plugin/issues/26).
* Upgrade to Gradle Wrapper 1.0.

### Version 0.7.2 (June 3, 2012)

* Storing App Engine SDKs in Gradle home directory so they can be shared among projects - [Issue 11](https://github.com/bmuschko/gradle-gae-plugin/issues/11).

### Version 0.7.1 (June 2, 2012)

* Validation of HTTP and stop port - [Issue 24](https://github.com/bmuschko/gradle-gae-plugin/issues/24).
* Updated documentation.

### Version 0.7 (May 27, 2012)

* Support for bundling generated classes and dependencies into one JAR - [Issue 16](https://github.com/bmuschko/gradle-gae-plugin/issues/16).
* Added task for running functional tests - [Issue 22](https://github.com/bmuschko/gradle-gae-plugin/issues/22).

### Version 0.6.3 (May 27, 2012)

* Provided task for downloading the application - [Issue 20](https://github.com/bmuschko/gradle-gae-plugin/issues/20).
* Fixed task for automatically downloading the GAE SDK. We may not add the `@OutputDirectory` annotation as it wouldn't
set the correct SDK at runtime if task is considered up-to-date.

### Version 0.6.2 (May 21, 2012)

* Provided task `gaeUploadAll` and using `warDir` convention property for some of the backends tasks - [Issue 18](https://github.com/bmuschko/gradle-gae-plugin/issues/18).

### Version 0.6.1 (April 13, 2012)

* Added convention property to set JVM flags - [Issue 14](https://github.com/bmuschko/gradle-gae-plugin/issues/14).
**Note: This new property replaces the previously existing properties `debug` and `debugPort`. For more information see
the FAQ section in the [README file](README.md).**
* Using `@Slf4J` AST transformation for logging.

### Version 0.6 (March 31, 2012)

* Added support for backend tasks - [Issue 13](https://github.com/bmuschko/gradle-gae-plugin/issues/13).

### Version 0.5.5 (March 10, 2012)

* Added `@OutputDirectory` annotation to task downloading GAE SDK - [Issue 12](https://github.com/bmuschko/gradle-gae-plugin/issues/12).

### Version 0.5.4 (January 24, 2012)

* Fixed evaluation order in multi-module projects - [Issue 10](https://github.com/bmuschko/gradle-gae-plugin/issues/10).
* Upgrade to Gradle Wrapper 1.0-milestone7.

### Version 0.5.3 (November 19, 2011)

* Upload task enables JAR splitting by default by using `--enable_jar_splitting` - [Issue 8](https://github.com/bmuschko/gradle-gae-plugin/issues/8).

### Version 0.5.2 (November 4, 2011)

* New version of SDK Zip not exploded if parent directory exists - [Issue 7](https://github.com/bmuschko/gradle-gae-plugin/issues/7).

### Version 0.5.1 (June 19, 2011)

* Ignore `IllegalStateException` when running `gradle -t` and `gaeSdk` configuration is not declared.

### Version 0.5 (June 5, 2011)

* Added convention property `disableUpdateCheck` for disabling the update check.
* Added convention property `debug` for enabling the JVM debugger.
* Added convention property `downloadSdk` for automatically downloading the App Engine SDK to used for running the plugin's tasks.
* Setting the App Engine tools SDK library in the buildscript's classpath is not required anymore. It will be resolved
automatically by using the JAR packages with the App Engine SDK.

### Version 0.4 (April 13, 2011)

* Only stopping the thread when calling `gaeStop` for local development server running in background; not JVM - [Issue 3](https://github.com/bmuschko/gradle-gae-plugin/issues/3).
* When passing in the password the `passIn` property will always be set - [Issue 4](https://github.com/bmuschko/gradle-gae-plugin/issues/4).

### Version 0.3 (April 12, 2011)

* Support for running local development server as daemon - [Issue 1](https://github.com/bmuschko/gradle-gae-plugin/issues#issue/1).
* Allow providing password via gradle.properties or convention property - [Issue 2](https://github.com/bmuschko/gradle-gae-plugin/issues#issue/2).

### Version 0.2 (March 18, 2011)

* Added new task `gaeExplodeWar`.
* Cut dependency to exploded WAR directory for `gaeVersion` task.
* Task `gaeRun` will use exploded WAR directory by default. This behavior can be overriden by the convention property
`warDir`.

### Version 0.1 (March 17, 2011)

* Initial release.