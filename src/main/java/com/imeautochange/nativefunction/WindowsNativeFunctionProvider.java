package com.imeautochange.nativefunction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.imeautochange.config.IMEInfo;
import com.imeautochange.nativefunction.windows.COMHelper;
import com.imeautochange.nativefunction.windows.COMInterface;
import com.imeautochange.nativefunction.windows.COMInterfaceITfInputProcessorProfiles;
import com.imeautochange.nativefunction.windows.Input;
import com.imeautochange.nativefunction.windows.LAYOUTORTIPPROFILE;
import com.imeautochange.nativefunction.windows.Win32Util;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.ShortByReference;

/**
 * This class provides native functionality under Windows platform.
 * Implemented using JNA to avoid including extra glue dlls.
 * Microsoft provides only scraps of information on MSDN about its mysterious IMEs, and
 * those related terms on official documents are quite convoluted and lacks explanation.
 * Here I will try to give some basic descriptions based on my own understanding and help you set up
 * a crude image of Windows IME.
 * A common-sense IME under Windows should be defined as one item displayed on the LangBar 
 * at the right-down corner of your screen. However, quite contradictory to our common sense,
 * these common-sense IMEs can further be divided into two categories, one is <b>Keyboard Layout(not IME)</b>, the other
 * is <b>Input Processor(real IME)</b>. Each keyboard layout is specified by a KLID, and each input processor is specified 
 * by its GUIDs. An input processor is expected to be working under a specified keyboard layout.
 * A keyboard layout is associated with a certain language, and determines how keyboard actions generate character values. 
 * It must be clarified that keyboard layout is <b>NOT</b> your physical keyboard layout(the way your keys are arranged on your
 * keyboard hardware), but is a system image of your physical keyboard layout according to which
 * Windows will interpret your key strokes. In other word, a keyboard layout is a physical keyboard layout 
 * Windows <b>expect you to have</b>. If you are using a common US physical keyboard layout,
 * you may surprisingly find out that when you press \ you will get # generated. I think it is
 * because UK physical keyboard layout has key # where your key \ lies. Officially, Microsoft claims that
 * <i>"The user can associate any input language with a given physical layout. 
 * For example, an English-speaking user who very occasionally works in French can set the input language 
 * of the keyboard to French without changing the physical layout of the keyboard. 
 * This means the user can enter text in French using the familiar English layout."</i>
 * An input processor, on the other hand, is responsible for generating new characters(for example 
 * Chinese characters which is not able to be generated directly by pressing a key due to the physical structure of your keyboard!) 
 * from the characters you type by pressing a key on your keyboard, and owns the usually seen candidate window(the one that
 * pops up when you type in a character) etc.
 * Due to Windows' historical issue, at present all common-sense IMEs, or say, input processors
 * are handled under two separate frameworks, namely the older IMM(Input Method Manager), and
 * the newer TSF(Text Service Framework). IMEs developed under IMM can work under TSF but the 
 * reverse is untrue. IMM's functions are mostly provided in Imm32.dll, under the form of
 * traditional Win32 API, while TSF's functions are mostly provided by COM Interfaces.
 * As Microsoft encourages usage of its new TSF framework, which is able to cover a far wider range of 
 * text input facilities(voice input etc.), some newly developed IMEs(SouGou PinYin etc.) are going under TSF.
 * At the same time, a bunch of IMM functions are starting to lose function after Window 7, although they are still
 * declared as functional on MSDN. Therefore, our program should use TSF functions instead IMM ones
 * to manipulate IMEs.
 * For more information about related jargon, see <a href="http://archives.miloush.net/michkap/archive/2004/11/27/270931.html">Some keyboarding terms</a>
 * 
 * @author IMXZ
 *
 */
public final class WindowsNativeFunctionProvider implements INativeFunctionProvider {
	private static HashMap<String, LAYOUTORTIPPROFILE> profileTable;
	private static LAYOUTORTIPPROFILE defaultProfile;
	private static String defaultProfileName;
	private static LAYOUTORTIPPROFILE englishProfile;
	private static String englishProfileName;
	
	public static WindowsNativeFunctionProvider INSTANCE = new WindowsNativeFunctionProvider();
	
	private WindowsNativeFunctionProvider() {
		COMHelper.initCOM();
		profileTable = new HashMap<String, LAYOUTORTIPPROFILE>();
		defaultProfile = null;
		defaultProfileName = null;
		englishProfile = null;
		englishProfileName = null;
		reloadIMEList();
	}
	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		COMHelper.uninitCOM();
	}

	@Override
	public int switchIMETo(String imeName) {
		LAYOUTORTIPPROFILE profile = profileTable.get(imeName);
		if (profile == null) {
			return RESULT_NOTINSTALLED;
		}
		int hResult;
		System.out.println("profile.langid "+profile.langid);
		ShortByReference langid = new ShortByReference();
		IntByReference bEnable = new IntByReference();
		
		hResult = COMInterfaceITfInputProcessorProfiles.INSTANCE.IsEnabledLanguageProfile(profile.clsid, profile.langid, profile.guidProfile, bEnable);
		if(hResult!=COMInterface.S_OK) {
			System.out.println("IsEnabledLanguageProfile error");
			return RESULT_ERROR;
		}else {
			System.out.println("IsEnabledLanguageProfile "+bEnable.getValue());
		}
		hResult = COMInterfaceITfInputProcessorProfiles.INSTANCE.GetCurrentLanguage(langid);
		System.out.println("IsEnabledLanguageProfile hResult "+hResult);
		if(hResult!=COMInterface.S_OK) {
			System.out.println("GetCurrentLanguage error");
			return RESULT_ERROR;
		}else {
			System.out.println("GetCurrentLanguage " + langid.getValue());
		}
        if (profile.langid != langid.getValue()) {
        	hResult = COMInterfaceITfInputProcessorProfiles.INSTANCE.ChangeCurrentLanguage(profile.langid);
        	if(hResult!=COMInterface.S_OK) {
    			System.out.println("ChangeCurrentLanguage error");
    			return RESULT_ERROR;
    		}
        }
        if (bEnable.getValue() != 0) {
        	hResult = COMInterfaceITfInputProcessorProfiles.INSTANCE.ActivateLanguageProfile(profile.clsid, profile.langid, profile.guidProfile);
        	if(hResult!=COMInterface.S_OK) {
    			System.out.println("ActivateLanguageProfile error");
    			return RESULT_ERROR;
    		}
        }
        else {
        	hResult = COMInterfaceITfInputProcessorProfiles.INSTANCE.EnableLanguageProfile(profile.clsid, profile.langid, profile.guidProfile, true);
        	if(hResult!=COMInterface.S_OK) {
    			System.out.println("EnableLanguageProfile error");
    			return RESULT_ERROR;
    		}
        	hResult = COMInterfaceITfInputProcessorProfiles.INSTANCE.ActivateLanguageProfile(profile.clsid, profile.langid, profile.guidProfile);
        	if(hResult!=COMInterface.S_OK) {
    			System.out.println("ActivateLanguageProfile error");
    			return RESULT_ERROR;
    		}
        }
		return RESULT_OK;
	}

	/**
	 * Directly add profiles of input processors to profileTable, but only add
	 * profiles of keyboard layouts to profileTable if no input processor profile is
	 * present for this language, due to the fact that common-sense IMEs of a
	 * certain language are either all keyboard layouts or all input processors.
	 * Only add those enabled.
	 * 
	 * @see LAYOUTORTIPPROFILE#TF_IPP_FLAG_ENABLED
	 */
	@Override
	public boolean reloadIMEList() {
		LAYOUTORTIPPROFILE lpLayoutOrTipProfile[];
		
		int num = Input.INSTANCE.EnumEnabledLayoutOrTip(null, null, null, null, 0);
		lpLayoutOrTipProfile = (LAYOUTORTIPPROFILE[])new LAYOUTORTIPPROFILE().toArray(num);
		Input.INSTANCE.EnumEnabledLayoutOrTip(null, null, null, lpLayoutOrTipProfile, LAYOUTORTIPPROFILE.LAYOUTORTIPPROFILE_SIZE * num);
		
		HashSet<Short> ipLangIdSet = new HashSet<Short>();
		HashMap<String, LAYOUTORTIPPROFILE> keyboardLayoutProfileTable = new HashMap<String, LAYOUTORTIPPROFILE>();
		char pszName[] = new char[64];
		IntByReference uBufLength = new IntByReference(64);
		for(int i=0;i<num;i++) {
			System.out.println(LAYOUTORTIPPROFILE.getStringRepresentation(lpLayoutOrTipProfile[i]));
			Input.INSTANCE.GetLayoutDescription(lpLayoutOrTipProfile[i].szId, pszName, uBufLength, 0);
			System.out.println(String.valueOf(pszName)+"\n");
			String name = String.valueOf(pszName).trim();
			if (lpLayoutOrTipProfile[i].dwProfileType == LAYOUTORTIPPROFILE.LOTP_KEYBOARDLAYOUT) {
				keyboardLayoutProfileTable.put(name, lpLayoutOrTipProfile[i]);
			} else {
				if((lpLayoutOrTipProfile[i].dwFlags & LAYOUTORTIPPROFILE.LOT_DEFAULT) != 0) {
					defaultProfile = lpLayoutOrTipProfile[i];
					defaultProfileName = name;
					// It seems that if a certain IME is LOT_DEFAULT, EnumEnabledLayoutOrTip will not check its LOT_DISABLED flag.
					// The LOT_DISABLED flag is always reset on return. 
					IntByReference bEnable = new IntByReference();
					COMInterfaceITfInputProcessorProfiles.INSTANCE.IsEnabledLanguageProfile(lpLayoutOrTipProfile[i].clsid,
							lpLayoutOrTipProfile[i].langid, lpLayoutOrTipProfile[i].guidProfile, bEnable);
					// Set the flag if disabled.
					if(bEnable.getValue() == 0) {
						lpLayoutOrTipProfile[i].dwFlags |= LAYOUTORTIPPROFILE.LOT_DISABLED;
					}
				}
				ipLangIdSet.add(lpLayoutOrTipProfile[i].langid);
				profileTable.put(name, lpLayoutOrTipProfile[i]);
			}
		}
		// If a certain language has input processors, corresponding keyboard layout should be omitted. 
		Iterator<Entry<String, LAYOUTORTIPPROFILE>> iter = keyboardLayoutProfileTable.entrySet().iterator();
		Map.Entry<String, LAYOUTORTIPPROFILE> entry;
		while (iter.hasNext()) {
			entry = (Entry<String, LAYOUTORTIPPROFILE>) iter.next();
			LAYOUTORTIPPROFILE profile = entry.getValue();
			if(!ipLangIdSet.contains(profile.langid)) {
				profileTable.put(entry.getKey(), profile);
			}
		}
//		Iterator<Entry<String, LAYOUTORTIPPROFILE>> iter2 = profileTable.entrySet().iterator();
//		Map.Entry<String, LAYOUTORTIPPROFILE> entry2;
//		while (iter2.hasNext()) {
//			entry2 = (Entry<String, LAYOUTORTIPPROFILE>) iter2.next();
//			LAYOUTORTIPPROFILE profile = entry2.getValue();
//			System.out.println(LAYOUTORTIPPROFILE.getStringRepresentation(profile));
//		}
		return true;
	}
	
	/**
	 * If profileTable is not null, imeInfoList will be cleared first.
	 * Then new entries will be added to it.
	 */
	@Override
	public boolean getIMEInfoList(ArrayList<IMEInfo> imeInfoList) {
		if(profileTable == null || imeInfoList == null) {
			return false;
		}
		if(!imeInfoList.isEmpty()) {
			imeInfoList.clear();
		}
		Iterator<Entry<String, LAYOUTORTIPPROFILE>> iter = profileTable.entrySet().iterator();
		Map.Entry<String, LAYOUTORTIPPROFILE> entry;
		while (iter.hasNext()) {
			entry = (Entry<String, LAYOUTORTIPPROFILE>) iter.next();
			LAYOUTORTIPPROFILE profile = entry.getValue();
			// Add to imeInfoList only if the profile is enabled.
			if ((profile.dwFlags & LAYOUTORTIPPROFILE.LOT_DISABLED) == 0) {
				imeInfoList.add(new IMEInfo(entry.getKey(), String.valueOf(profile.szId).trim(),
						String.format("%04X", profile.langid),
						Win32Util.flagsToString(profile.dwProfileType, LAYOUTORTIPPROFILE.dwProfileTypeBitFlags)));
			}
		}
		return true;
	}
	
	/**
	 * Returns false if error occurs.
	 */
	@Override
	public boolean isIMEInstalled(String imeName) {
		if (profileTable == null) {
			return false;
		}
		return profileTable.containsKey(imeName);
	}
	
	/**
	 * The IME is searched by following logic:
	 * (1) If there is System Default IME in profileTable, copy it to imeInfo.
	 * (2) If System Default IME doesn't exist in profileTable, copy an arbitrary text service in profileTable to imeInfo.
	 * (3) If no profile exists(profileTable is empty), return false. imeInfo will not be altered.
	 * In fact, there will always be one enabled IME, (3) will never happen, which means 
	 * imeInfo will always be filled with value with meaning.
	 */
	@Override
	public boolean getDefaultIME(IMEInfo imeInfo) {
		if(imeInfo == null) {
			return false;
		}
		if(defaultProfile == null) {
			if(profileTable.size()!=0) {
				// Copy the first one in profileTable.
				for(Map.Entry<String, LAYOUTORTIPPROFILE> entry : profileTable.entrySet()) {
					defaultProfile = entry.getValue();
					defaultProfileName = entry.getKey();
					break;
				}
			}
		}
		if(defaultProfile == null) {
			return false;
		}
		imeInfo.name = defaultProfileName;
		imeInfo.id = String.valueOf(defaultProfile.szId).trim();
		imeInfo.data = new String[] { String.format("%04X", defaultProfile.langid),
				Win32Util.flagsToString(defaultProfile.dwProfileType, LAYOUTORTIPPROFILE.dwProfileTypeBitFlags) };
		System.out.println("getDefaultIME \n"+imeInfo);
		return true;
	}
	
	/**
	 * The IME is searched by following logic:
	 * (1) If en_us keyboard layout(szId: 0409:0x00000409) exists in profileTable, it is copied to imeInfo.
	 * (2) If en_us doesn't exist, find an English qwerty keyboard layout(szId: XX09:0x0000XX09).
	 * (3) If no English qwerty keyboard layout exists, find an arbitrary English keyboard layout(szId: XX09:0xXXXXXX09).
	 * (4) If no English keyboard layout exists, copy an arbitrary keyboard layout to imeInfo(szId: XXXX:0xXXXXXXXX).
	 * (5) If no keyboard layout exists, copy an arbitrary input processor to imeInfo(szId: XXXX:{GUID}{GUID}).
	 * (6) If no profile exists, returns false. imeInfo will not be altered.
	 * In fact, there will always be one enabled IME, (6) will never happen, which means 
	 * imeInfo will always be filled with value with meaning.
	 */
	@Override
	public int getEnglishIME(IMEInfo imeInfo) {
		if(imeInfo == null) {
			return RESULT_ERROR;
		}
		if(englishProfile == null) {
			boolean doesEnqwertyKLExist = false;
			boolean doesEnKLExist = false;
			boolean doesKLExist = false;
			boolean doesProfileExist = false;
			String name;
			LAYOUTORTIPPROFILE profile;
			String szId;
			for (Map.Entry<String, LAYOUTORTIPPROFILE> entry : profileTable.entrySet()) {
				name = entry.getKey();
				profile = entry.getValue();
				szId = String.valueOf(profile.szId);
				System.out.println("szId: "+szId);
				System.out.println("szId.length"+szId.length());
				szId = szId.trim();
				System.out.println("szId: "+szId);
				System.out.println("szId.length"+szId.length());
				if(szId.equals("0409:00000409")) {
					englishProfile = profile;
					englishProfileName = name;
					break;
				}else if(!doesEnqwertyKLExist) {
					if(szId.matches("[a-fA-F0-9]{2}09:0000[a-fA-F0-9]{2}09")) {
						englishProfile = profile;
						englishProfileName = name;
						doesEnqwertyKLExist = true;
					}else if(!doesEnKLExist) {
						if(szId.matches("[a-fA-F0-9]{2}09:[a-fA-F0-9]{6}09")) {
							englishProfile = profile;
							englishProfileName = name;
							doesEnKLExist = true;
						}else if(!doesKLExist) {
							if(szId.matches("[a-fA-F0-9]{4}:[a-fA-F0-9]{8}")) {
								englishProfile = profile;
								englishProfileName = name;
								doesKLExist = true;
							}else if(!doesProfileExist) {
								englishProfile = profile;
								englishProfileName = name;
								doesProfileExist = true;
							}
						}
					}
				}
			}
		}
		if(englishProfile == null) {
			return RESULT_ERROR;
		}
		imeInfo.name = englishProfileName;
		imeInfo.id = String.valueOf(englishProfile.szId).trim();
		imeInfo.data = new String[] { String.format("%04X", englishProfile.langid),
				Win32Util.flagsToString(englishProfile.dwProfileType, LAYOUTORTIPPROFILE.dwProfileTypeBitFlags) };
		System.out.println("getEnglishIME \n"+imeInfo);
		return RESULT_OK;
		
	}

}
