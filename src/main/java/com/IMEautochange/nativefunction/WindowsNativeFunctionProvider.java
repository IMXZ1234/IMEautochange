package com.IMEautochange.nativefunction;

import java.io.File;
import java.util.HashMap;

import com.IMEautochange.util.JsonUtil;

public final class WindowsNativeFunctionProvider extends NativeFunctionProvider {
	public static final HashMap<String, Long> KLTable = new HashMap<String, Long>();
	static{
		KLTable.put("zh_cn", (long) 0x00000804);
		KLTable.put("en_us", (long) 0x00000409);
		KLTable.put("zh_tw", (long) 0x00000404);
		KLTable.put("ja_jp", (long) 0x00000411);
		KLTable.put("ko_kr", (long) 0x00000412);
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
				if (libuser32.ActivateKeyboardLayout(hKL.intValue() & 0xffff, 8) != 0) {
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
//		int ret = libuser32.GetKeyboardLayoutList(hKLnum, hKLList);
//		System.out.print("ret"+ret);
//		System.out.print("KLList");
//		for (int i=0;i<hKLnum;i++) {
//			System.out.print(hKLList[i]+"\t");
//		}
//		System.out.print("\n");
		/* If buffer size is enough. */
		return (libuser32.GetKeyboardLayoutList(hKLnum, hKLList) == hKLnum);
	}

	@Override
	public boolean reloadSupportedLanguageList(File supportedLanguageListDirFile) {
		if(!supportedLanguageListDirFile.exists()) {
			supportedLanguageListDirFile.mkdirs();
		}
		File supportedLanguageListFile = new File(supportedLanguageListDirFile, "language_list.json");
		/* Outputs the file only if it doesn't exist. 
		 * Built-in (language id-hKL) pairs will remain after reloading
		 * unless their hKLs are modified.*/
		if(!supportedLanguageListFile.exists()) {
//			System.out.println("file not exist created");
//			System.out.println("before" + KLTable);
			JsonUtil.saveHashMapToFile(KLTable, supportedLanguageListFile);
//			System.out.println("after" + KLTable);
			return true;
		}else {			
//			System.out.println("loaded table");
//			System.out.println("before" + KLTable);
			JsonUtil.loadHashMapfromFile(KLTable, supportedLanguageListFile);
//			System.out.println("after" + KLTable);
			return true; 
		}
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
//		System.out.println("hKL"+hKL);
		/* Keyboard Layout not supported. */
		if (hKL == null) {
			return false;
		}
		return isLanguageInstalled(hKL);
	}
	
	private boolean isLanguageInstalled(long hKL) {
		boolean hasKL = false;
		for (int i = 0; i < hKLnum; i++) {
			if (((hKLList[i] - hKL) & 0xffff) == 0) {
				hasKL = true;
			}
		}
//		System.out.println("hasKL"+hasKL);
		return hasKL;
	}

	@Override
	public boolean isLanguageSupported(String languageName) {
		return KLTable.containsKey(languageName);
	}
}
