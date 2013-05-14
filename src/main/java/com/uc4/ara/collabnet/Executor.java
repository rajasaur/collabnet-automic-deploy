package com.uc4.ara.collabnet;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import com.uc4.ara.collabnet.JArgs.CmdLineParser;
import com.uc4.ara.collabnet.Logging.ErrorCodes;
import com.uc4.ara.collabnet.Logging.Logger;

public class Executor {

	/**
	 * The compiled version.
	 */
	private static String compiledVersion = null;

	/**
	 * The compiled date.
	 */
	private static String compiledDate = null;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Logger.logMsg("\r\nUC4 ARA - TeamForge Integration Utility");
			Logger.logMsg("Version: " + getCompiledVersion("ARATeamForgeUtility"));
			Logger.logMsg("Build Date: " + getCompiledDate("ARATeamForgeUtility"));
			Logger.logMsg("\r\nThis utility is meant to be used with UC4 ARA and CollabNet TeamForge and allows for the creation of Package objects in the UC4 Release Manager. Properties necessary for the TeamForge integration can be assigned such as the \"TeamForge Project ID\" to a which a component in the package belongs to as well as references to its build location. Functions are described in detail below.\r\n");
			Logger.logMsg("================================================================================\r\n");
		
			CmdLineParser parser = new CmdLineParser();
			
			if(args.length == 0) {
				printUsage();
				System.exit(ErrorCodes.ERROR);
			}
			
			String pkg = Executor.class.getPackage().getName() + "."; 
			String className = args[0];
			
			if(className.equalsIgnoreCase("assigncomponent")) {
				className = pkg + "AssignComponent";
			} else if(className.equalsIgnoreCase("createpackage")) {
				className = pkg + "CreatePackage";
			} else {
				printUsage();
				System.exit(ErrorCodes.ERROR);
			}
			
			Class<?> clazz = Class.forName(className);
			
			String[] myArgs = new String[args.length - 1];
			for(int i = 1; i < args.length; i++)
				myArgs[i-1] = args[i];
			
			AbstractFeature instance = (AbstractFeature) clazz.newInstance();
			
			instance.setParams(parser);
			parser.parse(myArgs);
			int returnCode = instance.execute(parser);
			
			System.exit(returnCode);
		} catch (Exception ex ) {
			Logger.logException(ex);
			System.exit(ErrorCodes.EXCEPTION);
		}
	}

	private static void printUsage() {
		Logger.logMsg("Usage: Command [Arguments]\r\n");
		Logger.logMsg("Available Commands:\r\n");
		Logger.logMsg(" - CreatePackage: creates a deploymentpackage in rm");
		Logger.logMsg(" - AssignComponent: assigns a given component to a given package\r\n");
		Logger.logMsg("To view the usage of a command call it without parameters");
	}
	
	public static String getCompiledVersion(String manifestContentGroup)
			throws IOException {

		if (compiledVersion != null)
			return compiledVersion;

		Enumeration<URL> resources = 
				Executor.class.getClassLoader().getResources("META-INF/MANIFEST.MF");

		while (resources.hasMoreElements()) {
			try {
					
			URL url = resources.nextElement();
			String u = url.getFile();
			int idx = u.indexOf("!");
			if (idx > 0)
				u = u.substring(0, idx);
			if (u.startsWith("file:"))
				u = u.substring(5);
			
				JarFile myJar = new JarFile(u); 
				Manifest manifest = myJar.getManifest();
				Map<String, Attributes> manifestContents = manifest
						.getEntries();
				for (Map.Entry<String, Attributes> entry : manifestContents
						.entrySet()) {
					Attributes attrs = entry.getValue();
					for (Map.Entry<Object, Object> entry2 : attrs.entrySet()) {
						if (entry.getKey().equals(manifestContentGroup)) 
							if(entry2.getKey().toString()
									.equals("Application-BuildDate")) {
								compiledDate = entry2.getValue().toString();
							}
						if (entry.getKey().equals(manifestContentGroup)) 
								if(entry2.getKey().toString()
										.equals("Application-Version")) {
							compiledVersion = entry2.getValue().toString();
							
							return compiledVersion;
						}
					}
				}
			} catch (IOException e) {
			}
		}
		compiledVersion = "unknown";
		compiledDate = "unknown";
		return compiledVersion;
	}

	/**
	 * Gets the compiled date.
	 * 
	 * @param manifestContentGroup
	 *            the manifest content group
	 * @return the compiled date
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static String getCompiledDate(String manifestContentGroup)
			throws IOException {
		if (compiledDate == null)
			getCompiledVersion(manifestContentGroup);
		return compiledDate;
	}

}
