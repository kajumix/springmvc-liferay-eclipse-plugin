package com.sdg.eclipse;

import java.util.HashMap;
import java.util.Map;

public class ControllerClass {
	private static final String CONTROLLER_JAVA_SKELETON = "controller.java.skeleton";
	
	private String name;

	private String sourceFolder;
	
	Map<String, String> sourceReplaceMap;

	private String pkg;

	public ControllerClass(String name, String sourceFolder, String pkg, String view) {
		sourceReplaceMap = new HashMap<String, String>();
		sourceReplaceMap.put("\\{pkg\\}", pkg);
		sourceReplaceMap.put("\\{class\\}", name);
		sourceReplaceMap.put("\\{jsp\\}", view);
		this.name=name;
		this.sourceFolder = sourceFolder;
		this.pkg = pkg;
	}
	
	public void create() {
		Util.createFile(getSourceFolder(), getJavaName(),CONTROLLER_JAVA_SKELETON, sourceReplaceMap);
	}
	
	private String getSourceFolder() {
		String folder = sourceFolder + "/" + pkg.replaceAll("\\.", "/");
		Util.createIFNeeded(folder);
		return folder;
	}
	
	private String getJavaName() {
		return name + ".java";
	}
}
