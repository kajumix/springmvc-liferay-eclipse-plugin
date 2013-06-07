package com.sdg.eclipse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

public class LiferaySpringPortletWizard extends Wizard implements INewWizard {

	private static final String RESOURCES_FOLDER = "com/sdg/eclipse/resources/";

	LiferaySpringWizardPageOne pageOne;

	ISelection selection;
	IWorkbench workbench;

	public LiferaySpringPortletWizard() {
		super();
	}

	@Override
	public boolean performFinish() {
		Map<String, String> sourceReplaceMap = new HashMap<String, String>();
		Map<String, String> jspReplaceMap = new HashMap<String, String>();
		Map<String, String> configReplaceMap = new HashMap<String, String>();
		
		sourceReplaceMap.put("com.mycomapany", pageOne.getPackage());
		sourceReplaceMap.put("ViewController", pageOne.getClassName());
		configReplaceMap.put("com.myowncompany", pageOne.getPackage());
		configReplaceMap.put("MyFirstSpringMVCTestController", pageOne.getClassName());
		
		createFile(getSourceFolder(), pageOne.getClassName()	+ ".java", getResource("controller.java.skeleton"), sourceReplaceMap);
		createFile(getJSPFolder(), "view.jsp", getResource("view.jsp"), jspReplaceMap);
		createFile(getConfigFolder(),pageOne.getPortletName() + "-portlet.xml", getResource("context-sekeloton.xml"),configReplaceMap);
		return true;
	}

	public String getString(InputStream is) {

		Scanner scanner = new Scanner(is).useDelimiter("\\A");
		return scanner.hasNext() ? scanner.next() : "";

	}

	private String getConfigFolder() {
		return "/" + pageOne.getChosenProject().getName() + "/docroot/WEB-INF";
	}

	private String getResource(String resourceName) {
		return RESOURCES_FOLDER + resourceName;
	}

	private String getJSPFolder() {
		String folder = "/" + pageOne.getChosenProject().getName()
				+ "/docroot/WEB-INF/jsp";
		createIFNeeded(folder);
		return folder;
	}

	private void createIFNeeded(String folder) {
		// TODO Auto-generated method stub

	}

	private IFile createFile(String folderStr, String fileName, String resource, Map<String, String> replaceMap) {
		IFolder folder = ResourcesPlugin.getWorkspace().getRoot()
				.getFolder(new Path(folderStr));
		IFile classFile = folder.getFile(fileName);
		InputStream inputStream = null;
		try {
			inputStream = getClass().getClassLoader().getResourceAsStream(
					resource);			 
			String str = replace(getString(inputStream),replaceMap);
			classFile.create(new ByteArrayInputStream(str.getBytes()), false,
					null);
		} catch (CoreException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return classFile;
	}

	private String replace(String string, Map<String, String> replaceMap) {
		Set<String> keySet = replaceMap.keySet();
		for (String regex : keySet) {
			string = string.replaceAll(regex, replaceMap.get(regex));
		}
		return string;
	}

	private String getSourceFolder() {
		String folder = pageOne.getSourceFolder() + "/"
				+ pageOne.getPackage().replaceAll("\\.", "/");
		createIFNeeded(folder);
		return folder;
	}

	@Override
	public void addPages() {
		pageOne = new LiferaySpringWizardPageOne("pageOne");
		addPage(pageOne);

	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
		this.workbench = workbench;
	}

}
