package com.imeautochange.event;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EventsHandlerManager {
	public static final HashMap<String, ModClientEventsHandlerBase> handlerMap = new HashMap<String, ModClientEventsHandlerBase>();
//	private static ArrayList<ModClientEventsHandlerBase> eventsHandlers = new ArrayList<ModClientEventsHandlerBase>();
	
	private static final Logger LOGGER = LogManager.getLogger();
	
//	public static ArrayList<ModClientEventsHandlerBase> getEventsHandlerList() {
//		return eventsHandlers;
//	}
	
	public static HashMap<String, ModClientEventsHandlerBase> getEventsHandlers(){
		return handlerMap;
	}
	
	/**
	 * This method registers a handler with generated default name, which will be
	 * displayed on IMEautochange's ConfigScreen. Use the other method if you want
	 * to specify the displayed name(description) of your handler.
	 * 
	 * @param handler
	 */
	public static void registerHandler(ModClientEventsHandler handler) {
		registerHandler("Enable function of " + handler.getClass().getName(), handler);
	}

	public static void registerHandler(String description, ModClientEventsHandlerBase handler) {
//		handler.HANDLER_ID = eventsHandlers.size();
//		eventsHandlers.add(handler.HANDLER_ID, handler);
		handlerMap.put(description, handler);
	}

	public static ModClientEventsHandlerBase getHandlerIdByDescription(String description) {
		return handlerMap.get(description);
	}
}
