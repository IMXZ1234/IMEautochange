package com.imeautochange.nativefunction.windows;

import com.sun.jna.Native;
import com.sun.jna.win32.StdCallLibrary;

public interface Imm32 extends StdCallLibrary 
{ 
    // Method declarations, constant and structure definitions go here
	Imm32 INSTANCE = (Imm32)Native.loadLibrary("Imm32",Imm32.class);
	
	int ImmGetDefaultIMEWnd(int hWnd);
}