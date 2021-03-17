package com.imeautochange.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.file.FileConfig;
import com.imeautochange.config.ClassConfigInfo;
import com.imeautochange.config.ClassConfigItem;
import com.imeautochange.config.GeneralConfigItem;

public class ConfigUtil {
	private static final Logger LOGGER = LogManager.getLogger();
	
	public static final String IMENAME = "IME";
	public static final String ISENABLED = "Enabled";
	
	public static boolean updateConfigByClassConfigInfo(ClassConfigInfo classConfigInfo, Config config) {
		String rootNameString = classConfigInfo.description;
		if(rootNameString == null) {
			LOGGER.error("ClassConfigInfo.description: null!");
			return false;
		}
		for(Entry<String, ClassConfigItem> entry:classConfigInfo.configItems.entrySet()) {
			String configItemName = entry.getKey();
			ClassConfigItem configItem = entry.getValue();
			if(configItem == null || configItemName == null) {
				LOGGER.error("configItem || configItemName: null!");
				continue;
			}
			config.set(rootNameString+"."+configItemName+"."+IMENAME, configItem.imeName);
			config.set(rootNameString+"."+configItemName+"."+ISENABLED, configItem.enabled);
		}
		return true;
	}
	
	public static boolean updateClassConfigInfoByConfig(ClassConfigInfo classConfigInfo, Config config) {
		String rootNameString = classConfigInfo.description;
		if(rootNameString == null) {
			LOGGER.error("ClassConfigInfo.description: null!");
			return false;
		}
		for(Entry<String, ClassConfigItem> entry:classConfigInfo.configItems.entrySet()) {
			String configItemName = entry.getKey();
			ClassConfigItem configItem = entry.getValue();
			if(configItem == null) {
				LOGGER.error("configItemName: null!");
				continue;
			}
			if(configItemName == null) {
				LOGGER.error("configItem: null!");
				continue;
			}
			String imeName = config.get(rootNameString+"."+configItemName+"."+IMENAME);
			Boolean enabled = config.get(rootNameString+"."+configItemName+"."+ISENABLED);
			if(imeName != null){
				configItem.imeName = imeName;
			}else {
				LOGGER.error("configItem.imeName: null!");
			}
			if(enabled != null) {
				configItem.enabled = enabled;
			}else {
				LOGGER.error("configItem.enabled: null!");
			}
		}
		return true;
	}
	public static void updateConfigByClassConfigInfoMap(HashMap<String, ClassConfigInfo> classConfigInfoMap, Config config) {
		for(Map.Entry<String, ClassConfigInfo> entry : classConfigInfoMap.entrySet()) {
			updateConfigByClassConfigInfo(entry.getValue(),config);
		}
	}
	public static void updateClassConfigInfoMapByConfig(HashMap<String, ClassConfigInfo> classConfigInfoMap, FileConfig fileConfig) {
		Map<String, Object> configMap = fileConfig.valueMap();
		for(Entry<String, ClassConfigInfo> classConfigInfoEntry : classConfigInfoMap.entrySet()) {
			ClassConfigInfo classConfigInfo = classConfigInfoEntry.getValue();
			String classDescription = classConfigInfo.description;
			Config classConfig = (Config)configMap.get(classDescription);
			if(classConfig != null) {
				LOGGER.error("Found ClassConfigInfo in classConfigInfoMap, but not in Config!");
				updateClassConfigInfoByConfig(classConfigInfo, classConfig);
			}
		}
	}
	public static void updateGeneralConfigItemMapByConfig(HashMap<String, GeneralConfigItem> generalConfigItemMap, FileConfig fileConfig) {
		for (Entry<String, GeneralConfigItem> generalConfigItemEntry : generalConfigItemMap.entrySet()) {
			Object value = fileConfig.get(generalConfigItemEntry.getKey());
			if(value != null) {
				generalConfigItemEntry.getValue().value = value;
			} else {
				LOGGER.error("value: null!");
			}
		}
	}
	public static void updateConfigByGeneralConfigItemMap(HashMap<String, GeneralConfigItem> generalConfigItemMap, FileConfig fileConfig) {
		for (Entry<String, GeneralConfigItem> generalConfigItemEntry : generalConfigItemMap.entrySet()) {
			fileConfig.set(generalConfigItemEntry.getKey(), generalConfigItemEntry.getValue().value);
		}
	}
}
