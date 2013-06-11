package com.sdg.eclipse;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

public class LiferaySpringPortletWizard extends Wizard implements INewWizard {

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
	private String jars;
	private String category;
	private String portletDisplayName;
	private String portletTitle;


	public LiferaySpringPortletWizard() {
		super();
	}

	@Override
	public boolean performFinish() {
		gatherData();

		new ControllerClass(className, sourceFolder, pkg, viewName).create();
		
		new JspView(viewName, project).create();
		
		new SpringPortletContext(portletName, className, project, pkg).create();
		
		DependencySet dependencySet = new DependencySet(jars,project);
		
		dependencySet.addJars();
		
		new LiferayProperties(project, dependencySet).update();
		
		new LiferayDisplayXML(project, category, portletName).update();
		
		new WebXML(project).addSpringServlets();
		
		new PortletXML(project, portletName, portletDisplayName, portletTitle).update();
		
		
		return true;
	}

	private void gatherData() {
		className = pageOne.getClassName();
		sourceFolder = pageOne.getSourceFolder();
		pkg = pageOne.getPackage();
		portletName = pageOne.getPortletName();
		viewName = portletName;
		project = pageOne.getSelectedProject();
		jars = pageOne.getJarDependencies();
		category = pageOne.getCategory();
		portletDisplayName = pageTwo.getportletDisplayName();
		portletTitle = pageTwo.getPortletTitle();
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
