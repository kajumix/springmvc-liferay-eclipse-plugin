package com.solutiondesign.eclipse.liferay.springmvcwizard;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;
import org.eclipse.ui.dialogs.SelectionDialog;

public class LiferaySpringWizardPageOne extends WizardPage {

	private static final int VERTICAL_SPACING = 9;
	private static final int NUM_COLUMNS = 3;
	private static LiferaySpringWizardPage page = new LiferaySpringWizardPage();
	private Text portletNameText;
	private IProject project;
	private Text packageText;
	private Text srcFolderText;
	private Text categoryText;
	private Button instanceAbleCheck;
	private Text classText;

	

	public boolean isInstanceable() {
		return instanceAbleCheck.getSelection();
	}

	public String getPortletName() {
		return portletNameText.getText();
	}

	public IProject getSelectedProject() {
		return project;
	}

	public String getSourceFolder() {
		return srcFolderText.getText();
	}

	public String getClassName() {
		return classText.getText();
	}

	public String getPackage() {
		return packageText.getText();
	}


	public String getCategory() {
		return categoryText.getText();
	}

	protected LiferaySpringWizardPageOne(String pageName) {
		super(pageName);
		setTitle("Create Spring MVC Portlet for Liferay");
		
		
	}
	
	

	
	@Override
	public void createControl(Composite parent) {
		Composite container = page.createContainer(parent, NUM_COLUMNS,
				VERTICAL_SPACING);

		createChooseProject(container);
		createPortletName(container);
		createCategoryName(container);
		createInstanceableCheck(container);
		createBrowseSourceFolder(container);
		createBrowsePackage(container);
		createClass(container);
		

		Listener changeListener = new Listener() {

			@Override
			public void handleEvent(Event event) {
				updateButtons();
				
			}

			
		};
		
		Control[] controls = container.getChildren();
		for (Control control : controls) {
			if (control instanceof Text) {
				control.addListener(SWT.Modify, changeListener);
			} else if (control instanceof Combo) {
				control.addListener(SWT.Selection, changeListener);
			}
		}
		
		setControl(container);

	}



	private void createClass(Composite container) {
		page.makeLabel(container, "Class");
		classText = new Text(container, SWT.BORDER | SWT.SINGLE);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		classText.setLayoutData(gridData);
		page.makeLabel(container, "");

	}

	private void createInstanceableCheck(Composite container) {
		page.makeLabel(container, "Instanceable");
		instanceAbleCheck = new Button(container, SWT.CHECK);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		instanceAbleCheck.setLayoutData(gridData);
		page.makeLabel(container, "");

	}

	private void createCategoryName(Composite container) {
		page.makeLabel(container, "Category (optional)");
		categoryText = new Text(container, SWT.BORDER | SWT.SINGLE);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		categoryText.setLayoutData(gridData);
		page.makeLabel(container, "");

	}

	private void createBrowseSourceFolder(Composite container) {
		page.makeLabel(container, "Source folder");
		SelectionAdapter listener = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleBrowseSourceFolder();
			}
		};
		srcFolderText = page.makeTextAndBrowse(container, listener);

	}

	private void createBrowsePackage(Composite container) {
		page.makeLabel(container, "Package");
		SelectionAdapter listener = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleBrowsePackage();
			}
		};
		packageText = page.makeTextAndBrowse(container, listener);

	}

	@SuppressWarnings({ "restriction" })
	private void handleBrowsePackage() {
		try {
			IJavaProject javaProject = JavaCore.create(getSelectedProject());

			SelectionDialog createPackageDialog = JavaUI.createPackageDialog(
					getShell(), javaProject, SWT.NULL);
			if (createPackageDialog.open() == ContainerSelectionDialog.OK) {
				Object[] result = createPackageDialog.getResult();

				if (result != null && result.length > 0) {
					IPackageFragment pkg = (IPackageFragment) result[0];
					packageText.setText(pkg.getElementName());
				}
			}
		} catch (JavaModelException e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("restriction")
	private void handleBrowseSourceFolder() {
		ContainerSelectionDialog dialog = new ContainerSelectionDialog(
				getShell(), getSelectedProject(), false,
				"Choose a source folder:");

		dialog.setBlockOnOpen(true);
		dialog.showClosedProjects(false);
		if (dialog.open() == ContainerSelectionDialog.OK) {
			Object[] result = dialog.getResult();
			if (result != null && result.length > 0) {
				srcFolderText.setText(result[0].toString());
			}
		}
	}




	private void createPortletName(Composite container) {
		page.makeLabel(container, "Portlet name");
		portletNameText = new Text(container, SWT.BORDER | SWT.SINGLE);

		FocusListener listener = new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				updateClassText();

			}

			@Override
			public void focusGained(FocusEvent e) {
				updateClassText();

			}
		};
		portletNameText.addFocusListener(listener);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		portletNameText.setLayoutData(gridData);
		page.makeLabel(container, "");
	}

	private void updateClassText() {
		classText.setText(page.convertToCamelCase(portletNameText.getText())
				+ "Controller");
		

	}

	private void createChooseProject(Composite container) {
		final IProject[] projects = getProjects();
		page.makeLabel(container, "Choose Project");
		final Combo combo = new Combo(container, SWT.DROP_DOWN | SWT.READ_ONLY);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		combo.setLayoutData(gridData);
		combo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				project = projects[combo.getSelectionIndex()];
			}
		});

		for (int i = 0; i < projects.length; i++) {
			if (projects[i].isOpen()) {
				combo.add(projects[i].getName());
			}
		}
		page.makeLabel(container, "");
		

	}

	public IProject[] getProjects() {
		return ResourcesPlugin.getWorkspace().getRoot().getProjects();

	}

	@Override
	public boolean canFlipToNextPage() {
		return !(getSelectedProject() == null || getPortletName() == null
				|| getPortletName().isEmpty() || getSourceFolder() == null
				|| getSourceFolder().isEmpty() || getPackage() == null
				|| getPackage().isEmpty() || getClassName() == null || getClassName()
				.isEmpty());
	}
	

	

	private void updateButtons() {
		getWizard().getContainer().updateButtons();
	}
}
