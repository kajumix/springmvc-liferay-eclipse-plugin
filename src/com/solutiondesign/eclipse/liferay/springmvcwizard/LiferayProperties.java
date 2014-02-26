package com.solutiondesign.eclipse.liferay.springmvcwizard;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;



public class LiferayProperties {
	
	private IProject project;
	private DependencySet dependencies;
	
	public LiferayProperties(IProject project, DependencySet dependencies) {
		this.project = project;
		this.dependencies = dependencies;
	}

	private static final String LIFERAY_PLUGIN_PACKAGE_PROPERTIES = "docroot/WEB-INF/liferay-plugin-package.properties";

	public void update() {
		Properties properties = new Properties();
		InputStream propertiesInputStream = null;
		ByteArrayOutputStream propertiesOutputStream = null;
		try {
			propertiesInputStream = project.getFile(
					LIFERAY_PLUGIN_PACKAGE_PROPERTIES).getContents();
			properties.load(propertiesInputStream);
			String portalDependencies = (String) properties
					.get("portal-dependency-jars");
			Set<String> existingDependenciesSet = new HashSet<String>();
			if (portalDependencies!=null) {
				existingDependenciesSet.addAll(Arrays.asList(portalDependencies.split(",")));
			} 
			existingDependenciesSet.addAll(dependencies.getNames());
			portalDependencies = commaSeparated(existingDependenciesSet);
			properties
					.setProperty("portal-dependency-jars", portalDependencies);
			propertiesOutputStream = new ByteArrayOutputStream();
			properties.store(propertiesOutputStream, null);
			project.getFile(LIFERAY_PLUGIN_PACKAGE_PROPERTIES)
					.setContents(
							new ByteArrayInputStream(
									propertiesOutputStream.toByteArray()),
							IFile.FORCE, null);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (CoreException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				propertiesInputStream.close();
				propertiesOutputStream.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	private String commaSeparated(Set<String> portalDependenciesSet) {
		String result = "";
		for (String string : portalDependenciesSet) {
			result += string + ",";
		}
		return result;
	}
}
