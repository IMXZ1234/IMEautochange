package com.imeautochange.config;

import java.util.HashMap;

import com.imeautochange.ModFunctionManager;

public class ConfigManager {
	private static HashMap<Class<?>, ClassConfigInfo> listenerClassConfigInfo = new HashMap<Class<?>, ClassConfigInfo>();
	public static HashMap<Class<?>, ClassConfigInfo> getListenerClassConfigInfo() {
		return listenerClassConfigInfo;
	}
	public static void registerClassConfigInfo(ClassConfigInfo classConfigInfo) {
		listenerClassConfigInfo.put(classConfigInfo.clazz, classConfigInfo);
		ModFunctionManager.updateHandlersListenerTable(classConfigInfo);
	}
	public static void updateConfigChanges(HashMap<Class<?>, ClassConfigInfo> cachedChanges) {
		listenerClassConfigInfo.putAll(cachedChanges);
		ModFunctionManager.updateHandlersListenerTable(cachedChanges);
	}
	
	public static void loadFromConfigFile() {
		// TODO Auto-generated method stub
		
	}

}
