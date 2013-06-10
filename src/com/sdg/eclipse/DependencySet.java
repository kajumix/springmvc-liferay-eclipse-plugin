package com.sdg.eclipse;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;

public class DependencySet {

	private Collection<String> names = new ArrayList<String>();
	private Set<String> set = new HashSet<String>();
	private IProject project;

	public DependencySet(String jarDependencies, IProject project) {
		set.addAll(Arrays.asList(jarDependencies.split(",")));
		this.project = project;
		for (String name : set) {
			names.add(new File(name).getName());
		}
	}

	public void addJars() {
		IFolder folder = project.getFolder("/docroot/WEB-INF/lib");
		for (String jar : set) {
			File file = new File(jar);
			IFile dest = folder.getFile(file.getName());
			FileInputStream source = null;
			try {
				source = new FileInputStream(file);
				dest.create(source, true, null);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					source.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

	}

	public Collection<? extends String> getNames() {
		return names;
	}



}
