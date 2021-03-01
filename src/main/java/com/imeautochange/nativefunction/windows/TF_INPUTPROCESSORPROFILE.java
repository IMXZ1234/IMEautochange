package com.imeautochange.nativefunction.windows;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Structure;

public class TF_INPUTPROCESSORPROFILE extends Structure {
	public static final int TF_PROFILETYPE_INPUTPROCESSOR = 1;
	public static final int TF_PROFILETYPE_KEYBOARDLAYOUT = 2;
	public static final int TF_IPP_CAPS_DISABLEONTRANSITORY = 1;
	public static final int TF_IPP_CAPS_SECUREMODESUPPORT = 2;
	public static final int TF_IPP_CAPS_UIELEMENTENABLED = 4;
	public static final int TF_IPP_CAPS_COMLESSSUPPORT = 8;
	public static final int TF_IPP_CAPS_WOW16SUPPORT = 0x10;
	public static final int TF_IPP_CAPS_IMMERSIVESUPPORT = 0x1000;
	public static final int TF_IPP_CAPS_SYSTRAYSUPPORT = 0x20000;
	public static final int TF_IPP_FLAG_ACTIVE = 1;
	public static final int TF_IPP_FLAG_ENABLED = 2;
	public static final int TF_IPP_FLAG_SUBSTITUTEDBYINPUTPROCESSOR = 4;
	
	public static final Win32Util.BitFlag[] dwProfileTypeBitFlags = new Win32Util.BitFlag[] {
			new Win32Util.BitFlag("TF_PROFILETYPE_INPUTPROCESSOR", TF_PROFILETYPE_INPUTPROCESSOR),
			new Win32Util.BitFlag("TF_PROFILETYPE_KEYBOARDLAYOUT", TF_PROFILETYPE_KEYBOARDLAYOUT)
	};
	public static final Win32Util.BitFlag[] dwCapsBitFlags = new Win32Util.BitFlag[] {
			new Win32Util.BitFlag("TF_IPP_CAPS_DISABLEONTRANSITORY", TF_IPP_CAPS_DISABLEONTRANSITORY),
			new Win32Util.BitFlag("TF_IPP_CAPS_SECUREMODESUPPORT", TF_IPP_CAPS_SECUREMODESUPPORT),
			new Win32Util.BitFlag("TF_IPP_CAPS_UIELEMENTENABLED", TF_IPP_CAPS_UIELEMENTENABLED),
			new Win32Util.BitFlag("TF_IPP_CAPS_COMLESSSUPPORT", TF_IPP_CAPS_COMLESSSUPPORT),
			new Win32Util.BitFlag("TF_IPP_CAPS_WOW16SUPPORT", TF_IPP_CAPS_WOW16SUPPORT),
			new Win32Util.BitFlag("TF_IPP_CAPS_IMMERSIVESUPPORT", TF_IPP_CAPS_IMMERSIVESUPPORT),
			new Win32Util.BitFlag("TF_IPP_CAPS_SYSTRAYSUPPORT", TF_IPP_CAPS_SYSTRAYSUPPORT)
	};
	public static final Win32Util.BitFlag[] dwFlagsBitFlags = new Win32Util.BitFlag[] {
			new Win32Util.BitFlag("TF_IPP_FLAG_ACTIVE", TF_IPP_FLAG_ACTIVE),
			new Win32Util.BitFlag("TF_IPP_FLAG_ENABLED", TF_IPP_FLAG_ENABLED),
			new Win32Util.BitFlag("TF_IPP_FLAG_SUBSTITUTEDBYINPUTPROCESSOR", TF_IPP_FLAG_SUBSTITUTEDBYINPUTPROCESSOR)
	};
	
	public int dwProfileType;
	public short langid;
	public GUID clsid;
	public GUID guidProfile;
	public GUID catid;
	public long hklSubstitute;
	public int dwCaps;
	public long hkl;
	public int dwFlags;
	
public static class ByReference extends TF_INPUTPROCESSORPROFILE implements Structure.ByReference { }
	
	@Override
	protected List<String> getFieldOrder() {
		return Arrays.asList(
				"dwProfileType", 
				"langid", 
				"clsid", 
				"guidProfile", 
				"catid", 
				"hklSubstitute", 
				"dwCaps",
				"hkl",
				"dwFlags");
	}
	
	public static String getStringRepresentation(TF_INPUTPROCESSORPROFILE ipp) {
		return String.format("dwProfileType: \t\t%s\nlangid: \t\t%04X\nclsid: \t\t\t%s\nguidProfile: \t\t%s\ncatid: \t\t\t%s\nhklSubstitute: \t\t%016X\ndwCaps: \t\t%s\nhkl: \t\t\t%016X\ndwFlags: \t\t%s\n",
				Win32Util.flagsToString(ipp.dwProfileType, dwProfileTypeBitFlags),
				(short)ipp.langid,
				GUID.getStringRepresentation(ipp.clsid),
				GUID.getStringRepresentation(ipp.guidProfile),
				GUID.getStringRepresentation(ipp.catid),
				ipp.hklSubstitute,
				Win32Util.flagsToString(ipp.dwCaps, dwCapsBitFlags),
				ipp.hkl,
				Win32Util.flagsToString(ipp.dwFlags, dwFlagsBitFlags));
	}
}
