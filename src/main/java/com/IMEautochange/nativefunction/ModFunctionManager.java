package com.IMEautochange.nativefunction;

import java.io.File;

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
	private static boolean isLanguageInGameInstalled = false;
	private static boolean isLanguageInGuiInstalled = false;
	private static boolean isLanguageInGameSupported = false;
	private static boolean isLanguageInGuiSupported = false;
	private static File supportedLanguageListDirFile;
	
	private static int platform;
	private static NativeFunctionProvider nativeFunctionProvider;
	
	public static void getNativeFunctionProvider(){
		platform = OSChecker.getPlatform();
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
			IMEautochange.logger.error("Unsupported Platform: " + OSChecker.getPlatformFullName());
			nativeFunctionProvider = null;
		}
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
	public static void init(String languageInGameIn, String languageInGuiIn, File baseDir) {
		languageInGame = languageInGameIn;
		languageInGui = languageInGuiIn;
		File dir = new File(baseDir, "ime_support");
		switch (platform) {
		case OSChecker.PLATFORM_WINDOWS:
			supportedLanguageListDirFile = new File(dir, "windows");
			break;
		case OSChecker.PLATFORM_LINUX:
			supportedLanguageListDirFile = new File(dir, "linux");
			break;
		case OSChecker.PLATFORM_MACOSX:
			supportedLanguageListDirFile = new File(dir, "macosx");
			break;
		default:
			supportedLanguageListDirFile = null;
			return;
		}
		reloadLanguageList(true);
//		isLanguageInGameInstalled = nativeFunctionProvider.isLanguageInstalled(languageInGame);
//		isLanguageInGuiInstalled = nativeFunctionProvider.isLanguageInstalled(languageInGui);
//		isLanguageInGameSupported = nativeFunctionProvider.isLanguageSupported(languageInGame);
//		isLanguageInGuiSupported = nativeFunctionProvider.isLanguageSupported(languageInGui);
	}
	
	public static boolean isLanguageInGameInstalled() {
		return isLanguageInGameInstalled;
	}
	public static boolean isLanguageInGameSupported() {
		return isLanguageInGameSupported;
	}
	public static boolean isLanguageInGuiInstalled() {
		return isLanguageInGuiInstalled;
	}
	public static boolean isLanguageInGuiSupported() {
		return isLanguageInGuiSupported;
	}
	
	/**
	 * Set language in game.
	 * 
	 * @param languageName
	 * @param suppressWarning If false, and if target language is invalid, will print an info message in chat line.
	 * @return If set language is valid.
	 */
	public static boolean setLanguageInGame(String languageName, boolean suppressWarning) {
		if (nativeFunctionProvider.isLanguageSupported(languageName)) {
			isLanguageInGameSupported = true;
			if (nativeFunctionProvider.isLanguageInstalled(languageName)) {
				isLanguageInGameInstalled = true;
				languageInGame = languageName;
				return true;
			} else {
				isLanguageInGameInstalled = false;
				if (!suppressWarning)
					Minecraft.getMinecraft().ingameGUI.getChatGUI()
							.printChatMessage(new TextComponentTranslation(keyWarningKLInGameNotInstalled));
			}
		} else {
			isLanguageInGameSupported = false;
			if (!suppressWarning)
				Minecraft.getMinecraft().ingameGUI.getChatGUI()
						.printChatMessage(new TextComponentTranslation(keyWarningKLInGameNotSupported));
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
	public static boolean setLanguageInGui(String languageName, boolean suppressWarning) {
		if (nativeFunctionProvider.isLanguageSupported(languageName)) {
			isLanguageInGuiSupported = true;
			if (nativeFunctionProvider.isLanguageInstalled(languageName)) {
				isLanguageInGuiInstalled = true;
				languageInGui = languageName;
				return true;
			} else {
				isLanguageInGuiInstalled = false;
				if (!suppressWarning)
					Minecraft.getMinecraft().ingameGUI.getChatGUI()
							.printChatMessage(new TextComponentTranslation(keyWarningKLInGuiNotInstalled));
			}
		} else {
			isLanguageInGuiSupported = false;
			if (!suppressWarning)
				Minecraft.getMinecraft().ingameGUI.getChatGUI()
						.printChatMessage(new TextComponentTranslation(keyWarningKLInGuiNotSupported));
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
	 * Info should be suppressed when ModFunctionManager is initializing to avoid {@link NullPointerException} .
	 * 
	 * @param suppressInfo
	 * @return
	 */
	public static boolean reloadLanguageList(boolean suppressInfo) {
		if (nativeFunctionProvider.reloadSupportedLanguageList(supportedLanguageListDirFile)) {
//			System.out.println("reload support"+true);
//			System.out.println("isLanguageInGameSupported"+isLanguageInGameSupported);
//			System.out.println("isLanguageInGuiSupported"+isLanguageInGuiSupported);
			/* Newly added to support list. */
			if (!isLanguageInGameSupported && nativeFunctionProvider.isLanguageSupported(languageInGame)) {
				if (!suppressInfo)
					Minecraft.getMinecraft().ingameGUI.getChatGUI()
							.printChatMessage(new TextComponentTranslation(keyInfoKLInGameSupported));
				isLanguageInGameSupported = true;
			}
			if (!isLanguageInGuiSupported && nativeFunctionProvider.isLanguageSupported(languageInGui)) {
				if (!suppressInfo)
					Minecraft.getMinecraft().ingameGUI.getChatGUI()
							.printChatMessage(new TextComponentTranslation(keyInfoKLInGuiSupported));
				isLanguageInGuiSupported = true;
			}
			if (nativeFunctionProvider.reloadInstalledLanguageList()) {
//				System.out.println("reload install"+true);
//				System.out.println("isLanguageInGameInstalled"+isLanguageInGameInstalled);
//				System.out.println("isLanguageInGuiInstalled"+isLanguageInGuiInstalled);
				/* Newly installed. */
				if (!isLanguageInGameInstalled && nativeFunctionProvider.isLanguageInstalled(languageInGame)) {
					if (!suppressInfo)
						Minecraft.getMinecraft().ingameGUI.getChatGUI()
								.printChatMessage(new TextComponentTranslation(keyInfoKLInGameInstalled));
					isLanguageInGameInstalled = true;
				}
				if (!isLanguageInGuiInstalled && nativeFunctionProvider.isLanguageInstalled(languageInGui)) {
					if (!suppressInfo)
						Minecraft.getMinecraft().ingameGUI.getChatGUI()
								.printChatMessage(new TextComponentTranslation(keyInfoKLInGuiInstalled));
					isLanguageInGuiInstalled = true;
				}
				return true;
			}
			return false;
		}
		return false;
	}
}
