package org.gradle.api.plugins.gae

import org.gradle.api.Project
import org.gradle.api.plugins.gae.task.GaeExplodeWarTask;
import org.gradle.testfixtures.ProjectBuilder

import spock.lang.Specification

class GaePluginTest extends Specification{
    
    def "Test exploded war property is set to plugins automagically"(){
        Project project = ProjectBuilder.builder().build()
        project.apply plugin: 'gae'

        def tasks = project.tasks.matching { it.name in ['gaeExplodeWar', 'gaeRun', 'gaeUpload', 'gaeUpdateBackends', 'gaeUpdateAllBackends' ] }
        expect:
        tasks.every { it.explodedWarDirectory }
    }

}
