package com.sdg.eclipse;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
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

	private static final String CONTEXT_SEKELOTON_XML = "context-sekeloton.xml";
	private static final String CONTROLLER_JAVA_SKELETON = "controller.java.skeleton";
	private static final String LIFERAY_PLUGIN_PACKAGE_PROPERTIES = "docroot/WEB-INF/liferay-plugin-package.properties";
	private static final String RESOURCES_FOLDER = "com/sdg/eclipse/resources/";
	private static final String[] DEPENDENCIES = new String[] {
			"spring-aop.jar", "spring-asm.jar", "spring-aspects.jar",
			"spring-beans.jar", "spring-context-support.jar",
			"spring-context.jar", "spring-core.jar", "spring-expression.jar",
			"spring-jdbc.jar", "spring-jms.jar", "spring-orm.jar",
			"spring-oxm.jar", "spring-transaction.jar",
			"spring-web-portlet.jar", "spring-web-servlet.jar",
			"spring-web-struts.jar", "spring-web.jar", "commons-beanutils.jar",
			"commons-collections.jar", "commons-fileupload.jar",
			"commons-io.jar", "commons-lang.jar", "jstl-api.jar",
			"jstl-impl.jar", "alloy-taglib.jar", "liferay-icu4j.jar",
			"liferay-yui-compressor.jar" };

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
		configReplaceMap.put("MyFirstSpringMVCTestController",
				pageOne.getClassName());

		createFile(getSourceFolder(), getJavaName(),getResource(CONTROLLER_JAVA_SKELETON), sourceReplaceMap);
		createFile(getJSPFolder(), getJSPName(), getResource(getJSPName()),	jspReplaceMap);
		createFile(getConfigFolder(),getPortletXMLName(),getResource(CONTEXT_SEKELOTON_XML), configReplaceMap);
		updateLiferayProperties();

		return true;
	}

	private String getPortletXMLName() {
		return pageOne.getPortletName() + "-portlet.xml";
	}

	private String getJSPName() {
		return "view.jsp";
	}

	private String getJavaName() {
		return pageOne.getClassName() + ".java";
	}

	private void updateLiferayProperties() {
		Properties properties = new Properties();
		InputStream propertiesInputStream = null;
		ByteArrayOutputStream propertiesOutputStream = null;
		try {
			propertiesInputStream = pageOne.getChosenProject()
					.getFile(LIFERAY_PLUGIN_PACKAGE_PROPERTIES).getContents();
			properties.load(propertiesInputStream);
			String portalDependencies = (String) properties
					.get("portal-dependency-jars");
			Set<String> portalDependenciesSet = new HashSet<String>(
					Arrays.asList(portalDependencies.split(",")));
			portalDependenciesSet.addAll(Arrays.asList(DEPENDENCIES));			
			portalDependencies = commaSeparated(portalDependenciesSet);
			properties.setProperty("portal-dependency-jars",portalDependencies);			
			propertiesOutputStream = new ByteArrayOutputStream();
			properties.store(propertiesOutputStream,null);
			pageOne.getChosenProject().getFile(LIFERAY_PLUGIN_PACKAGE_PROPERTIES).setContents(new ByteArrayInputStream(propertiesOutputStream.toByteArray()), IFile.FORCE, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				propertiesInputStream.close();
				propertiesOutputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private String commaSeparated(Set<String> portalDependenciesSet) {
		String result = "";
		for (String string : portalDependenciesSet) {
			result += string +",";
		}
		return result;
	}

	public String getString(InputStream is) {
		Scanner scanner = null;
		try {
			scanner = new Scanner(is).useDelimiter("\\A");
			return scanner.hasNext() ? scanner.next() : "";
		}finally {
			scanner.close();
		}
		

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

	private IFile createFile(String folderStr, String fileName,
			String resource, Map<String, String> replaceMap) {
		IFolder folder = ResourcesPlugin.getWorkspace().getRoot()
				.getFolder(new Path(folderStr));
		IFile classFile = folder.getFile(fileName);
		InputStream inputStream = null;
		try {
			inputStream = getClass().getClassLoader().getResourceAsStream(
					resource);
			String str = replace(getString(inputStream), replaceMap);
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
