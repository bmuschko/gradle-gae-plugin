package org.gradle.api.plugins.gae.task

import java.io.File;

import org.gradle.api.tasks.InputDirectory;

class GaeEclipseSettingsTask extends AbstractGaeTask {
	@InputDirectory File explodedSdkDirectory
	def prefixesForAppEnginePrefs = ['appengine-api-1.0-sdk', 'jsr107cache', 'datanucleus-core', 'geronimo-jpa_3.0_spec', 'datanucleus-appengine', 'geronimo-jta_1.1_spec', 'jdo2-api', 'datanucleus-jpa', 'appengine-api-labs']
	
	@Override
	void executeTask() {
		generateGdtPreferences()
		generateAppenginePrefrences()
	}
	
	private void generateGdtPreferences() {
		def gdtPrefsFile = new File(".settings/com.google.gdt.eclipse.core.prefs")
		if (gdtPrefsFile.exists()) gdtPrefsFile.delete()
		gdtPrefsFile << 'eclipse.preferences.version=1' + System.getProperty("line.separator")
		gdtPrefsFile << 'jarsExcludedFromWebInfLib=' + System.getProperty("line.separator")
		gdtPrefsFile << "warSrcDir=${project.webAppDirName}" + System.getProperty("line.separator")
		gdtPrefsFile << 'warSrcDirIsOutput=true' + System.getProperty("line.separator")
	}
	
	private void generateAppenginePrefrences() {
		// AppEngine Prefs File
		def appEnginePrefsFile = new File(".settings/com.google.appengine.eclipse.core.prefs")
		if (appEnginePrefsFile.exists()) appEnginePrefsFile.delete()
		appEnginePrefsFile << 'eclipse.preferences.version=1' + System.getProperty("line.separator")
		appEnginePrefsFile << 'filesCopiedToWebInfLib='
		prefixesForAppEnginePrefs.each {
			appEnginePrefsFile << getJarFileName(it)
			appEnginePrefsFile << "|"
		}
	}
	
	protected String getJarFileName(String beginning) {
		def answer;
		
		getExplodedSdkDirectory().eachFileRecurse {
			if (it.name.startsWith(beginning))
				answer = it.name
		}
		
		return answer
	}
}
