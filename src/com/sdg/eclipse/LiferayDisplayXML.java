package com.sdg.eclipse;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

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

		InputStream contents = null;
		Document doc = null;
		IFile file = project.getFolder("docroot/WEB-INF").getFile("liferay-display.xml");
		try {
			DocumentBuilder db = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			contents = file.getContents();
			doc = db.parse(contents);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				contents.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Node portletNode = doc.createElement("portlet");
		Node idAttr = doc.createAttribute("id");
		idAttr.setTextContent(portletName);
		portletNode.getAttributes().setNamedItem(idAttr);
		
		Node displayNode = doc.getElementsByTagName("display").item(0);
		Node categoryNode = getCategoryNode(doc, displayNode);
		categoryNode.appendChild(portletNode);
		

		TransformerFactory transformerFactory = TransformerFactory
				.newInstance();
		try {
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			StreamResult result = new StreamResult(byteArrayOutputStream);
			transformer.transform(source, result);
			file.setContents(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()),
							IFile.FORCE, null);
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
