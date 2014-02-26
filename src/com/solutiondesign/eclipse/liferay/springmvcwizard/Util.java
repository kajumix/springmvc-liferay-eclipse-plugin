package com.solutiondesign.eclipse.liferay.springmvcwizard;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;

public class Util {

	public static IFile createFile(String folderStr, String fileName,
			String resourceStr, Map<String, String> replaceMap) {
		IFolder folder = ResourcesPlugin.getWorkspace().getRoot()
				.getFolder(new Path(folderStr));
		IFile classFile = folder.getFile(fileName);
		Resource resource = new Resource(resourceStr);
		try {
			String str = getString(resource.getStream());
			if (replaceMap != null) {
				str = replace(str, replaceMap);
			}
			classFile.create(new ByteArrayInputStream(str.getBytes()), false,
					null);
		} catch (CoreException e) {
			throw new RuntimeException(e);
		} finally {
			resource.close();
		}
		return classFile;
	}

	private static String replace(String string, Map<String, String> replaceMap) {
		Set<String> keySet = replaceMap.keySet();
		for (String regex : keySet) {
			string = string.replaceAll(regex, replaceMap.get(regex));
		}
		return string;
	}

	private static String getString(InputStream is) {
		Scanner scanner = null;
		try {
			scanner = new Scanner(is).useDelimiter("\\A");
			return scanner.hasNext() ? scanner.next() : "";
		} finally {
			scanner.close();
		}

	}

	public static void createIFNeeded(String folder) {
		IFolder jspFolder = ResourcesPlugin.getWorkspace().getRoot()
				.getFolder(new Path(folder));
		if (!jspFolder.exists()) {
			try {
				jspFolder.create(true, true, null);
			} catch (CoreException e) {
				throw new RuntimeException(e);
			}
		}

	}
}
