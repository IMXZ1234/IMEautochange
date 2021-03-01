package com.imeautochange.nativefunction.windows;

import com.sun.jna.ptr.PointerByReference;

public interface COMInterfaceITfInputProcessorProfileMgr extends COMInterface{
	public static final int TF_IPPMF_FORPROCESS = 0x10000000;
	public static final int TF_IPPMF_FORSESSION = 0x20000000;
	public static final int TF_IPPMF_ENABLEPROFILE = 1;
	public static final int TF_IPPMF_DISABLEPROFILE = 2;
	public static final int TF_IPPMF_DONTCARECURRENTINPUTLANGUAGE = 4;
	
	public static final GUID.ByReference IID_ITfInputProcessorProfileMgr = new GUID.ByReference("71c6e74c-0f28-11d8-a82a-00065b84435c");
	
	public static COMInterfaceITfInputProcessorProfileMgr INSTANCE = 
			(COMInterfaceITfInputProcessorProfileMgr) COMHelper.loadInterfaceByQuery(COMInterfaceITfInputProcessorProfileMgr.class, COMInterfaceITfInputProcessorProfiles.INSTANCE, 
					new COMInfo(null, IID_ITfInputProcessorProfileMgr,
							new String[] {"ActivateProfile","GetProfile","EnumProfiles","GetActiveProfile"},
							new int[] {3,5,6,10}));
	
	public int EnumProfiles(short langid, PointerByReference ppEnum);
	public int GetActiveProfile(GUID catid, TF_INPUTPROCESSORPROFILE pProfile);	
	public int GetProfile(int dwProfileType, short langid, GUID clsid, GUID guidProfile, long hkl, TF_INPUTPROCESSORPROFILE pProfile);
	public int ActivateProfile(int dwProfileType, short langid, GUID clsid, GUID guidProfile, long hkl, int dwFlags);
}
