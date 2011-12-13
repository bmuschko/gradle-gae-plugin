# Gradle GAE plugin

![Google App Engine Logo](http://code.google.com/appengine/images/appengine_lowres.png)

The plugin provides tasks for uploading, downloading, running and managing [Google App Engine](http://code.google.com/appengine/)
(GAE) projects in any given Gradle build. It extends the War plugin.

## Usage

To use the GAE plugin, include in your build script:

    apply plugin: 'gae'

The plugin JAR needs to be defined in the classpath of your build script. You can either get the plugin from the GitHub
download section or upload it to your local repository. The following code snippet shows an example:

    buildscript {
        repositories {
            add(new org.apache.ivy.plugins.resolver.URLResolver()) {
                name = 'GitHub'
                addArtifactPattern 'http://cloud.github.com/downloads/[organisation]/[module]/[module]-[revision].[ext]'
            }
        }

        dependencies {
            classpath 'bmuschko:gradle-gae-plugin:0.5.2'
        }
    }

*Note:* The plugin requires you to set the environment variable _APPENGINE_HOME_ or the system property _google.appengine.sdk_
pointing to your current Google App Engine SDK installation. In case you have both variables set the system property takes
precedence over the environment variable. Alternatively, you can choose to automatically download the SDK by setting the
convention property `downloadSdk` to `true`. This option requires you to specify the SDK version you want to use by setting
the configuration `gaeSdk`.

    dependencies {
        gaeSdk 'com.google.appengine:appengine-java-sdk:1.5.5'
    }

## Tasks

The GAE plugin defines the following tasks:

* `gaeCronInfo`: Verifies and prints the scheduled task (cron) configuration.
* `gaeDownloadSdk`: Downloads and sets Google App Engine SDK.
* `gaeEnhance`: Enhances DataNucleus classes by using byte-code manipulation to make your normal Java classes "persistable".
* `gaeExplodeWar`: Extends the `war` task to generate WAR file and explodes the artifact into `build/exploded-war`.
* `gaeLogs`: Retrieves log data for the application running on App Engine.
* `gaeRollback`: Undoes a partially completed update for the given application.
* `gaeRun`: Starts a local development server running your project code. By default the WAR file is created, exploded and used as
web application directory each time you run this task. This behavior can be changed by setting the convention property
`warDir`.
* `gaeStop`: Stops the local development server.
* `gaeUpdateCron`: Updates the schedule task (cron) configuration for the app, based on the cron.xml file.
* `gaeUpdateDos`: Updates the DoS protection configuration for the app, based on the dos.xml file.
* `gaeUpdateIndexes`: Updates datastore indexes in App Engine to include newly added indexes.
* `gaeUpdateQueues`: Updates the task queue configuration (queue.xml) in App Engine.
* `gaeUpload`: Uploads files for an application given the application's root directory. The application ID and version are taken from the appengine-web.xml file.
* `gaeVacuumIndexes`: Deletes unused indexes in App Engine server.
* `gaeVersion`: Prints detailed version information about the SDK, Java and the operating system.
* `gaeEclipseGenerateSettings`: Generates files to integrate with the Eclipse Google Plugin

## Project layout

The GAE plugin uses the same layout as the War plugin.
*Note:* If the Gradle Eclipse plugin is configured, the webAppDirName/WEB-INF/lib directory will also need to exist

## Convention properties

The GAE plugin defines the following convention properties in the `gae` closure:

* `httpPort`: The TCP port which local development server should listen for HTTP requests on (defaults to 8080).
* `stopPort`: The TCP port which local development server should listen for admin requests on (defaults to 8081).
* `stopKey`: The key to pass to local development server when requesting it to stop (defaults to null).
* `daemon`: Specifies whether the local development server should run in the background. When true, this task completes as
soon as the server has started. When false, this task blocks until the local development server is stopped (defaults to false).
* `warDir`: Web application directory used for local development server (defaults to `build/exploded-war`).
* `disableUpdateCheck`: Disables the Google App Engine update check if set to true.
* `debug`: Enables remote JVM debugging if set to true.
* `debugPort`: The TCP port the JVM listens on for remote debug connections (defaults to 8000).
* `downloadSdk`: Downloads the Google App Engine SDK defined by the configuration name `gaeSdk` and explodes the artifact into
`build/exploded-gae-sdk` (defaults to false). If set to `true` the given SDK is used for running all plugin tasks which
takes precedence over _APPENGINE_HOME_ and the system property _google.appengine.sdk_.

Within `gae` you can define optional properties in a closure named `appcfg`:

* `email`: The email address of the Google account of an administrator for the application, for actions that require signing in.
If omitted and no cookie is stored from a previous use of the command, the command will prompt for this value.
* `server`: The App Engine server hostname (defaults to appengine.google.com).
* `host`: The hostname of the local machine for use with remote procedure calls.
* `passIn`: Do not store the administrator sign-in credentials as a cookie; prompt for a password every time. If the property
`password` was provided then this value will always be true.
* `password`: The password in plain text to be used whenever a task requires one. The password is only applied if the `email`
convention property was provided also. Alternatively, you can set the password in your `gradle.properties` via the property
`gaePassword`. The password in `gradle.properties` takes precedence over the one set in this convention property.
* `httpProxy`: Use the given HTTP proxy to contact App Engine.
* `httpsProxy`: Use the given HTTPS proxy to contact App Engine, when using HTTPS. If `httpProxy` is given but `httpsProxy`
is not, both HTTP and HTTPS requests will use the given proxy.

The task `gaeLogs` requires you to at least define the file to write the logs to. Define the tasks' properties in the
closure `logs`:

* `numDays`: The number of days of log data to retrieve, ending on the current date at midnight UTC. A value of 0 retrieves
all available logs. If `append` is given, then the default is 0, otherwise the default is 1.
* `severity`: The minimum log level for the log messages to retrieve. The value is a number corresponding to the log
level: 4 for CRITICAL, 3 for ERROR, 2 for WARNING, 1 for INFO, 0 for DEBUG. All messages at the given log level and above
will be retrieved (defaults to 1 (INFO)).
* `append`: Tells the plugin to append logs to the log output file instead of overwriting the file. This simply appends the
requested data, it does not guarantee the file won't contain duplicate error messages. If this argument is not specified,
the plugin will overwrite the log output file.
* `outputFile`: The file the logs get written to.

### Integration with Gradle Eclipse Plugin / Google Plugin

If this plugin is enabled on the same project that the eclipse plugin is, then executing 'gradle eclipse' will set up the project
so that it will integrate nicely with the Google plugin for eclipse.  There are a few caveats due to this.

* You will need to have a webAppDirName/WEB-INF/lib directory
* Because the Google plugin expects the JARs to be in WEB-INF/lib, the 'gradle eclipse' task will copy the required files there
* The Eclipse build output will also be in webAppDirName/WEB-INF/classes
* Due to above, you likely should exclude those directory from version control

### Example

    gae {
        httpPort = 8085

        appcfg {
            email = 'benjamin.muschko@gmail.com'
            passIn = true

            logs {
                severity = 1
                outputFile = file('mylogs.txt')
            }
        }
    }

## FAQ

**Can I use the plugin with a [Gaelyk](http://gaelyk.appspot.com/) project?**

Yes, you just have to configure the WAR plugin to point to the correct web application (by default `war`) and source code
(by default `src`) directory. If you want to stick to the default source directory simply create the subdirectory `src/main/groovy`.

    apply plugin: 'groovy'

    sourceSets {
        main {
            groovy {
                srcDirs = ['src']
            }
        }
    }

    webAppDirName = file('war')

When editing a Groovlets/Groovy templates in Gaelyk the server automatically deploys the change and you see it take effect almost instantly.
The plugin provides support for that. Simply set the `warDir` convention property and leave the server running.

    gae {
        warDir = file('war')
    }