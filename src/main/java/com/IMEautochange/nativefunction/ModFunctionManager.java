package com.IMEautochange.nativefunction;

import com.IMEautochange.IMEautochange;
import com.IMEautochange.util.OSChecker;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Provides platform-independent functions.
 * @author IMXZ
 *
 */
public class ModFunctionManager 
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
	
	private static String languageInGame = "en_us";
	private static String languageInGui = "zh_cn";
	private static boolean isLanguageInGameInstalled;
	private static boolean isLanguageInGuiInstalled;
	private static boolean isLanguageInGameSupported;
	private static boolean isLanguageInGuiSupported;
	
	private static NativeFunctionProvider nativeFunctionProvider;
	
	public static void getNativeFunctionProvider(){
		int platform = OSChecker.getPlatform();
		switch (platform) { 
		case OSChecker.PLATFORM_WINDOWS:
			nativeFunctionProvider = new WindowsNativeFunctionProvider();
			break;
		case OSChecker.PLATFORM_LINUX:
			nativeFunctionProvider = null;//new LinuxNativeFunctionProvider();
			break;
		case OSChecker.PLATFORM_MACOSX:
			nativeFunctionProvider = null;//new MacOSXNativeFunctionProvider();
			break;
		default:
			IMEautochange.logger.error("Unknown Platform: " + OSChecker.getPlatformFullName());
			nativeFunctionProvider = null;
		}
	}
	
	public static boolean doesFunctionProviderExist() {
		return (nativeFunctionProvider != null);
	}
	
	/**
	 * Initialization. Note when this is called, Minecraft is not fully initialized,
	 * and attempt should not be made to print anything in ChatGui, 
	 * which will incur a null pointer error.
	 * 
	 * @param languageInGameIn
	 * @param languageInGuiIn
	 */
	public static void init(String languageInGameIn, String languageInGuiIn) {
		languageInGame = languageInGameIn;
		languageInGui = languageInGuiIn;
		reloadLanguageList(true);
		isLanguageInGameInstalled = nativeFunctionProvider.isLanguageInstalled(languageInGame);
		isLanguageInGuiInstalled = nativeFunctionProvider.isLanguageInstalled(languageInGui);
		isLanguageInGameSupported = nativeFunctionProvider.isLanguageSupported(languageInGame);
		isLanguageInGuiSupported = nativeFunctionProvider.isLanguageSupported(languageInGui);
	}
	
	public boolean isLanguageInGameInstalled() {
		return isLanguageInGameInstalled;
	}
	public boolean isLanguageInGameSupported() {
		return isLanguageInGameSupported;
	}
	public boolean isLanguageInGuiInstalled() {
		return isLanguageInGuiInstalled;
	}
	public boolean isLanguageInGuiSupported() {
		return isLanguageInGuiSupported;
	}
	
	/**
	 * Set language in game.
	 * 
	 * @param languageName
	 * @param suppressWarning If false, and if target language is invalid, will print an info message in chat line.
	 * @return If set language is valid.
	 */
	public static boolean setLanguageInGame(String languageName, boolean suppressWarning) 
	{
		if(nativeFunctionProvider.isLanguageSupported(languageName)) {
			isLanguageInGameSupported=true;
			if(nativeFunctionProvider.isLanguageInstalled(languageName)) {
				isLanguageInGameInstalled=true;
				languageInGame = languageName;
				return true;
			}else {
				isLanguageInGameInstalled = false;
				Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentTranslation(keyWarningKLInGameNotInstalled));
			}
		}else {
			isLanguageInGameSupported = false;
			Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentTranslation(keyWarningKLInGameNotSupported));
		}
		languageInGame = languageName;
		return false;
	}
	
	/**
	 * Set language in Gui.
	 * 
	 * @param languageName
	 * @param suppressWarning If false, and if target language is invalid, will print an info message in chat line.
	 * @return If set language is valid.
	 */
	public static boolean setLanguageInGui(String languageName, boolean suppressWarning) 
	{
		if(nativeFunctionProvider.isLanguageSupported(languageName)) {
			isLanguageInGuiSupported=true;
			if(nativeFunctionProvider.isLanguageInstalled(languageName)) {
				isLanguageInGuiInstalled=true;
				languageInGui = languageName;
				return true;
			}else {
				isLanguageInGuiInstalled = false;
				Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentTranslation(keyWarningKLInGuiNotInstalled));
			}
		}else {
			isLanguageInGuiSupported = false;
			Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentTranslation(keyWarningKLInGuiNotSupported));
		}
		languageInGui = languageName;
		return false;
	}
	
	/**
	 * Switch IME Language
	 * 
	 * @param switchTo true: switch to Language in game; false: switch to
	 *                 Language in Gui.
	 * @return If successfully switched. Switching may not be successful since
	 *         desired Language may have not been installed.
	 */
	public static boolean switchLanguageTo(boolean switchTo) {
		if (switchTo) {
			return switchLanguageTo(languageInGame);
		} else {
			return switchLanguageTo(languageInGui);
		}
	}
	
	public static boolean switchLanguageTo(String languageName) {
		if (nativeFunctionProvider.switchLanguageTo(languageName) == 0) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Info should be suppressed when ModFunctionManager is initializing to avoid null pointer error.
	 * 
	 * @param suppressInfo
	 * @return
	 */
	public static boolean reloadLanguageList(boolean suppressInfo) {
		if (nativeFunctionProvider.reloadSupportedLanguageList()){
			if (!suppressInfo) {
				/* Newly added to support list. */
				if (!isLanguageInGameSupported && nativeFunctionProvider.isLanguageSupported(languageInGame)) {
					Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentTranslation(keyInfoKLInGameSupported));
				}
				if (!isLanguageInGuiSupported && nativeFunctionProvider.isLanguageSupported(languageInGui)) {
					Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentTranslation(keyInfoKLInGuiSupported));
				}
			}
			if (nativeFunctionProvider.reloadInstalledLanguageList()) {
				if (!suppressInfo) {
					/* Newly installed. */
					if(!isLanguageInGameInstalled && nativeFunctionProvider.isLanguageInstalled(languageInGame)) {
						Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentTranslation(keyInfoKLInGameInstalled));
					}
					if(!isLanguageInGuiInstalled && nativeFunctionProvider.isLanguageInstalled(languageInGui)) {
						Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentTranslation(keyInfoKLInGuiInstalled));
					}
				}
				return true;
			}
		}
		return false;
	}
}
