package com.imeautochange.event;


import com.imeautochange.nativefunction.NativeFunctionManager;

import net.minecraft.client.gui.IngameGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class GuiOpenEventsHandler extends ModClientEventsHandler {
	public static String IngameGuiIMEName;

	@SubscribeEvent
	public void onGuiOpenEvent(GuiOpenEvent event) {
		Screen screen = event.getGui();
		if(screen == null) {
			NativeFunctionManager.switchIMETo(IngameGuiIMEName);
			return;
		}
		Class<?> screenClass = screen.getClass();
		String imeInfo = screenIMETable.get(screenClass);
		if(imeInfo != null) {
			NativeFunctionManager.switchIMETo(imeInfo);
		}
	}
	
	@Override
	public int addScreenListenerClass(Class<?> clazz, String imeName) {
		if(clazz.isAssignableFrom(IngameGui.class)){
			IngameGuiIMEName = imeName;
		}
		return super.addScreenListenerClass(clazz, imeName);
	}
	
	@Override
	public int removeScreenListenerClass(Class<?> clazz) {
		if(clazz.isAssignableFrom(IngameGui.class)){
			IngameGuiIMEName = null;
		}
		return super.removeScreenListenerClass(clazz);
	}
}
