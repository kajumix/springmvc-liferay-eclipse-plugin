package com.sdg.eclipse;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class LiferaySpringWizardPageTwo extends WizardPage {
	private static LiferaySpringWizardPage page = new LiferaySpringWizardPage();
	private Text portletDisplayNameText;
	private Text portletTitleText;
	

	protected LiferaySpringWizardPageTwo(String pageName) {
		super(pageName);
		setTitle("Portlet attributes");
	}

	@SuppressWarnings("restriction")
	@Override
	public void createControl(Composite parent) {
		Composite container = page.createContainer(parent, 3, 9);
		createPortletDisplayName(container);
		createPortletTitle(container);
		setControl(container);

	}

	private void createPortletTitle(Composite container) {
		page.makeLabel(container, "Portlet Title");
		portletTitleText = new Text(container, SWT.BORDER | SWT.SINGLE);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		portletTitleText.setLayoutData(gridData);
		page.makeLabel(container, "");
		
	}

	private void createPortletDisplayName(Composite container) {
		page.makeLabel(container, "Portlet Display Name");
		portletDisplayNameText = new Text(container, SWT.BORDER | SWT.SINGLE);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		portletDisplayNameText.setLayoutData(gridData);
		page.makeLabel(container, "");
	}

	public String getportletDisplayName(){
		return portletDisplayNameText.getText();
	}
	
	public String getPortletTitle() {
		return portletTitleText.getText();
	}
}
