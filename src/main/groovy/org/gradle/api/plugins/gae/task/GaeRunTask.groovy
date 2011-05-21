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
package org.gradle.api.plugins.gae.task

import com.google.appengine.tools.KickStart
import org.gradle.api.GradleException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.gradle.api.plugins.gae.task.internal.*

/**
 * Google App Engine task starting up a local development server.
 *
 * @see <a href="http://code.google.com/appengine/docs/java/tools/devserver.html#Running_the_Development_Web_Server">Documentation</a>
 * @author Benjamin Muschko
 */
class GaeRunTask extends AbstractGaeTask implements Explodable {
    static final Logger logger = LoggerFactory.getLogger(GaeRunTask.class)
    private Integer httpPort
    private Integer stopPort
    private String stopKey
    private File explodedWarDirectory
    private Boolean daemon
    private Boolean disableUpdateCheck
    private Boolean debug
    private Integer debugPort
    private final KickStartSynchronizer kickStartSynchronizer = new KickStartSynchronizer()

    @Override
    void executeTask() {
        startLocalDevelopmentServer()
    }

    private void startLocalDevelopmentServer() {
        try {
            logger.info "Starting local development server..."

            if(!getDaemon()) {
                startShutdownMonitor(new SystemExitShutdownCallback())
                runKickStart()
            }
            else {
                Thread kickStartThread = new Thread(new KickStartRunnable())
                startShutdownMonitor(new ThreadShutdownCallback(kickStartThread))
                kickStartThread.start()

                // Pause current thread until local development server is fully started
                kickStartSynchronizer.getGate().await()
            }
        }
        catch(Exception e) {
            throw new GradleException("An error occurred starting the local development server.", e)
        }
        finally {
            if(!getDaemon()) {
                logger.info "Local development server exiting."
            }
        }
    }

    private void startShutdownMonitor(ShutdownCallback shutdownCallback) {
        Thread shutdownMonitor = new ShutdownMonitor(getStopPort(), getStopKey(), shutdownCallback)
        shutdownMonitor.start()
    }

    private void runKickStart() {
        List params =
            ["com.google.appengine.tools.development.DevAppServerMain",
             "--port=" + getHttpPort(),
             getExplodedWarDirectory().getCanonicalPath()];
	   if (getDisableUpdateCheck()) {
            params.add(1,"--disable_update_check");
        }
        if (getDebug()) {
		 params.add(0, "--jvm_flag=-Xdebug");
		 params.add(1, "--jvm_flag=-Xrunjdwp:transport=dt_socket,server=y,suspend=n,address="+getDebugPort());
        }
        logger.info "Using params = $params"
        KickStart.main(params.toArray([] as String[]))
    }

    private class KickStartRunnable implements Runnable {
        final Logger logger = LoggerFactory.getLogger(KickStartRunnable.class)

        @Override
        void run() {
            InputStream systemInOriginal = System.in
            PrintStream systemOutOriginal = System.out
            PrintStream systemErrOriginal = System.err
            PipedOutputStream pipeOut = new PipedOutputStream()
            PipedInputStream pipeIn = new PipedInputStream(pipeOut)
            PrintStream out = new PrintStream(pipeOut, true)
            System.setIn(pipeIn)
            System.setOut(out)
            System.setErr(out)

            Thread outputCommandLineConsumer = new CommandLineStreamConsumer(pipeIn, new OutStreamHandler(), GaeRunTask.this.getKickStartSynchronizer())
            outputCommandLineConsumer.start()

            try {
                runKickStart()
            }
            catch(Exception e) {
                throw new GradleException("An error occurred starting the local development server.", e)
            }
            finally {
                System.setIn(systemInOriginal)
                System.setOut(systemOutOriginal)
                System.setErr(systemErrOriginal)
            }
        }

        private class OutStreamHandler implements StreamOutputHandler {
            @Override
            void handleLine(String line) {
                logger.info line
                checkServerStartupProgress(line)
            }
        }

        private class ErrStreamHandler implements StreamOutputHandler {
            @Override
            void handleLine(String line) {
                logger.error line
                checkServerStartupProgress(line)
            }
        }

        private void checkServerStartupProgress(final String line) {
            if(line.contains("The server is running")) {
                GaeRunTask.this.getKickStartSynchronizer().getGate().countDown()
            }
        }
    }

    public Integer getHttpPort() {
        httpPort
    }

    public void setHttpPort(Integer httpPort) {
        this.httpPort = httpPort
    }

    public Integer getStopPort() {
        stopPort
    }

    public void setStopPort(Integer stopPort) {
        this.stopPort = stopPort
    }

    public String getStopKey() {
        stopKey
    }

    public void setStopKey(String stopKey) {
        this.stopKey = stopKey
    }

    @Override
    public File getExplodedWarDirectory() {
        explodedWarDirectory
    }

    @Override
    public void setExplodedWarDirectory(File explodedWarDirectory) {
        this.explodedWarDirectory = explodedWarDirectory
    }

    public Boolean getDaemon() {
        daemon
    }

    public void setDaemon(Boolean daemon) {
        this.daemon = daemon
    }

    public Boolean getDisableUpdateCheck() {
        disableUpdateCheck
    }

    public void setDisableUpdateCheck(Boolean disableUpdateCheck) {
        this.disableUpdateCheck = disableUpdateCheck
    }

    public Boolean getDebug() {
        debug
    }

    public void setDebug(Boolean debug) {
        this.debug = debug
    }

    public Integer getDebugPort() {
        debugPort
    }

    public void setDebugPort(Integer debugPort) {
        this.debugPort= debugPort
    }

    public KickStartSynchronizer getKickStartSynchronizer() {
        kickStartSynchronizer
    }
}

