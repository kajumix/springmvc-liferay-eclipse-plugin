package com.sdg.eclipse;

import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

public class LiferaySpringPortletWizard extends Wizard implements INewWizard {

	private static final List<String> names = Arrays.asList(new String[] {
			"commons-fileupload.jar", "commons-io.jar", "spring-jdbc.jar",
			"spring-aop.jar", "liferay-icu4j.jar", "spring-web-servlet.jar",
			"spring-oxm.jar", "spring-core.jar", "commons-lang.jar",
			"jstl-api.jar", "spring-expression.jar",
			"spring-context-support.jar", "spring-web-struts.jar",
			"liferay-yui-compressor.jar", "jstl-impl.jar",
			"spring-transaction.jar", "spring-beans.jar", "alloy-taglib.jar",
			"spring-context.jar", "spring-asm.jar", "spring-orm.jar",
			"commons-beanutils.jar", "spring-aspects.jar",
			"spring-web-portlet.jar", "commons-collections.jar",
			"spring-web.jar", "spring-jms.jar" });
	private LiferaySpringWizardPageOne pageOne;
	private LiferaySpringWizardPageTwo pageTwo;

	ISelection selection;
	IWorkbench workbench;

	private String className;
	private String sourceFolder;
	private String pkg;
	private String portletName;
	private String viewName;
	private IProject project;
	private String category;
	private String portletDisplayName;
	private String portletTitle;
	private boolean instanceable;

	public LiferaySpringPortletWizard() {
		super();
	}

	@Override
	public boolean performFinish() {
		try {
			gatherData();

			new ControllerClass(className, sourceFolder, pkg, viewName)
					.create();

			new JspView(viewName, project).create();

			new SpringPortletContext(portletName, className, project, pkg)
					.create();

			DependencySet dependencySet = new DependencySet(names);

			new LiferayProperties(project, dependencySet).update();

			new LiferayDisplayXML(project, category, portletName).update();

			new WebXML(project).addSpringServlets();

			new PortletXML(project, portletName, portletDisplayName,
					portletTitle).update();

			new LiferayPortletXML(project, portletName, instanceable).update();
		} catch (Throwable e) {
			ErrorDialog.openError(workbench.getActiveWorkbenchWindow()
					.getShell(), "Error", e.getMessage(),
					new Status(Status.ERROR, "org.sdg.liferay.springportlet",
							1, e.getMessage(), e.getCause()));
			e.printStackTrace();
		}

		return true;
	}

	private void gatherData() {
		className = pageOne.getClassName();
		sourceFolder = pageOne.getSourceFolder();
		pkg = pageOne.getPackage();
		portletName = pageOne.getPortletName();
		viewName = portletName;
		project = pageOne.getSelectedProject();
		pageOne.getJarDependencies();
		category = pageOne.getCategory();
		portletDisplayName = pageTwo.getportletDisplayName();
		portletTitle = pageTwo.getPortletTitle();
		instanceable = pageOne.isInstanceable();
	}

	@Override
	public void addPages() {
		pageOne = new LiferaySpringWizardPageOne("pageOne");
		pageTwo = new LiferaySpringWizardPageTwo("pageTwo");
		addPage(pageOne);
		addPage(pageTwo);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
		this.workbench = workbench;
	}

}
