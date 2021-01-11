package com.IMEautochange.jna;

import java.util.HashMap;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.win32.StdCallLibrary;

public class ModNativeMethods
{
	static final HashMap<String, Long> KLTable = new HashMap<String, Long>();
	static{
		KLTable.put("zh_cn", (long) 0x08040804);
		KLTable.put("en_us", (long) 0x04090409);
		KLTable.put("zh_tw", (long) 0x04040404);
		KLTable.put("ja_jp", (long) 0x04110411);
		KLTable.put("ko_kr", (long) 0x04120412);
	}
	static User32 libuser32 = User32.INSTANCE;
//	static Advapi32 libadvapi32 = Advapi32.INSTANCE;
	
	public static int gethKLListLen() {
		return libuser32.GetKeyboardLayoutList(0,null);
	}
	
	public static void gethKLList(int hKLnum, long[] hKLList) {
		libuser32.GetKeyboardLayoutList(hKLnum, hKLList);
	}
	
	public static void activateKL(long hKL) {
		libuser32.ActivateKeyboardLayout((int)hKL, 8);
	}
	
//	static boolean hasKLzh_cn=false;
//	static boolean hasKLen_us=false;
//	static final long hKLzh_cn=0x08040804;
//	static final long hKLen_us=0x04090409;
//	//Get keyboard layout information, e.g. if a certain keyboard layout such as zh_cn exists
//	public static void GethKLinfo() {
//		hKLnum=libuser32.GetKeyboardLayoutList(0,null);
//		hKLs=new long[hKLnum];
//		libuser32.GetKeyboardLayoutList(hKLnum,hKLs);
//		hasKLzh_cn=false;
//		hasKLen_us=false;
//		for(long hKL:hKLs) {
//			if (hKL==hKLzh_cn) {	//zh_cn keyboard layout
//				hasKLzh_cn=true;
//			}
//			if (hKL==hKLen_us) {	//en_us keyboard layout
//				hasKLen_us=true;
//			}
//		}
//	}
//	//Toggle IME
//	//If original keyboard layout is zh_cn, then change it to en_us
//	//If original keyboard layout is en_us, then change it to zh_cn
//	public static void ToggleIME() {
//		GethKLinfo();
//		char[] lpbuffer=new char[50];
//    	int i;
//    	int hkl=0;
//    	for(i=0;i<50;i++) {
//    		lpbuffer[i]=0;
//    	}
//    	if(hasKLzh_cn&&((long)libuser32.GetKeyboardLayout(0))!=hKLzh_cn) {
//    		hkl=libuser32.ActivateKeyboardLayout((int)hKLzh_cn,8);
//    	}else if(hasKLen_us&&((long)libuser32.GetKeyboardLayout(0))==hKLzh_cn) {
//    		hkl=libuser32.ActivateKeyboardLayout((int)hKLen_us,8);
//    	}
//    	
//		System.out.printf("last hKL = %x\n", hkl);
//		libuser32.GetKeyboardLayoutNameW(lpbuffer);
//		System.out.print("KeyboardLayoutName is ");
//		for(i=0;i<50;i++) {
//			if(lpbuffer[i]==0) {
//				break;
//			}else {
//				System.out.printf("%c",lpbuffer[i]);
//			}
//		}
//	}
//	//Set keyboard layout to zh_cn
//	public static void SetKLzh_cn() {
//		GethKLinfo();
//		if(hasKLzh_cn)
//    		libuser32.ActivateKeyboardLayout((int)hKLzh_cn,8);
//	}
//	//Set keyboard layout to en_us
//	public static void SetKLen_us() {
//		GethKLinfo();
//		if(hasKLen_us)
//    		libuser32.ActivateKeyboardLayout((int)hKLen_us,8);
//	}
}


