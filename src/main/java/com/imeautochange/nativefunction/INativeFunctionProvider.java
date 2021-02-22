package com.imeautochange.nativefunction;

import java.util.ArrayList;

import com.imeautochange.config.IMEInfo;

public interface INativeFunctionProvider {
	
	public static final int RESULT_ERROR = 0x00000000;
	public static final int RESULT_OK = 0x00000001;
	public static final int RESULT_NOTINSTALLED = 0x00000002;
	
	
	public int switchIMETo(String imeName);
	public boolean reloadIMEList();
	public boolean getIMEInfoList(ArrayList<IMEInfo> imeInfoList);
	public boolean isIMEInstalled(String imeName);
	public boolean getDefaultIME(IMEInfo imeInfo);
	public int getEnglishIME(IMEInfo imeInfo);
}
