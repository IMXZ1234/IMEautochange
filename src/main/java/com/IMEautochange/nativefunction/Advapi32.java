package com.IMEautochange.nativefunction;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.win32.StdCallLibrary;

public interface Advapi32 extends StdCallLibrary 
{ 
    // Method declarations, constant and structure definitions go here
	Advapi32 INSTANCE = (Advapi32)Native.loadLibrary("advapi32",Advapi32.class);
	
//samDesired
//	KEY_QUERY_VALUE                      equ 1h
//	KEY_SET_VALUE                        equ 2h
//	KEY_CREATE_SUB_KEY                   equ 4h
//	KEY_ENUMERATE_SUB_KEYS               equ 8h
//	KEY_NOTIFY                           equ 10h
//	KEY_CREATE_LINK                      equ 20h
//	KEY_READ                             equ (STANDARD_RIGHTS_READ OR KEY_QUERY_VALUE OR KEY_ENUMERATE_SUB_KEYS OR KEY_NOTIFY) AND NOT SYNCHRONIZE
//	KEY_WRITE                            equ (STANDARD_RIGHTS_WRITE OR KEY_SET_VALUE OR KEY_CREATE_SUB_KEY) AND NOT SYNCHRONIZE
//	KEY_EXECUTE                          equ KEY_READ
//	KEY_ALL_ACCESS                       equ (STANDARD_RIGHTS_ALL OR KEY_QUERY_VALUE OR KEY_SET_VALUE OR KEY_CREATE_SUB_KEY OR KEY_ENUMERATE_SUB_KEYS OR KEY_NOTIFY OR KEY_CREATE_LINK) AND NOT SYNCHRONIZE
	int RegOpenKeyExW(int hKey,char[] lpSubKey,int ulOptions,int samDesired,int[] phkResult);
//lpType
//	REG_NONE                             equ 0
//	REG_SZ                               equ 1
//	REG_EXPAND_SZ                        equ 2
//	REG_BINARY                           equ 3
//	REG_DWORD                            equ 4
//	REG_DWORD_LITTLE_ENDIAN              equ 4
//	REG_DWORD_BIG_ENDIAN                 equ 5
//	REG_LINK                             equ 6
//	REG_MULTI_SZ                         equ 7
//	REG_RESOURCE_LIST                    equ 8
	int RegQueryValueExW(int hKey,char[] lpValueName,char[] lpReserved,char[] lpType,char[] lpData,int[] lpcbData);
}