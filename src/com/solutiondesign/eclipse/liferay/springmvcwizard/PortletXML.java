package com.solutiondesign.eclipse.liferay.springmvcwizard;

import org.eclipse.core.resources.IProject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class PortletXML {

	private String portletTitle;
	private String portletName;
	private IProject project;
	private String portletDisplayName;

	public PortletXML(IProject project, String portletName,
			String portletDisplayName, String portletTitle) {
		this.project = project;
		this.portletName = portletName;
		this.portletTitle = portletTitle;
		this.portletDisplayName = portletDisplayName;
	}

	public void update() {
		Xml portletXml = new Xml(project.getFolder("docroot/WEB-INF").getFile(
				"portlet.xml"));
		Document document = portletXml.getDocument();
		Node portletAppNode = document.getElementsByTagName("portlet-app")
				.item(0);

		Node portletNode = document.createElement("portlet");

		Node portletNameNode = document.createElement("portlet-name");
		portletNameNode.setTextContent(portletName);

		Node portletDisplayNameNode = document.createElement("display-name");
		portletDisplayNameNode.setTextContent(portletDisplayName);

		Node portletClassNode = document.createElement("portlet-class");
		portletClassNode
				.setTextContent("org.springframework.web.portlet.DispatcherPortlet");

		Node initParamContextNode = document.createElement("init-param");
		Node initParamContextNameNode = document.createElement("name");
		initParamContextNameNode.setTextContent("contextConfigLocation");
		initParamContextNode.appendChild(initParamContextNameNode);
		Node initParamContextValueNode = document.createElement("value");
		initParamContextValueNode.setTextContent("/WEB-INF/" + portletName
				+ "-portlet.xml");
		initParamContextNode.appendChild(initParamContextValueNode);

		Node supportsNode = document.createElement("supports");
		Node mimeTypeNode = document.createElement("mime-type");
		mimeTypeNode.setTextContent("text/html");
		Node portletModeNode = document.createElement("portlet-mode");
		portletModeNode.setTextContent("view");

		supportsNode.appendChild(mimeTypeNode);
		supportsNode.appendChild(portletModeNode);

		Node portletInfoNode = document.createElement("portlet-info");
		Node titleNode = document.createElement("title");
		titleNode.setTextContent(portletTitle);

		portletInfoNode.appendChild(titleNode);

		portletNode.appendChild(portletNameNode);
		portletNode.appendChild(portletDisplayNameNode);
		portletNode.appendChild(portletClassNode);
		portletNode.appendChild(initParamContextNode);
		portletNode.appendChild(supportsNode);
		portletNode.appendChild(portletInfoNode);
		portletAppNode.appendChild(portletNode);

		portletXml.update();

	}

}
