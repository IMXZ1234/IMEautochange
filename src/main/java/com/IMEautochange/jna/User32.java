package com.IMEautochange.jna;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.win32.StdCallLibrary;

public interface User32 extends StdCallLibrary { 
    // Method declarations, constant and structure definitions go here
	 User32 INSTANCE = (User32)Native.loadLibrary("user32",User32.class);
	 
	 boolean GetKeyboardLayoutNameW(char[] lpbuffer);
	 int ActivateKeyboardLayout(int hkl,int Flags);
	 int GetKeyboardLayout(int idThread);
	 
	 //fills at interval length of long, unexpectedly. but truth is that
	 int GetKeyboardLayoutList(int nBuff,long[] lpList);
}