package com.imeautochange.nativefunction.windows;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Structure;

public class LAYOUTORTIPPROFILE extends Structure {
	public static final int LAYOUTORTIPPROFILE_SIZE = 584;
	
	public static final int LOTP_INPUTPROCESSOR = 1;
	public static final int LOTP_KEYBOARDLAYOUT = 2;
	public static final int LOT_DEFAULT = 1;
	public static final int LOT_DISABLED = 2;
	
	public static final Win32Util.BitFlag[] dwProfileTypeBitFlags = new Win32Util.BitFlag[] {
			new Win32Util.BitFlag("LOTP_INPUTPROCESSOR", 1),
			new Win32Util.BitFlag("LOTP_KEYBOARDLAYOUT", 2)
	};
	public static final Win32Util.BitFlag[] dwFlagsBitFlags = new Win32Util.BitFlag[] {
			new Win32Util.BitFlag("LOT_DEFAULT", 1),
			new Win32Util.BitFlag("LOT_DISABLED", 2),
	};
	
	public int dwProfileType;
	public short langid;
	public GUID clsid;
	public GUID guidProfile;
	public GUID catid;
	public int dwSubstituteLayout;
	public int dwFlags;
	public char[] szId = new char[260];
	
	public static class ByReference extends LAYOUTORTIPPROFILE implements Structure.ByReference { }
	
	@Override
	protected List<String> getFieldOrder() {
		return Arrays.asList(
				"dwProfileType", 
				"langid", 
				"clsid", 
				"guidProfile", 
				"catid", 
				"dwSubstituteLayout", 
				"dwFlags", 
				"szId");
	}

	public static String getStringRepresentation(LAYOUTORTIPPROFILE lotp) {
		return String.format("dwProfileType: \t\t%s\nlangid: \t\t%04X\nclsid: \t\t\t%s\nguidProfile: \t\t%s\ncatid: \t\t\t%s\ndwSubstituteLayout: \t%08X\ndwFlags: \t\t%s\nszId: \t\t\t%s\n",
				Win32Util.flagsToString(lotp.dwProfileType, dwProfileTypeBitFlags),
				(short)lotp.langid,
				GUID.getStringRepresentation(lotp.clsid),
				GUID.getStringRepresentation(lotp.guidProfile),
				GUID.getStringRepresentation(lotp.catid),
				lotp.dwSubstituteLayout,
				Win32Util.flagsToString(lotp.dwFlags, dwFlagsBitFlags),
				String.valueOf(lotp.szId));
	}
}
