package com.IMEautochange.nativefunction;

import java.io.File;

public abstract class NativeFunctionProvider {
	public abstract int switchLanguageTo(String languageName);
	
	public abstract boolean reloadInstalledLanguageList();
	
	public abstract boolean reloadSupportedLanguageList(File supportedLanguageListDirFile);
	
	public abstract boolean isLanguageInstalled(String languageName);
	
	public abstract boolean isLanguageSupported(String languageName);
}
