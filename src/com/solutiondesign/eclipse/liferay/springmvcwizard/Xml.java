package com.solutiondesign.eclipse.liferay.springmvcwizard;

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
import org.eclipse.core.runtime.CoreException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class Xml {
	
	private IFile file;
	
	private Document doc;

	public Xml (IFile file) {
		
		this.file=file;
		
		InputStream contents = null;
		
		
		try {
			DocumentBuilder db = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			contents = file.getContents();
			doc = db.parse(contents);
		} catch (CoreException e) {
			throw new RuntimeException(e);
		} catch (SAXException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (ParserConfigurationException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				contents.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		
	}
	
	public void update() {
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
			throw new RuntimeException(e);
		} catch (CoreException e) {
			throw new RuntimeException(e);
		}
	}

	public Document getDocument() {
		return doc;
	}

	
}
