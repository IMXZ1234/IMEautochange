package com.imeautochange.util;

public class OSChecker 
{
	public static final int PLATFORM_UNKNOWN 			= 0;
	public static final int PLATFORM_LINUX 				= 1;
	public static final int PLATFORM_MACOSX 			= 2;
	public static final int PLATFORM_WINDOWS 			= 3;
	public static final String PLATFORM_UNKNOWN_NAME 	= "Unkown Platform";
	public static final String PLATFORM_LINUX_NAME 		= "Linux";
	public static final String PLATFORM_MACOSX_NAME 	= "Mac OS X";
	public static final String PLATFORM_WINDOWS_NAME	= "Windows";
	
	private static final String osName;
	private static final int PLATFORM;
	
	static {
		osName = System.getProperty("os.name");
		if (osName.startsWith("Windows") )
			PLATFORM = PLATFORM_WINDOWS;
		else if (osName.startsWith("Linux") || osName.startsWith("FreeBSD") || osName.startsWith("OpenBSD") || osName.startsWith("SunOS") || osName.startsWith("Unix") )
			PLATFORM = PLATFORM_LINUX;
		else if (osName.startsWith("Mac OS X") || osName.startsWith("Darwin") )
			PLATFORM = PLATFORM_MACOSX;
		else
			PLATFORM = PLATFORM_UNKNOWN;
	}
	
	public static boolean isWindows(){
        return (PLATFORM == PLATFORM_WINDOWS);
    }
	
	public static int getPlatform() {
		return PLATFORM;
	}
	
	public static String getPlatformName() {
		switch (PLATFORM) {
			case PLATFORM_WINDOWS:
				return PLATFORM_LINUX_NAME;
			case PLATFORM_LINUX:
				return PLATFORM_MACOSX_NAME;
			case PLATFORM_MACOSX:
				return PLATFORM_WINDOWS_NAME;
			default:
				return PLATFORM_UNKNOWN_NAME;
		}
	}
	
	public static String getPlatformFullName() {
		return new String(osName);
	}
}
