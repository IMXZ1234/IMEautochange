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
	public static final boolean LANGUAGE_INGAME = true;
	public static final boolean LANGUAGE_INGUI = false;
	public static final String keyWarningKLInGameNotInstalled = "text.imeautochange.warningKLInGameNotInstalled";
	public static final String keyWarningKLInGuiNotInstalled = "text.imeautochange.warningKLInGuiNotInstalled";
	public static final String keyWarningKLInGameNotSupported = "text.imeautochange.warningKLInGameNotSupported";
	public static final String keyWarningKLInGuiNotSupported = "text.imeautochange.warningKLInGuiNotSupported";
	public static final String keyInfoKLInGameInstalled = "text.imeautochange.infoKLInGameInstalled";
	public static final String keyInfoKLInGuiInstalled = "text.imeautochange.infoKLInGuiInstalled";
	public static final String keyInfoKLInGameSupported = "text.imeautochange.infoKLInGameSupported";
	public static final String keyInfoKLInGuiSupported = "text.imeautochange.infoKLInGuiSupported";
	
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
	
	/**
	 * Initialization. Note when this is called, Minecraft is not fully initialized,
	 * and attempt should not be made to print anything in ChatGui, 
	 * which will incur a {@link NullPointerException}.
	 * 
	 * @param languageInGameIn
	 * @param languageInGuiIn
	 * @param baseDir 
	 */
//	public static void init() {
//		
//		File dir = new File(baseDir, "ime_support");
//		switch (platform) {
//		case OSChecker.PLATFORM_WINDOWS:
//			supportedLanguageListDirFile = new File(dir, "windows");
//			break;
//		case OSChecker.PLATFORM_LINUX:
//			supportedLanguageListDirFile = new File(dir, "linux");
//			break;
//		case OSChecker.PLATFORM_MACOSX:
//			supportedLanguageListDirFile = new File(dir, "macosx");
//			break;
//		default:
//			supportedLanguageListDirFile = null;
//			return;
//		}
//	}
	
	public static boolean switchIMETo(IMEInfo IMEInfo) {
		if(IMEInfo == null) {
			LOGGER.error("Specified IME is null.");
			return false;
		}
		return switchIMETo(IMEInfo.name);
	}
	
	public static boolean switchIMETo(String IMEName) {
		System.out.println("switching to "+IMEName);
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
