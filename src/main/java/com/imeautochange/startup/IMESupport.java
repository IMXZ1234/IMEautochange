package com.imeautochange.startup;

import com.imeautochange.ModFunctionManager;
import com.imeautochange.config.ClassConfigInfo;
import com.imeautochange.config.ConfigManager;
import com.imeautochange.event.EventsHandlerManager;
import com.imeautochange.event.ModClientEventsHandler;
import com.imeautochange.event.ModClientEventsHandlerBase;

public abstract class IMESupport {
	public static final String DESCRIPTION_MOUSEINPUTAWARE = "Enable Switching on Mouse Click";
	public static final String DESCRIPTION_KEYBOARDINPUTINPUTAWARE = "Enable Switching on Key Pressed";
	public static final String DESCRIPTION_GUIOPENAWARE = "Enable Switching on GUI Open";
	
	public static String defaultIMEName = ModFunctionManager.getDefaultIMEName();
	public static String englishIMEName = ModFunctionManager.getEnglishIMEName();

	public String[] handlerDescriptions;
	public ModClientEventsHandlerBase[] handlers;
	public ClassConfigInfo[] classConfigInfos;
	
	public final void initIMESupport() {
		initMembers();
		int handlerNum = handlers.length;
		if (handlerNum > handlerDescriptions.length) {
			handlerNum = handlerDescriptions.length;
		}
		for (int i = 0; i < handlerNum; i++) {
			EventsHandlerManager.registerHandler(handlerDescriptions[i], handlers[i]);
		}
		for (int i = 0; i < classConfigInfos.length; i++) {
			ConfigManager.registerClassConfigInfo(classConfigInfos[i]);
		}
	}
	
	public abstract void initMembers();
}
