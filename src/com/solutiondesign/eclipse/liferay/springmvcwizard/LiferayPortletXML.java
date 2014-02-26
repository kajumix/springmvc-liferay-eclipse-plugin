package com.solutiondesign.eclipse.liferay.springmvcwizard;

import org.eclipse.core.resources.IProject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class LiferayPortletXML {

	private boolean instanceable;
	private String portletName;
	private IProject project;

	public LiferayPortletXML(IProject project, String portletName,
			boolean instanceable) {
		this.project = project;
		this.portletName = portletName;
		this.instanceable = instanceable;
	}

	public void update() {
		Xml liferayPortletXml = new Xml(project.getFolder("/docroot/WEB-INF").getFile("liferay-portlet.xml"));
		Document document = liferayPortletXml.getDocument();
		Node portletAppNode = document.getElementsByTagName("liferay-portlet-app").item(0);
		Node portletNode = document.createElement("portlet");
		Node portletNameNode = document.createElement("portlet-name");
		portletNameNode.setTextContent(portletName);
		portletNode.appendChild(portletNameNode);
		Node iconNode = document.createElement("icon");
		iconNode.setTextContent("/icon.png");
		portletNode.appendChild(iconNode);
		Node instanceableNode = document.createElement("instanceable");
		instanceableNode.setTextContent(instanceable ? "true":"false");
		portletNode.appendChild(instanceableNode);
		Node cssNode = document.createElement("header-portlet-css");
		cssNode.setTextContent("/css/main.css");
		portletNode.appendChild(cssNode);
		Node javaScriptNode = document.createElement("footer-portlet-javascript");
		javaScriptNode.setTextContent("/js/main.js");
		portletNode.appendChild(javaScriptNode );
		Node cssClassWrapperNode = document.createElement("css-class-wrapper");
		cssClassWrapperNode.setTextContent(portletName+"-portlet");
		portletNode.appendChild(cssClassWrapperNode);
		portletAppNode.appendChild(portletNode);
		
		liferayPortletXml.update();
	}

}
