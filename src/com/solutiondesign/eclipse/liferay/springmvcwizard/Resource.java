package com.solutiondesign.eclipse.liferay.springmvcwizard;

import java.io.IOException;
import java.io.InputStream;

public class Resource {
	private String name;
	private InputStream resourceStream;
	private static final String RESOURCES_FOLDER = "com/sdg/eclipse/resources/";

	public Resource(String name) {
		this.name=name;
	}

	public InputStream getStream(){
		if (resourceStream==null) {
			resourceStream = getClass().getClassLoader().getResourceAsStream(getResourcePath());
		}
		return resourceStream;
	}

	private String getResourcePath() {
		return RESOURCES_FOLDER + name;
	}
	
	public void close() {
		try {
			resourceStream.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
