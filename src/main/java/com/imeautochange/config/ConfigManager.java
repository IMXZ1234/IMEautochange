package com.imeautochange.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.electronwill.nightconfig.core.file.FileConfig;
import com.imeautochange.ModFunctionManager;
import com.imeautochange.nativefunction.NativeFunctionManager;
import com.imeautochange.util.ConfigUtil;

import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.loading.FMLPaths;

public class ConfigManager {
	public static final String DESCRIPTION_TEXTINPUTIMENAME = "TextInputIMEName";
	public static final String DESCRIPTION_GAMECONTROLINPUTIMENAME = "GameControlInputIMEName";
	public static final String FUNCTION_CONFIG_FILENAME = "imeautochange-functions.toml";
	public static final String GENERAL_CONFIG_FILENAME = "imeautochange-general.toml";
	private static HashMap<String, ClassConfigInfo> listenerClassConfigInfo = new HashMap<String, ClassConfigInfo>();
	private static HashMap<String, GeneralConfigItem> generalConfigItems = new HashMap<String, GeneralConfigItem>();
	private static FileConfig functionFileConfig;
	private static FileConfig generalFileConfig;

	private static final Logger LOGGER = LogManager.getLogger();

	public static HashMap<String, ClassConfigInfo> getListenerClassConfigInfo() {
		return listenerClassConfigInfo;
	}

	public static HashMap<String, GeneralConfigItem> getGeneralConfigItems() {
		return generalConfigItems;
	}
	
	public static String getTextInputIMEName() {
		return (String) generalConfigItems.get(DESCRIPTION_TEXTINPUTIMENAME).value;
	}
	
	public static String getGameControlInputIMEName() {
		return (String) generalConfigItems.get(DESCRIPTION_GAMECONTROLINPUTIMENAME).value;
	}

	public static void initGeneralConfigItems() {
		registerGeneralConfigItem(new GeneralConfigItem(DESCRIPTION_TEXTINPUTIMENAME,
				new TranslationTextComponent("imeautochange.config.general.textinputimename"),
				NativeFunctionManager.getDefaultIME().name));
		registerGeneralConfigItem(new GeneralConfigItem(DESCRIPTION_GAMECONTROLINPUTIMENAME,
				new TranslationTextComponent("imeautochange.config.general.gamecontrolinputimename"),
				NativeFunctionManager.getEnglishIME().name));
	}

	public static void registerGeneralConfigItem(GeneralConfigItem generalConfigItem) {
		LOGGER.info("Registering GeneralConfigItem: " + generalConfigItem.description);
		generalConfigItems.put(generalConfigItem.description, generalConfigItem);
	}

	public static void registerClassConfigInfo(ClassConfigInfo classConfigInfo) {
		LOGGER.info("Registering ClassConfigInfo: " + classConfigInfo.description);
		listenerClassConfigInfo.put(classConfigInfo.description, classConfigInfo);
//		if(classConfigInfo.isOverlay) {
//			listenerClassConfigInfo.put(classConfigInfo.overlayAdapter.getOverlayClass(), classConfigInfo);
//		}else {
//			listenerClassConfigInfo.put(classConfigInfo.clazz, classConfigInfo);
//		}
	}

	public static void updateFunctionConfigChanges(HashMap<String, ClassConfigInfo> cachedChanges) {
		LOGGER.info("Updating Config Changes...");
		listenerClassConfigInfo.putAll(cachedChanges);
		ModFunctionManager.updateHandlersListenerTable(cachedChanges);
		saveFunctionConfigChangesToFile(cachedChanges);
	}

	public static void updateDefaultIMEConfigChanges(HashMap<String, GeneralConfigItem> cachedChanges) {
		LOGGER.info("Updating Config Changes...");
		String lastTextInputIMEName = getTextInputIMEName();
		String lastGameControlInputIMEName = getGameControlInputIMEName();
		generalConfigItems.putAll(cachedChanges);
		String textInputIMEName = getTextInputIMEName();
		String gameControlInputIMEName = getGameControlInputIMEName();
		for (Entry<String, ClassConfigInfo> classConfigInfoEntry : listenerClassConfigInfo.entrySet()) {
			for (Entry<String, ClassConfigItem> configItemEntry: classConfigInfoEntry.getValue().configItems.entrySet()) {
				ClassConfigItem configItem = configItemEntry.getValue();
				if(configItem.defaultIMEName.equals(lastTextInputIMEName)) {
					configItem.defaultIMEName = textInputIMEName;
				}
				if(configItem.defaultIMEName.equals(lastGameControlInputIMEName)) {
					configItem.defaultIMEName = gameControlInputIMEName;
				}
			}
		}
		saveGeneralConfigChangesToFile(cachedChanges);
	}

	public static void saveFunctionConfigChangesToFile(HashMap<String, ClassConfigInfo> cachedChanges) {
		Path configFilePath = FMLPaths.CONFIGDIR.get().resolve(FUNCTION_CONFIG_FILENAME);
		LOGGER.info("Saving Function Config Changes to File...");
		if (!Files.exists(configFilePath)) {
			try {
				Files.createFile(configFilePath);
				LOGGER.info("New Function Config File Generated.");
				ConfigUtil.updateConfigByClassConfigInfoMap(listenerClassConfigInfo, functionFileConfig);
				functionFileConfig.save();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			ConfigUtil.updateConfigByClassConfigInfoMap(cachedChanges, functionFileConfig);
			functionFileConfig.save();
		}
		LOGGER.info("Complete Saving Config Changes to File.");
	}

	public static void updateFunctionConfigFromFile() {
		Path configFilePath = FMLPaths.CONFIGDIR.get().resolve(FUNCTION_CONFIG_FILENAME);
		System.out.println(configFilePath.toString());
		if (!Files.exists(configFilePath)) {
			try {
				Files.createFile(configFilePath);
				LOGGER.info("New Function Config File Generated.");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		functionFileConfig = FileConfig.of(configFilePath);
		functionFileConfig.load();
		if (functionFileConfig.isEmpty()) {
			ConfigUtil.updateConfigByClassConfigInfoMap(listenerClassConfigInfo, functionFileConfig);
			functionFileConfig.save();
		} else {
			LOGGER.info("Reading Function Config from File...");
			ConfigUtil.updateClassConfigInfoMapByConfig(listenerClassConfigInfo, functionFileConfig);
			LOGGER.info("Complete Reading Config from File.");
		}
	}

	public static void saveGeneralConfigChangesToFile(HashMap<String, GeneralConfigItem> cachedChanges) {
		Path configFilePath = FMLPaths.CONFIGDIR.get().resolve(GENERAL_CONFIG_FILENAME);
		LOGGER.info("Saving General Config Changes to File...");
		if (!Files.exists(configFilePath)) {
			try {
				Files.createFile(configFilePath);
				LOGGER.info("New General Config File Generated.");
				ConfigUtil.updateConfigByGeneralConfigItemMap(generalConfigItems, generalFileConfig);
				generalFileConfig.save();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			ConfigUtil.updateConfigByGeneralConfigItemMap(generalConfigItems, generalFileConfig);
			generalFileConfig.save();
		}
		LOGGER.info("Complete Saving Config Changes to File.");
	}

	public static void updateGeneralConfigFromFile() {
		Path configFilePath = FMLPaths.CONFIGDIR.get().resolve(GENERAL_CONFIG_FILENAME);
		System.out.println(configFilePath.toString());
		if (!Files.exists(configFilePath)) {
			try {
				Files.createFile(configFilePath);
				LOGGER.info("New General Config File Generated.");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		generalFileConfig = FileConfig.of(configFilePath);
		generalFileConfig.load();
		if (generalFileConfig.isEmpty()) {
			ConfigUtil.updateConfigByGeneralConfigItemMap(generalConfigItems, generalFileConfig);
			generalFileConfig.save();
		} else {
			LOGGER.info("Reading General Config from File...");
			ConfigUtil.updateGeneralConfigItemMapByConfig(generalConfigItems, generalFileConfig);
			LOGGER.info("Complete Reading Config from File.");
		}
	}
}
