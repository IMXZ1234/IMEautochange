package com.IMEautochange.nativefunction;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.platform.win32.WinDef;

public interface User32 extends StdCallLibrary 
{ 
    // Method declarations, constant and structure definitions go here
	 User32 INSTANCE = (User32)Native.loadLibrary("user32",User32.class);
	 
	 boolean GetKeyboardLayoutNameW(char[] lpbuffer);
	 int ActivateKeyboardLayout(int hkl,int Flags);
	 int GetKeyboardLayout(int idThread);
	 int GetForegroundWindow();
	 int GWL_EXSTYLE = -20;
	 int GWL_STYLE = -16;
	 int WS_EX_TOPMOST = 8;
	 int GetWindowLongW(int hWnd, int nIndex);
	 int SetWindowLongW(int hWnd, int nIndex, int dwNewLong);
	 //fills at interval length of long, unexpectedly. but truth is that
	 int GetKeyboardLayoutList(int nBuff,long[] lpList);
	 public static int HWND_TOP = (int) 0;
	 int SWP_NOMOVE = (int) 0x0002;
	 int SWP_NOSIZE = (int) 0x0001;
	 int SWP_NOACTIVATE = (int) 0x0010;
	 boolean SetWindowPos(int hWnd, int hWndInsertAfter, int x, int y, int cx, int cy, int uFlags);
}