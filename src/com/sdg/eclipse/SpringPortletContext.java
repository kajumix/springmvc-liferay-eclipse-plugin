package com.sdg.eclipse;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IProject;

public class SpringPortletContext {
	private static final String CONTEXT_SEKELOTON_XML = "context-sekeloton.xml";

	private String portletName;
	private IProject project;
	Map<String, String> configReplaceMap;
	
	public SpringPortletContext(String portletName, String className, IProject project, String pkg) {
		this.portletName = portletName;
		this.project = project;
		configReplaceMap = new HashMap<String, String>();
		configReplaceMap.put("\\{pkg\\}", pkg);
		configReplaceMap.put("\\{class\\}",className);
	}

	public void create() {
		Util.createFile(getConfigFolder(),getPortletXMLName(),CONTEXT_SEKELOTON_XML, configReplaceMap);
		
	}
	private String getConfigFolder() {
		return "/" + project.getName() + "/docroot/WEB-INF";
	}

	private String getPortletXMLName() {
		return portletName + "-portlet.xml";
	}
	
}
