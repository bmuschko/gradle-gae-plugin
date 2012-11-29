/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gradle.api.plugins.gae.task.appcfg

import java.io.File;

import groovy.util.logging.Slf4j
import org.gradle.api.GradleException
import org.gradle.api.plugins.gae.task.Explodable;
import org.gradle.api.plugins.gae.task.GaeWebAppDirTask
import org.gradle.api.tasks.InputDirectory;
import org.gradle.api.tasks.TaskInputs;

/**
 * Abstract Google App Engine task used for application configuration.
 *
 * @author Benjamin Muschko
 */
@Slf4j
abstract class GaeAppConfigTaskTemplate extends GaeWebAppDirTask implements Explodable {
    String email
    String server
    String host
    Boolean passIn
    String password
    String httpProxy
    String httpsProxy
    Boolean changing
    File explodedWarDirectory

    @Override
    void executeTask() {
        // User has to enter credentials
        if(requiresUserInput()) {
            runAppConfig()
        }
        // All credentials were provided
        else {
            Thread appConfigThread = new Thread(new AppConfigRunnable())
            appConfigThread.start()
            appConfigThread.join()
        }
    }
    
    File getEffectiveWarDirectory(){
        getChanging() ? getExplodedWarDirectory() : getWebAppSourceDirectory()
    }

    private boolean requiresUserInput() {
        !(getEmail() && getPassword())
    }

    void runAppConfig() {
        try {
            log.info startLogMessage()

            def params = []
            addCommonParams(params)
            params.addAll getParams()
            log.info "Using params = $params"

            ClassLoader classLoader = Thread.currentThread().contextClassLoader
            def appCfg = classLoader.loadClass('com.google.appengine.tools.admin.AppCfg')
            appCfg.main(params as String[])
        }
        catch(Exception e) {
            throw new GradleException(errorLogMessage(), e)
        }
        finally {
            log.info finishLogMessage()
        }
    }

    private void addCommonParams(params) {
        if(getEmail()) {
            params << "--email=${getEmail()}"
        }

        if(getServer()) {
            params << "--server=${getServer()}"
        }

        if(getHost()) {
            params << "--host=${getHost()}"
        }

        if(getPassIn() || getPassword()) {
            params << '--passin'
        }

        if(getHttpProxy()) {
            params << "--proxy=${getHttpProxy()}"
        }

        if(getHttpsProxy()) {
            params << "--proxy_https=${getHttpsProxy()}"
        }
    }

    private class AppConfigRunnable implements Runnable {
        @Override
        void run() {
            PrintStream systemOutOriginal = System.out
            InputStream systemInOriginal = System.in
            PipedInputStream inputStreamReplacement = new PipedInputStream()
            OutputStream stdin = new PipedOutputStream(inputStreamReplacement)

            try {
                System.setIn(inputStreamReplacement)
                BufferedWriter stdinWriter = new BufferedWriter(new OutputStreamWriter(stdin))
                PrintStream printStream = new PrintStream(new PasswordOutputStream(stdinWriter, systemOutOriginal), true)
                System.setOut(printStream)

                GaeAppConfigTaskTemplate.this.runAppConfig()
            }
            finally {
                System.setOut(systemOutOriginal)
                System.setIn(systemInOriginal)
            }
        }
    }

    private class PasswordWriterRunnable implements Runnable {
        final BufferedWriter stdinWriter

        public PasswordWriterRunnable(BufferedWriter stdinWriter) {
            this.stdinWriter = stdinWriter
        }

        @Override
        void run() {
            stdinWriter.write(GaeAppConfigTaskTemplate.this.password)
            stdinWriter.newLine()
            stdinWriter.flush()
        }
    }

    private class PasswordOutputStream extends OutputStream {
        final BufferedWriter stdinWriter
        final PrintStream out

        public PasswordOutputStream(BufferedWriter stdinWriter, PrintStream out) {
            this.stdinWriter = stdinWriter
            this.out = out
        }

        @Override
        void write(int b) throws IOException {
            Thread passwordWriterThread = new Thread(new PasswordWriterRunnable(stdinWriter))
            passwordWriterThread.setDaemon(true)
            passwordWriterThread.start()
            out.write(b)
        }
    }

    abstract String startLogMessage()
    abstract String errorLogMessage()
    abstract String finishLogMessage()
    abstract List getParams()
    
    @Override
    public TaskInputs getInputs() {
        TaskInputs inputs = super.inputs
        if(changing){
            inputs.dir(getExplodedWarDirectory())
        }
        inputs
    }
}
