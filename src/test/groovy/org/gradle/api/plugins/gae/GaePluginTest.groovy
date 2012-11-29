package org.gradle.api.plugins.gae

import org.gradle.api.Project
import org.gradle.api.Task;
import org.gradle.api.plugins.gae.task.GaeExplodeWarTask;
import org.gradle.api.plugins.gae.task.GaeRunTask;
import org.gradle.api.plugins.gae.task.appcfg.GaeUploadTask;
import org.gradle.api.plugins.gae.task.appcfg.backends.GaeListBackendsTask;
import org.gradle.api.plugins.gae.task.appcfg.backends.GaeUpdateAllBackendsTask;
import org.gradle.testfixtures.ProjectBuilder

import spock.lang.Specification
import spock.lang.Unroll;

class GaePluginTest extends Specification{
    
    @Unroll
    def "Test exploded war property is set to #involved"(){
        Project project = ProjectBuilder.builder().build()
        project.apply plugin: 'gae'
        
        project.with {
            gae {
                warDir = file('war')
            }
        }
        
        expect:
        project.tasks.getByName(involved).explodedWarDirectory
        
        where:
        involved << ['gaeExplodeWar', 'gaeRun', 'gaeUpload', 'gaeUpdateBackend', 'gaeUpdateAllBackends' ]
    }
    
    @Unroll
    def "Test right dir picked for gaeRun"(){
        Project project = ProjectBuilder.builder().build()
        project.apply plugin: 'gae'
        
        project.with {
            gae {
                warDir = file('war')
            }
        }
        
        GaeRunTask task = project.tasks.getByName('gaeRun')
        
        expect:
        task.getWarDirForKickStart() == task.getWarDirectory()
    }
    
    @Unroll
    def "Changing appCfg works"(){
        Project project = ProjectBuilder.builder().build()
        project.apply plugin: 'gae'
        
        project.with {
            gae {
                appcfg {
                    changing = isChanging
                }
            }
        }

        project.evaluate()
        
        GaeExplodeWarTask explode = project.tasks.getByName('gaeExplodeWar')
        GaeListBackendsTask task  = project.tasks.getByName('gaeListBackends')
        
        
        expect:
        task.taskDependencies.getDependencies(explode).empty != depends
        
        where:
        isChanging | depends
        true       | true
        false      | false
    }

}
