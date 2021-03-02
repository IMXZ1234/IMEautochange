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
import com.imeautochange.config.ConfigItem;
import com.imeautochange.config.ConfigManager;
import com.imeautochange.config.IMEInfo;
import com.imeautochange.config.ModKeyBinding;
import com.imeautochange.event.EventsHandlerManager;
import com.imeautochange.event.KeyBindingInputEventsHandler;
import com.imeautochange.event.ModClientEventsHandler;
import com.imeautochange.event.ModClientEventsHandlerBase;
import com.imeautochange.event.ModClientEventsHandlerSpecific;
import com.imeautochange.event.OpenChatKeyBindingInputEventsHandler;
import com.imeautochange.nativefunction.NativeFunctionManager;
import com.imeautochange.startup.BuiltInSupport;
import com.imeautochange.startup.IMESupportManager;
import com.imeautochange.util.ReflectionUtil;

import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ModFunctionManager {
	private static HashMap<Class<?>, ClassConfigInfo> listenerClassConfigInfo;
	private static boolean modFunctionEnabled = false;
	private static KeyBindingInputEventsHandler keyBindingInputEventsHandler;
	private static HashMap<String, ModClientEventsHandlerBase> eventsHandlers;
	private static ArrayList<IMEInfo> imeInfoList;
	private static String defaultIMEName;
	private static String englishIMEName;
	
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
		defaultIMEName = NativeFunctionManager.getDefaultIME().name;
		englishIMEName = NativeFunctionManager.getEnglishIME().name;
		// Already reloaded during initialization of NativeFunctionManager.
		imeInfoList = NativeFunctionManager.getIMEInfoList(false);
		modFunctionEnabled = true;
		// Initialization
		IMESupportManager.registerIMESupport(new BuiltInSupport());
		IMESupportManager.registerIMESupport(new BuiltInModSupport());
		IMESupportManager.initAllIMESupports();
		// Initialize members(get a reference from other managers)
		listenerClassConfigInfo = ConfigManager.getListenerClassConfigInfo();
		eventsHandlers = EventsHandlerManager.getEventsHandlers();
		// Load Configuration
		ConfigManager.updateConfigFromFile();
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
	    NativeFunctionManager.switchIMETo(imeInfoList.get(0));
	}
	
	public static HashMap<Class<?>, ClassConfigInfo> getListenerClassConfigInfo() {
		return listenerClassConfigInfo;
	}
	
	public static ArrayList<IMEInfo> getIMEInfoList(){
		return imeInfoList;
	}
	
	public static String getDefaultIMEName() {
		return defaultIMEName;
	}
	
	public static String getEnglishIMEName() {
		return englishIMEName;
	}
	
	public static void updateHandlersCachedFieldList() {
		LOGGER.info("Caching Reflection Fields...");
		for(Entry<Class<?>, ClassConfigInfo> classConfigInfoEntry : listenerClassConfigInfo.entrySet()) {
			ClassConfigInfo classConfigInfo = classConfigInfoEntry.getValue();
				ArrayList<Field> classFields = new ArrayList<Field>();
				if(classConfigInfo.isOverlay) {
					if(ReflectionUtil.getFieldList(classConfigInfo.overlayAdapter.getOverlayClass(), TextFieldWidget.class, classFields)) {
						for(String handlerDescription : classConfigInfo.configItems.keySet()) {
							ModClientEventsHandlerBase handler = EventsHandlerManager.getHandlerIdByDescription(handlerDescription);
							if(handler instanceof ModClientEventsHandler) {
								((ModClientEventsHandler)handler).cachedOverlayFieldList.put(classConfigInfo.overlayAdapter, classFields);
							}
						}
					}
				}else {
					if(ReflectionUtil.getFieldList(classConfigInfo.clazz, TextFieldWidget.class, classFields)) {
						for(String handlerDescription : classConfigInfo.configItems.keySet()) {
							ModClientEventsHandlerBase handler = EventsHandlerManager.getHandlerIdByDescription(handlerDescription);
							if(handler instanceof ModClientEventsHandler) {
								((ModClientEventsHandler)handler).cachedScreenFieldTable.put(classConfigInfo.clazz, classFields);
							}
						}
					}
				}
		}
	}
	
//	public static void loadConfigFromFile() {
//		ConfigManager.loadFromConfigFile();
//	}
	
	public static void updateHandlersListenerTable(HashMap<Class<?>, ClassConfigInfo> cachedChanges) {
		LOGGER.info("Updating Handlers Internal table...");
		for (Entry<Class<?>, ClassConfigInfo> classConfigInfoEntry : cachedChanges.entrySet()) {
			updateHandlersListenerTable(classConfigInfoEntry.getValue());
		}
	}

	public static void updateHandlersListenerTable(ClassConfigInfo classConfigInfo) {
		for (Entry<String, ConfigItem> configItemsEntry : classConfigInfo.configItems.entrySet()) {
			ModClientEventsHandlerBase handler = EventsHandlerManager.getHandlerIdByDescription(configItemsEntry.getKey());
			if (handler != null) {
				ConfigItem configItem = configItemsEntry.getValue();
				if (handler instanceof ModClientEventsHandler) {
					if (configItem.enabled) {
						if (classConfigInfo.isOverlay)
							((ModClientEventsHandler) handler).addOverlayListenerClass(classConfigInfo.overlayAdapter, configItem.imeName);
						else
							((ModClientEventsHandler) handler).addScreenListenerClass(classConfigInfo.clazz, configItem.imeName);
					} else {
						if (classConfigInfo.isOverlay)
							((ModClientEventsHandler) handler).removeOverlayListenerClass(classConfigInfo.overlayAdapter);
						else
							((ModClientEventsHandler) handler).removeScreenListenerClass(classConfigInfo.clazz);
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
