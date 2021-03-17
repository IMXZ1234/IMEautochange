package com.imeautochange.nativefunction.windows;

import com.sun.jna.Native;
import com.sun.jna.PointerType;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.win32.StdCallLibrary;

public interface Ole32 extends StdCallLibrary {
	public static final int COINIT_APARTMENTTHREADED = 0x2;      // Apartment model
	// These constants are only valid on Windows NT 4.0
	public static final int COINITBASE_MULTITHREADED = 0;
	public static final int COINIT_MULTITHREADED = COINITBASE_MULTITHREADED;
	public static final int COINIT_DISABLE_OLE1DDE = 0x4; // Don't use DDE for Ole1 support.
	public static final int COINIT_SPEED_OVER_MEMORY = 0x8; // Trade memory for speed.
	Ole32 INSTANCE = (Ole32) Native.loadLibrary("ole32", Ole32.class);
	int CoCreateInstance(
			  GUID.ByReference  rclsid,
			  PointerType pUnkOuter,
			  int     dwClsContext,
			  GUID.ByReference    iID_ITfInputProcessorProfiles,
			  PointerByReference    ppv
			);
	int CoInitialize(int pvReserved);
	int CoInitializeEx(int pvReserved, int  dwCoInit);
	void CoUninitialize();
}
