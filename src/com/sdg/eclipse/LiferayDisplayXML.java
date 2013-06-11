package com.sdg.eclipse;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class LiferayDisplayXML {
	private IProject project;
	private String category;
	private String portletName;

	public LiferayDisplayXML(IProject project, String category,
			String portletName) {
		this.project = project;
		this.category = category;
		this.portletName = portletName;
	}

	public void update() {
		IFile file = project.getFolder("docroot/WEB-INF").getFile("liferay-display.xml");
		Xml xml = new Xml(file);
		Document doc = xml.getDocument();
		
		
		Node portletNode = doc.createElement("portlet");
		Node idAttr = doc.createAttribute("id");
		idAttr.setTextContent(portletName);
		portletNode.getAttributes().setNamedItem(idAttr);
		
		Node displayNode = doc.getElementsByTagName("display").item(0);
		Node categoryNode = getCategoryNode(doc, displayNode);
		categoryNode.appendChild(portletNode);
		

		xml.update();

	}

	private Node getCategoryNode(Document doc, Node displayNode) {
		NodeList nodes = doc.getElementsByTagName("category");
		for(int i=0; i<nodes.getLength();i++) {
			Node item = nodes.item(i);
			Node namedItem = item.getAttributes().getNamedItem("name");
			if (namedItem!=null && namedItem.getTextContent()!=null && namedItem.getTextContent().equals(category)) {
				return item;
			}
		}
		Node categoryNode = doc.createElement("category");
		Node nameAttr = doc.createAttribute("name");
		nameAttr.setTextContent(category);
		categoryNode.getAttributes().setNamedItem((nameAttr));
		displayNode.appendChild(categoryNode);
		return categoryNode;
	}
}
