package com.imeautochange.nativefunction;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.imeautochange.config.IMEInfo;
import com.imeautochange.util.OSChecker;

/**
 * Provides platform-independent functions.
 * @author IMXZ
 *
 */
public class NativeFunctionManager 
{
	
	private static final Logger LOGGER = LogManager.getLogger();
	private static int platform;
	private static INativeFunctionProvider nativeFunctionProvider;
	
	private static ArrayList<IMEInfo> imeInfoList;
	private static IMEInfo defaultIME;
	private static IMEInfo engishIME;
	
	public static void init(){
		platform = OSChecker.getPlatform();
		switch (platform) { 
		case OSChecker.PLATFORM_WINDOWS:
			LOGGER.info("platform Windows");
			nativeFunctionProvider = WindowsNativeFunctionProvider.INSTANCE;
			break;
		case OSChecker.PLATFORM_LINUX:
			nativeFunctionProvider = null;//new LinuxNativeFunctionProvider();
			return;
		case OSChecker.PLATFORM_MACOSX:
			nativeFunctionProvider = null;//new MacOSXNativeFunctionProvider();
			return;
		default:
			LOGGER.error("Unsupported Platform: " + OSChecker.getPlatformFullName());
			nativeFunctionProvider = null;
			return;
		}
		nativeFunctionProvider.reloadIMEList();
		imeInfoList = new ArrayList<IMEInfo>();
		if(!nativeFunctionProvider.getIMEInfoList(imeInfoList)) {
			LOGGER.error("Unsuspected error on getting IME list.");
		}
		// Avoid null pointer error.
		defaultIME = new IMEInfo(null, null);
		if(!nativeFunctionProvider.getDefaultIME(defaultIME)) {
			LOGGER.error("Unsuspected error on getting default IME.");
		}
		engishIME = new IMEInfo(null, null);
		if(nativeFunctionProvider.getEnglishIME(engishIME) == INativeFunctionProvider.RESULT_ERROR) {
			LOGGER.error("Unsuspected error on getting default IME.");
		}
		return;
	}
	
	public static boolean doesFunctionProviderExist() {
		return (nativeFunctionProvider != null);
	}
	
	public static boolean switchIMETo(IMEInfo IMEInfo) {
		if(IMEInfo == null) {
			LOGGER.error("Specified IME is null.");
			return false;
		}
		return switchIMETo(IMEInfo.name);
	}
	
	public static boolean switchIMETo(String IMEName) {
		if(IMEName == null) {
			return false;
		}
		System.out.println("switching to"+IMEName);
		switch(nativeFunctionProvider.switchIMETo(IMEName)) {
		case INativeFunctionProvider.RESULT_OK:
			return true;
		case INativeFunctionProvider.RESULT_NOTINSTALLED:
			LOGGER.error("Specified IME not installed.");
			return false;
		default:
			LOGGER.error("Unsuspected error on switching IME.");
			return false;
		}
	}
	
	/**
	 * Default reloading.
	 * @return
	 */
	public static ArrayList<IMEInfo> getIMEInfoList(){
		return getIMEInfoList(true);
	}
	
	
	public static boolean reloadIMEInfoList() {
		if (!nativeFunctionProvider.reloadIMEList()) {
			LOGGER.error("Unsuspected error on reloading IME list.");
			return false;
		}
		if (!nativeFunctionProvider.getIMEInfoList(imeInfoList)) {
			LOGGER.error("Unsuspected error on getting IME list.");
		}
		return true;
	}

	/**
	 * Returns maintained IMEInfoList.
	 * 
	 * @param reload If internal IME List should be reloaded.
	 */
	public static ArrayList<IMEInfo> getIMEInfoList(boolean reload) {
		if (reload) {
			if (!nativeFunctionProvider.reloadIMEList()) {
				LOGGER.error("Unsuspected error on reloading IME list.");
			}
		}
		if (!nativeFunctionProvider.getIMEInfoList(imeInfoList)) {
			LOGGER.error("Unsuspected error on getting IME list.");
		}
		return imeInfoList;
	}

	public static IMEInfo getDefaultIME() {
		if(!nativeFunctionProvider.getDefaultIME(defaultIME)) {
			LOGGER.error("Unsuspected error on getting default IME.");
		}
		return defaultIME;
	}
	
	public static IMEInfo getEnglishIME() {
		if(nativeFunctionProvider.getEnglishIME(engishIME) == INativeFunctionProvider.RESULT_ERROR) {
			LOGGER.error("Unsuspected error on getting English IME.");
		}
		return engishIME;
	}
	
	public static boolean isIMEInstalled(String imeName) {
		return nativeFunctionProvider.isIMEInstalled(imeName);
	}
	public static boolean isIMEInstalled(IMEInfo imeInfo) {
		return isIMEInstalled(imeInfo.name);
	}
}
