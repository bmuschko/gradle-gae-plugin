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

import org.gradle.api.plugins.gae.task.Explodable
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.TaskInputs;

/**
 * Google App Engine task uploading your application to the server.
 *
 * @see <a href="http://code.google.com/appengine/docs/java/tools/uploadinganapp.html#Uploading_the_App">Documentation</a>
 * @author Benjamin Muschko
 */
class GaeUploadTask extends GaeAppConfigTaskTemplate {
    static final String COMMAND = 'update'

    @Override
    String startLogMessage() {
        'Starting upload process...'
    }

    @Override
    String errorLogMessage() {
        'An error occurred uploading the application to App Engine.'
    }

    @Override
    String finishLogMessage() {
        'Finished uploading process.'
    }

    @Override
    List getParams() {
        ['--enable_jar_splitting', COMMAND, getExplodedWarDirectory().canonicalPath]
    }
}
