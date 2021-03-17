package com.imeautochange;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.imeautochange.compat.startup.BuiltInModSupport;
import com.imeautochange.config.ClassConfigInfo;
import com.imeautochange.config.ClassConfigItem;
import com.imeautochange.config.ConfigManager;
import com.imeautochange.config.GeneralConfigItem;
import com.imeautochange.config.IMEInfo;
import com.imeautochange.config.ModKeyBinding;
import com.imeautochange.event.EventsHandlerManager;
import com.imeautochange.event.KeyBindingInputEventsHandler;
import com.imeautochange.event.ModClientEventsHandlerBase;
import com.imeautochange.event.ModClientEventsHandlerCommon;
import com.imeautochange.event.ModClientEventsHandlerSpecific;
import com.imeautochange.event.OpenChatKeyBindingInputEventsHandler;
import com.imeautochange.nativefunction.NativeFunctionManager;
import com.imeautochange.startup.BuiltInSupport;
import com.imeautochange.startup.IMESupportManager;
import com.imeautochange.util.ReflectionUtil;

import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ModFunctionManager {
	private static HashMap<String, ClassConfigInfo> listenerClassConfigInfo;
	private static HashMap<String, GeneralConfigItem> generalConfigItems;
	private static boolean modFunctionEnabled = false;
	private static KeyBindingInputEventsHandler keyBindingInputEventsHandler;
	private static HashMap<String, ModClientEventsHandlerBase> eventsHandlers;
	private static ArrayList<IMEInfo> imeInfoList;
//	private static String textInputIMEName;
//	private static String gameControlInputIMEName;
	
	private static Logger LOGGER = LogManager.getLogger();

	public static boolean isFunctionEnabled() {
		return modFunctionEnabled;
	}
	public static void init(final FMLClientSetupEvent event) {
		// Initialize NativeFunctionManager
		LOGGER.info("Initializing Mod Functions...");
		NativeFunctionManager.init();
		if(!NativeFunctionManager.doesFunctionProviderExist()) {
			return;
		}
		modFunctionEnabled = true;
		// Initialize General Configuration
		ConfigManager.initGeneralConfigItems();
		// Load General Configuration
		ConfigManager.updateGeneralConfigFromFile();
		generalConfigItems = ConfigManager.getGeneralConfigItems();
		// Already reloaded during initialization of NativeFunctionManager.
		imeInfoList = NativeFunctionManager.getIMEInfoList(false);
		// Initialization
		IMESupportManager.registerIMESupport(new BuiltInSupport());
		IMESupportManager.registerIMESupport(new BuiltInModSupport());
		IMESupportManager.initAllIMESupports();
		// Initialize members(get a reference from other managers)
		listenerClassConfigInfo = ConfigManager.getListenerClassConfigInfo();
		eventsHandlers = EventsHandlerManager.getEventsHandlers();
		// Load Function Configuration
		ConfigManager.updateFunctionConfigFromFile();
	    // Initialize Events Handlers
//	    updateHandlersListenerTable(listenerClassConfigInfo);
	    updateHandlersCachedFieldList();
	    updateHandlersListenerTable(listenerClassConfigInfo);
	    for(Map.Entry<String, ModClientEventsHandlerBase> entry : eventsHandlers.entrySet()) {
	    	entry.getValue().register();
	    }
	    
		// Key binding
		event.enqueueWork(() -> ModKeyBinding.registerKeyBinding());
		keyBindingInputEventsHandler = new KeyBindingInputEventsHandler();
		keyBindingInputEventsHandler.register();
	}
	
	public static HashMap<String, ClassConfigInfo> getListenerClassConfigInfo() {
		return listenerClassConfigInfo;
	}
	
	public static ArrayList<IMEInfo> getIMEInfoList(){
		return imeInfoList;
	}
	
//	public static String getTextInputIMEName() {
//		return textInputIMEName;
//	}
//	
//	public static String getGameControlInputIMEName() {
//		return gameControlInputIMEName;
//	}
//	
//	public static void setTextInputIMEName(String textInputIMENameIn) {
//		textInputIMEName = textInputIMENameIn;
//	}
//	
//	public static void setGameControlInputIMEName(String gameControlInputIMENameIn) {
//		gameControlInputIMEName = gameControlInputIMENameIn;
//	}
	
	public static void updateHandlersCachedFieldList() {
		LOGGER.info("Caching Reflection Fields...");
		for(Entry<String, ClassConfigInfo> classConfigInfoEntry : listenerClassConfigInfo.entrySet()) {
			ClassConfigInfo classConfigInfo = classConfigInfoEntry.getValue();
				ArrayList<Field> classFields = new ArrayList<Field>();
				if(classConfigInfo.isOverlay) {
					if(ReflectionUtil.getFieldList(classConfigInfo.overlayAdapter.getOverlayClass(), TextFieldWidget.class, classFields)) {
						for(String handlerDescription : classConfigInfo.configItems.keySet()) {
							ModClientEventsHandlerBase handler = EventsHandlerManager.getHandlerIdByDescription(handlerDescription);
							if(handler instanceof ModClientEventsHandlerCommon) {
								((ModClientEventsHandlerCommon)handler).cachedOverlayFieldList.put(classConfigInfo.overlayAdapter, classFields);
							}
						}
					}
				}else {
					if(ReflectionUtil.getFieldList(classConfigInfo.clazz, TextFieldWidget.class, classFields)) {
						for(String handlerDescription : classConfigInfo.configItems.keySet()) {
							ModClientEventsHandlerBase handler = EventsHandlerManager.getHandlerIdByDescription(handlerDescription);
							if(handler instanceof ModClientEventsHandlerCommon) {
								((ModClientEventsHandlerCommon)handler).cachedScreenFieldTable.put(classConfigInfo.clazz, classFields);
							}
						}
					}
				}
		}
	}
	
//	public static void loadConfigFromFile() {
//		ConfigManager.loadFromConfigFile();
//	}
	
	public static void updateHandlersListenerTable(HashMap<String, ClassConfigInfo> cachedChanges) {
		LOGGER.info("Updating Handlers Internal table...");
		for (Entry<String, ClassConfigInfo> classConfigInfoEntry : cachedChanges.entrySet()) {
			updateHandlersListenerTable(classConfigInfoEntry.getValue());
		}
	}

	public static void updateHandlersListenerTable(ClassConfigInfo classConfigInfo) {
		System.out.println("ClassConfigInfo");
		System.out.println(classConfigInfo);
		for (Entry<String, ClassConfigItem> configItemsEntry : classConfigInfo.configItems.entrySet()) {
			ModClientEventsHandlerBase handler = EventsHandlerManager.getHandlerIdByDescription(configItemsEntry.getKey());
			if (handler != null) {
				ClassConfigItem configItem = configItemsEntry.getValue();
				if (handler instanceof ModClientEventsHandlerCommon) {
					if (configItem.enabled) {
						if (classConfigInfo.isOverlay)
							((ModClientEventsHandlerCommon) handler).addOverlayListenerClass(classConfigInfo.overlayAdapter, configItem.imeName);
						else
							((ModClientEventsHandlerCommon) handler).addScreenListenerClass(classConfigInfo.clazz, configItem.imeName);
					} else {
						if (classConfigInfo.isOverlay)
							((ModClientEventsHandlerCommon) handler).removeOverlayListenerClass(classConfigInfo.overlayAdapter);
						else
							((ModClientEventsHandlerCommon) handler).removeScreenListenerClass(classConfigInfo.clazz);
					}
				} else if(handler instanceof ModClientEventsHandlerSpecific){
					if (configItem.enabled) {
						handler.register();
						((ModClientEventsHandlerSpecific) handler).setIMENameToSwitch(configItem.imeName);
					} else {
						if (handler instanceof OpenChatKeyBindingInputEventsHandler) {
							((ModClientEventsHandlerSpecific) handler).setIMENameToSwitch(null);
						} else {
							handler.unregister();
						}
					}
				}
			}
		}
	}
}
