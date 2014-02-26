package com.solutiondesign.eclipse.liferay.springmvcwizard;

import org.eclipse.core.resources.IProject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class WebXML {

	
	private static final String DISPATCHER_SERVLET = "dispatcher";
	private static final String VIEW_URL_PATTERN = "/WEB-INF/servlet/view";
	private static final String VIEW_SERVLET = "view-servlet";
	private static final String DISPATCHER_CLASS = "org.springframework.web.servlet.DispatcherServlet";
	private static final String VIEW_RENDER_CLASS = "org.springframework.web.servlet.ViewRendererServlet";
	private IProject project;
	private boolean needDispatcher = true;
	private boolean needViewRender = true;

	public WebXML(IProject project) {
		this.project = project;
	}

	public void addSpringServlets() {
		Xml xml = new Xml(project.getFolder("docroot/WEB-INF").getFile("web.xml"));
		Document doc = xml.getDocument();
		determineNeeds(doc);
		if (needViewRender) {
			addServlet(doc, VIEW_RENDER_CLASS, VIEW_SERVLET);
			addServletMapping(doc, VIEW_URL_PATTERN, VIEW_SERVLET);
		}

		if (needDispatcher) {
			addServlet(doc, DISPATCHER_CLASS, DISPATCHER_SERVLET);
//			addServletMapping(doc, DISPATCHER_URL_PATTERN, DISPATCHER_SERVLET);
		}

		if (needDispatcher || needViewRender) {
			xml.update();
		}
	}

	private void addServlet(Document doc, String viewRenderClass,
			String servletName) {
		Element servletNode = doc.createElement("servlet");
		Node servletNameNode = doc.createElement("servlet-name");
		servletNameNode.setTextContent(servletName);
		servletNode.appendChild(servletNameNode);
		Node servletClassNode = doc.createElement("servlet-class");
		servletClassNode.setTextContent(viewRenderClass);
		servletNode.appendChild(servletClassNode);
		Node webAppNode = doc.getElementsByTagName("web-app").item(0);
		webAppNode.appendChild(servletNode);
	}

	private void addServletMapping(Document doc, String urlPattern,
			String servletName) {
		Element servletNode = doc.createElement("servlet-mapping");
		Node servletNameNode = doc.createElement("servlet-name");
		servletNameNode.setTextContent(servletName);
		servletNode.appendChild(servletNameNode);
		Node servletClassNode = doc.createElement("url-pattern");
		servletClassNode.setTextContent(urlPattern);
		servletNode.appendChild(servletClassNode);
		Node webAppNode = doc.getElementsByTagName("web-app").item(0);
		webAppNode.appendChild(servletNode);
	}

	private void determineNeeds(Document doc) {
		NodeList servletClassNodes = doc.getElementsByTagName("servlet-class");
		int length = servletClassNodes.getLength();
		if (servletClassNodes != null || length > 0) {
			for (int i = 0; i < length; i++) {
				Node servletClassNode = servletClassNodes.item(i);
				String servletClass = servletClassNode.getTextContent();
				if (servletClass != null
						&& servletClass.equals(VIEW_RENDER_CLASS)) {
					needViewRender = false;
				}

				if (servletClass != null
						&& servletClass.equals(DISPATCHER_CLASS)) {
					needDispatcher = false;
				}
			}
		}
	}

}
