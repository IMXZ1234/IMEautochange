package com.imeautochange.nativefunction.windows;

import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.ptr.ShortByReference;

public interface COMInterfaceITfInputProcessorProfiles extends COMInterface{
	public static final GUID.ByReference IID_ITfInputProcessorProfiles = new GUID.ByReference(
			new byte[] { 0x1F, 0x02, (byte) 0xB6, (byte) 0xC5, 0x78, 0x42, 0x4E, (byte) 0xE6, (byte) 0x8A, 0x0B,
					(byte) 0x9A, 0x24, 0x18, 0x3A, (byte) 0x95, (byte) 0xCA });
	public static final GUID.ByReference CLSID_TF_InputProcessorProfiles = new GUID.ByReference(
			new byte[] { 0x33,(byte) 0xC5,0x3A,0x50, (byte) 0xF4,0x56, 0x48,(byte) 0x84, (byte) 0xB0, 0x49,
					(byte) 0x85, (byte) 0xFD, 0x64, 0x3E, (byte) 0xCF, (byte) 0xED });
	
	public static COMInterfaceITfInputProcessorProfiles INSTANCE = 
			(COMInterfaceITfInputProcessorProfiles) COMHelper.loadInterface(COMInterfaceITfInputProcessorProfiles.class, 
					new COMInfo(CLSID_TF_InputProcessorProfiles, IID_ITfInputProcessorProfiles,
							new String[] {"GetLanguageList","IsEnabledLanguageProfile","GetCurrentLanguage",
									"ChangeCurrentLanguage","ActivateLanguageProfile","EnableLanguageProfile"},
							new int[] {15,18,13,
									14,10,17}));

	public int GetLanguageList(PointerByReference ppLangId, IntByReference pulCount);
	public int IsEnabledLanguageProfile(GUID clsid, short langid, GUID guidProfile,IntByReference pfEnable);
	public int GetCurrentLanguage(ShortByReference langid);
	public int ChangeCurrentLanguage(short langid);
	public int ActivateLanguageProfile(GUID clsid, short langid, GUID guidProfile);
	public int EnableLanguageProfile(GUID clsid, short langid, GUID guidProfile,boolean b);
}
