package com.IMEautochange.nativefunction;

import java.util.HashMap;

public final class WindowsNativeFunctionProvider extends NativeFunctionProvider {
	public static final HashMap<String, Long> KLTable = new HashMap<String, Long>();
	static{
		KLTable.put("zh_cn", (long) 0x08040804);
		KLTable.put("en_us", (long) 0x04090409);
		KLTable.put("zh_tw", (long) 0x04040404);
		KLTable.put("ja_jp", (long) 0x04110411);
		KLTable.put("ko_kr", (long) 0x04120412);
	}
	private static User32 libuser32 = User32.INSTANCE;
//	private static Imm32 libimm32 = Imm32.INSTANCE;
//	private static int hWnd = 0;
	private static long[] hKLList;
	private static int hKLnum;
	
	/**
	 * Switch IME language to language specified by languageName.
	 * 
	 * @return 0 if success; 1 if not installed; 2 if not supported; 3 if failed
	 *         unexpectedly.
	 */
	@Override
	public int switchLanguageTo(String languageName) {
		Long hKL = KLTable.get(languageName);
		if (hKL != null) {
			if (isLanguageInstalled(hKL)) {
				if (libuser32.ActivateKeyboardLayout((int) hKL.longValue(), 8) != 0) {
					return 0;
				} else {
					return 3;
				}
			} else {
				return 1;
			}
		} else {
			return 2;
		}
	}

	@Override
	public boolean reloadInstalledLanguageList() {
		hKLnum = libuser32.GetKeyboardLayoutList(0,null);
		hKLList = new long[hKLnum];
		/* If buffer size is enough. */
		return (libuser32.GetKeyboardLayoutList(hKLnum, hKLList) == hKLnum);
	}

	@Override
	public boolean reloadSupportedLanguageList() {
		// TODO Auto-generated method stub
		return true;
	}
	
	/**
	 * Check if given language is installed in OS.
	 * 
	 * @param languageName
	 * @return If is installed.
	 */
	@Override
	public boolean isLanguageInstalled(String languageName) {
		Long hKL = KLTable.get(languageName);
		/* Keyboard Layout not supported. */
		if (hKL == null) {
			return false;
		}
		return isLanguageInstalled(hKL);
	}
	
	private boolean isLanguageInstalled(long hKL) {
		boolean hasKL = false;
		for (int i = 0; i < hKLnum; i++) {
			if (hKLList[i] == hKL) {
				hasKL = true;
			}
		}
		return hasKL;
	}

	@Override
	public boolean isLanguageSupported(String languageName) {
		return KLTable.containsKey(languageName);
	}
}
