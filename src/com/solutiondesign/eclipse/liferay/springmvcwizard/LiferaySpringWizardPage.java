package com.solutiondesign.eclipse.liferay.springmvcwizard;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class LiferaySpringWizardPage {
	
	
	
	public void makeLabel(Composite container, String labelText) {
		Label portletNamelabel = new Label(container, SWT.NULL);
		portletNamelabel.setText(labelText);
	}
	
	public Composite createContainer(Composite parent, int numColumns, int verticalSpacing) {
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout gridLayout = new GridLayout();
		container.setLayout(gridLayout);
		
		gridLayout.numColumns = numColumns;
		
		gridLayout.verticalSpacing = verticalSpacing;
		return container;
	}

	public Text makeTextAndBrowse(Composite container,
			SelectionAdapter listener) {
		Text text = new Text(container,SWT.BORDER|SWT.SINGLE);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		text.setLayoutData(gridData);
		Button button = new Button(container, SWT.PUSH);
		button.setText("Browse...");
		button.addSelectionListener(listener);
		return text;
	}
	
	public String convertToCamelCase(String text) {
		// return text.replaceAll("(?<=-)(.)","$1")
		String[] parts = text.split("-");
		String converted = "";
		for (String part : parts) {
			converted = converted + toProperCase(part);
		}
		return converted;
	}

	private String toProperCase(String s) {
		return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
	}
}
