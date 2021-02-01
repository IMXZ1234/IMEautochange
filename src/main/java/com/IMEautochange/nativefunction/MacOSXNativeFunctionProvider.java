package com.IMEautochange.nativefunction;

import java.io.File;

public class MacOSXNativeFunctionProvider extends NativeFunctionProvider{

	@Override
	public int switchLanguageTo(String languageName) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean reloadInstalledLanguageList() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean reloadSupportedLanguageList(File supportedLanguageListDirFile) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isLanguageInstalled(String languageName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isLanguageSupported(String languageName) {
		// TODO Auto-generated method stub
		return false;
	}

}
