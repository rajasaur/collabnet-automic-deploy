package com.uc4.ara.collabnet.Logging;

public class Logger {

	public static void logMsg(String message) {
		System.out.println(message);
	}
	
	public static void logException(Exception ex) {
		Logger.logMsg("EXCEPTION: ");
		Logger.logMsg(ex.getMessage());
		Logger.logMsg("StackTrace: ");
		for(StackTraceElement element : ex.getStackTrace()) {
			Logger.logMsg(element.toString());
		}
	}
	
	public static void logError(String message) {
		Logger.logMsg("ERROR: ");
		Logger.logMsg(message);
	}
}
