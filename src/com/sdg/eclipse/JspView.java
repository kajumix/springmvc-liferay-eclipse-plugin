package com.sdg.eclipse;

import java.util.Map;

import org.eclipse.core.resources.IProject;

public class JspView {
	
	private static final String VIEW_JSP = "jsp-skeleton.jsp";
	
	private String name;
	private IProject project;
	Map<String, String> jspReplaceMap;

	public JspView(String name, IProject project) {
		
		
		this.name = name;
		this.project = project;
	}

	public void create() {
		Util.createFile(getJSPFolder(), name+".jsp", VIEW_JSP,	null);
		
	}

	private String getJSPFolder() {
		String folder = "/" + project.getName()
				+ "/docroot/WEB-INF/jsp";
		Util.createIFNeeded(folder);
		return folder;
	}


}
