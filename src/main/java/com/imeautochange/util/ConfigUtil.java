package com.imeautochange.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.file.FileConfig;
import com.imeautochange.config.ClassConfigInfo;
import com.imeautochange.config.ConfigItem;

public class ConfigUtil {
	public static final String IMENAME = "IME";
	public static final String ISENABLED = "Enabled";
	public static boolean updateConfigByClassConfigInfo(ClassConfigInfo classConfigInfo, Config config) {
		String rootNameString = classConfigInfo.displayName.getString();
		if(rootNameString == null) {
			System.out.println("rootNameString: null!");
			return false;
		}
		System.out.println("rootNameString: "+rootNameString);
		for(Entry<String, ConfigItem> entry:classConfigInfo.configItems.entrySet()) {
			String configItemName = entry.getKey();
			ConfigItem configItem = entry.getValue();
			if(configItem == null || configItemName == null) {
				System.out.println("configItem || configItemName: null!");
				continue;
			}
			config.set(rootNameString+"."+configItemName+"."+IMENAME, configItem.imeName);
			config.set(rootNameString+"."+configItemName+"."+ISENABLED, configItem.enabled);
		}
		return true;
	}
	public static boolean updateClassConfigInfoByConfig(ClassConfigInfo classConfigInfo, Config config) {
		String rootNameString = classConfigInfo.displayName.getString();
		if(rootNameString == null) {
			System.out.println("rootNameString: null!");
			return false;
		}
		System.out.println("rootNameString: "+rootNameString);
		for(Entry<String, ConfigItem> entry:classConfigInfo.configItems.entrySet()) {
			String configItemName = entry.getKey();
			ConfigItem configItem = entry.getValue();
			if(configItem == null) {
				System.out.println("configItemName: null!");
				continue;
			}else {
				System.out.println("configItemName: "+configItemName);
			}
			if(configItemName == null) {
				System.out.println("configItem: null!");
				continue;
			}else {
				System.out.println("configItem: "+configItem);
			}
			String imeName = config.get(rootNameString+"."+configItemName+"."+IMENAME);
			Boolean enabled = config.get(rootNameString+"."+configItemName+"."+ISENABLED);
			if(imeName != null){
				configItem.imeName = imeName;
			}else {
				System.out.println("imeName is null");
			}
			if(enabled != null) {
				configItem.enabled = enabled;
			}else {
				System.out.println("enabled is null");
			}
//			config.set(rootNameString+"."+configItemName+"."+IMENAME, configItem.imeName);
//			config.set(rootNameString+"."+configItemName+"."+ISENABLED, configItem.enabled);
		}
		return true;
	}
	public static void updateConfigByClassConfigInfoMap(HashMap<Class<?>, ClassConfigInfo> classConfigInfoMap, Config config) {
		for(Map.Entry<Class<?>, ClassConfigInfo> entry : classConfigInfoMap.entrySet()) {
			updateConfigByClassConfigInfo(entry.getValue(),config);
		}
	}
	public static void updateClassConfigInfoMapByConfig(HashMap<Class<?>, ClassConfigInfo> classConfigInfoMap, FileConfig fileConfig) {
		Map<String, Object> configMap = fileConfig.valueMap();
		for(Entry<Class<?>, ClassConfigInfo> classConfigInfoEntry : classConfigInfoMap.entrySet()) {
			ClassConfigInfo classConfigInfo = classConfigInfoEntry.getValue();
			String classDisplayName = classConfigInfo.displayName.toString();
			Config classConfig = (Config)configMap.get(classDisplayName);
			if(classConfig == null) {
				continue;
			}
			updateClassConfigInfoByConfig(classConfigInfo, classConfig);
		}
	}
}
