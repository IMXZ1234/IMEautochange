package com.IMEautochange.nativefunction;

import java.util.HashMap;

public abstract class NativeFunctionProvider {
	public abstract int switchLanguageTo(String languageName);
	
	public abstract boolean reloadInstalledLanguageList();
	
	public abstract boolean reloadSupportedLanguageList();
	
	public abstract boolean isLanguageInstalled(String languageName);
	
	public abstract boolean isLanguageSupported(String languageName);
}
