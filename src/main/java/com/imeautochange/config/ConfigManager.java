package com.imeautochange.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

import com.electronwill.nightconfig.core.file.FileConfig;
import com.imeautochange.ModFunctionManager;
import com.imeautochange.util.ConfigUtil;

import net.minecraftforge.fml.loading.FMLPaths;

public class ConfigManager {
	public static final String CONFIG_FILENAME = "imeautochange-functions.toml";
	private static HashMap<Class<?>, ClassConfigInfo> listenerClassConfigInfo = new HashMap<Class<?>, ClassConfigInfo>();
	private static FileConfig fileConfig;
//	public static Path configFilePath;
	
	
	public static HashMap<Class<?>, ClassConfigInfo> getListenerClassConfigInfo() {
		return listenerClassConfigInfo;
	}
	public static void registerClassConfigInfo(ClassConfigInfo classConfigInfo) {
		System.out.println("registerClassConfigInfo:\n"+classConfigInfo);
		if(classConfigInfo.isOverlay) {
			listenerClassConfigInfo.put(classConfigInfo.overlayAdapter.getOverlayClass(), classConfigInfo);
		}else {
			listenerClassConfigInfo.put(classConfigInfo.clazz, classConfigInfo);
		}
//		ModFunctionManager.updateHandlersListenerTable(classConfigInfo);
	}
	public static void updateConfigChanges(HashMap<Class<?>, ClassConfigInfo> cachedChanges) {
//		for(Entry<Class<?>, ClassConfigInfo> entry:cachedChanges.entrySet()) {
//			ClassConfigInfo classConfigInfo = entry.getValue();
//			if(classConfigInfo.isOverlay) {
//				listenerClassConfigInfo.put(classConfigInfo.overlayAdapter.getOverlayClass(), classConfigInfo);
//			}else {
//				listenerClassConfigInfo.put(classConfigInfo.clazz, classConfigInfo);
//			}
//		}
		listenerClassConfigInfo.putAll(cachedChanges);
		ModFunctionManager.updateHandlersListenerTable(cachedChanges);
		saveConfigChangesToFile(cachedChanges);
	}
	
	public static void saveConfigChangesToFile(HashMap<Class<?>, ClassConfigInfo> cachedChanges) {
		Path configFilePath = FMLPaths.CONFIGDIR.get().resolve(CONFIG_FILENAME);
		System.out.println(configFilePath.toString());
		if (!Files.exists(configFilePath)) {
			try {
				Files.createFile(configFilePath);
				ConfigUtil.updateConfigByClassConfigInfoMap(listenerClassConfigInfo, fileConfig);
				fileConfig.save();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			ConfigUtil.updateConfigByClassConfigInfoMap(cachedChanges, fileConfig);
			fileConfig.save();
		}
	}
	public static void updateConfigFromFile() {
		Path configFilePath = FMLPaths.CONFIGDIR.get().resolve(CONFIG_FILENAME);
		System.out.println(configFilePath.toString());
		if (!Files.exists(configFilePath)) {
			try {
				Files.createFile(configFilePath);
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
			ConfigUtil.updateClassConfigInfoMapByConfig(listenerClassConfigInfo, fileConfig);
		}
	}

}
