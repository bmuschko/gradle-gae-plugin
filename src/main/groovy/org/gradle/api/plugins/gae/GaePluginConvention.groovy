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
package org.gradle.api.plugins.gae

import org.gradle.api.plugins.gae.task.appcfg.GaeAppConfigConvention

/**
 * Defines App Engine plugin convention.
 *
 * @author Benjamin Muschko
 */
class GaePluginConvention {
    Integer httpPort = 8080
    Integer stopPort = 8081
    String stopKey
    Boolean daemon = false
    Boolean disableUpdateCheck = false
    Boolean debug = false
    Integer debugPort = 8000
    File warDir
    GaeAppConfigConvention appCfg = new GaeAppConfigConvention()

    def gae(Closure closure) {
        closure.delegate = this
        closure()
    }

    def appcfg(Closure closure) {
        closure.resolveStrategy = Closure.DELEGATE_FIRST
        closure.delegate = appCfg
        closure()
    }

    def logs(Closure closure) {
        closure.resolveStrategy = Closure.DELEGATE_FIRST
        closure.delegate = appCfg.logs
        closure()
    }
}
