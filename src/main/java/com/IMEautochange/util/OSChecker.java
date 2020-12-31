package com.IMEautochange.util;

public class OSChecker {
	
	private static String OSname = System.getProperty("os.name").toLowerCase();
	
	public static boolean isWindows(){
        return OSname.indexOf("windows")>=0;
    }
	
}
