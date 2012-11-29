package org.gradle.api.plugins.gae

import org.gradle.api.Project
import org.gradle.api.plugins.gae.task.GaeExplodeWarTask;
import org.gradle.testfixtures.ProjectBuilder

import spock.lang.Specification
import spock.lang.Unroll;

class GaePluginTest extends Specification{
    
    @Unroll
    def "Test exploded war property is set to #involved"(){
        Project project = ProjectBuilder.builder().build()
        project.apply plugin: 'gae'
        
        expect:
        project.tasks.getByName(involved).explodedWarDirectory
        
        where:
        involved << ['gaeExplodeWar', 'gaeRun', 'gaeUpload', 'gaeUpdateBackend', 'gaeUpdateAllBackends' ]
    }

}
