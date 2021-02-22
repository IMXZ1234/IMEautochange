package com.imeautochange.nativefunction.windows;

import java.util.HashMap;

import com.sun.jna.PointerType;

//import com.sun.jna.platform.win32.Guid.GUID;

public class COMInfo {
	public GUID.ByReference rclsid; 
	public int dwClsContext;
	PointerType pUnkOuter;
	public GUID.ByReference riid;
	public HashMap<String, Integer> offsetTable = new HashMap<String, Integer>();
	
	public final static int CLSCTX_INPROC_SERVER = 1;
	
	public COMInfo(GUID.ByReference rclsid, GUID.ByReference riid, HashMap<String, Integer> offsetTable) {
		this(rclsid, CLSCTX_INPROC_SERVER, null, riid, offsetTable);
	}

	public COMInfo(GUID.ByReference rclsid, int dwClsContext, GUID.ByReference riid,
			HashMap<String, Integer> offsetTable) {
		this(rclsid, dwClsContext, null, riid, offsetTable);
	}

	public COMInfo(GUID.ByReference rclsid, int dwClsContext, PointerType pUnkOuter, GUID.ByReference riid,
			HashMap<String, Integer> offsetTable) {
		this.rclsid = rclsid;
		this.dwClsContext = dwClsContext;
		this.pUnkOuter = pUnkOuter;
		this.riid = riid;
		this.offsetTable = offsetTable;
	}
	
	public COMInfo(GUID.ByReference rclsid, GUID.ByReference riid,
			String[] funcNames, int[] funcOffsets) {
		this(rclsid, CLSCTX_INPROC_SERVER, null, riid, funcNames, funcOffsets);
	}
	
	public COMInfo(GUID.ByReference rclsid, int dwClsContext, GUID.ByReference riid,
			String[] funcNames, int[] funcOffsets) {
		this(rclsid, dwClsContext, null, riid, funcNames, funcOffsets);
	}
	
	public COMInfo(GUID.ByReference rclsid, int dwClsContext, PointerType pUnkOuter, GUID.ByReference riid,
			String[] funcNames, int[] funcOffsets) {
		int funcNum = funcNames.length;
		if (funcOffsets.length < funcNum) {
			funcNum = funcOffsets.length;
		}
		this.rclsid = rclsid;
		this.dwClsContext = dwClsContext;
		this.pUnkOuter = pUnkOuter;
		this.riid = riid;
		this.offsetTable = new HashMap<String, Integer>();
		for (int i = 0; i < funcNum; i++) {
			offsetTable.put(funcNames[i], funcOffsets[i]);
		}
	}
}
