package com.imeautochange.nativefunction.windows;

import com.sun.jna.Native;
import com.sun.jna.PointerType;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.win32.StdCallLibrary;

public interface Ole32 extends StdCallLibrary {
	Ole32 INSTANCE = (Ole32) Native.loadLibrary("ole32", Ole32.class);
	int CoCreateInstance(
			  GUID.ByReference  rclsid,
			  PointerType pUnkOuter,
			  int     dwClsContext,
			  GUID.ByReference    iID_ITfInputProcessorProfiles,
			  PointerByReference    ppv
			);
	int CoInitialize(int pvReserved);
	void CoUninitialize();
}
