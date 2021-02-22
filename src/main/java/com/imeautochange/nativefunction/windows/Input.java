package com.imeautochange.nativefunction.windows;

import com.sun.jna.Native;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.StdCallLibrary;

public interface Input extends StdCallLibrary {
	Input INSTANCE=(Input)Native.loadLibrary("input",Input.class);
	int EnumEnabledLayoutOrTip(char[] pszUserReg, char[] pszSystemReg, char[] pszSoftwareReg, LAYOUTORTIPPROFILE[] pLayoutOrTipProfile, int uBufLength);
	int GetLayoutDescription(char[] szId, char[] pszName, IntByReference uBufLength, int dwFlags);

}
