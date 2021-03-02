package com.imeautochange.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.electronwill.nightconfig.core.file.FileConfig;
import com.imeautochange.ModFunctionManager;
import com.imeautochange.util.ConfigUtil;

import net.minecraftforge.fml.loading.FMLPaths;

public class ConfigManager {
	public static final String CONFIG_FILENAME = "imeautochange-functions.toml";
	private static HashMap<Class<?>, ClassConfigInfo> listenerClassConfigInfo = new HashMap<Class<?>, ClassConfigInfo>();
	private static FileConfig fileConfig;
	
	private static final Logger LOGGER = LogManager.getLogger();
	
	public static HashMap<Class<?>, ClassConfigInfo> getListenerClassConfigInfo() {
		return listenerClassConfigInfo;
	}
	public static void registerClassConfigInfo(ClassConfigInfo classConfigInfo) {
		LOGGER.info("Registering ClassConfigInfo: " + classConfigInfo.description);
		if(classConfigInfo.isOverlay) {
			listenerClassConfigInfo.put(classConfigInfo.overlayAdapter.getOverlayClass(), classConfigInfo);
		}else {
			listenerClassConfigInfo.put(classConfigInfo.clazz, classConfigInfo);
		}
	}
	public static void updateConfigChanges(HashMap<Class<?>, ClassConfigInfo> cachedChanges) {
		LOGGER.info("Updating Config Changes...");
		listenerClassConfigInfo.putAll(cachedChanges);
		ModFunctionManager.updateHandlersListenerTable(cachedChanges);
		saveConfigChangesToFile(cachedChanges);
	}
	
	public static void saveConfigChangesToFile(HashMap<Class<?>, ClassConfigInfo> cachedChanges) {
		Path configFilePath = FMLPaths.CONFIGDIR.get().resolve(CONFIG_FILENAME);
		LOGGER.info("Saving Config Changes to File...");
		if (!Files.exists(configFilePath)) {
			try {
				Files.createFile(configFilePath);
				LOGGER.info("New Config File Generated.");
				ConfigUtil.updateConfigByClassConfigInfoMap(listenerClassConfigInfo, fileConfig);
				fileConfig.save();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			ConfigUtil.updateConfigByClassConfigInfoMap(cachedChanges, fileConfig);
			fileConfig.save();
		}
		LOGGER.info("Complete Saving Config Changes to File.");
	}
	public static void updateConfigFromFile() {
		Path configFilePath = FMLPaths.CONFIGDIR.get().resolve(CONFIG_FILENAME);
		System.out.println(configFilePath.toString());
		if (!Files.exists(configFilePath)) {
			try {
				Files.createFile(configFilePath);
				LOGGER.info("New Config File Generated.");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		fileConfig = FileConfig.of(configFilePath);
		fileConfig.load();
		if (fileConfig.isEmpty()) {
			ConfigUtil.updateConfigByClassConfigInfoMap(listenerClassConfigInfo, fileConfig);
			fileConfig.save();
		} else {
			LOGGER.info("Reading Config from File...");
			ConfigUtil.updateClassConfigInfoMapByConfig(listenerClassConfigInfo, fileConfig);
			LOGGER.info("Complete Reading Config from File.");
		}
	}

}
