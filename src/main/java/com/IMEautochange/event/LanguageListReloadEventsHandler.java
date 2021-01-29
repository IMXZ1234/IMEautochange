package com.IMEautochange.event;

import com.IMEautochange.nativefunction.ModFunctionManager;

import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class LanguageListReloadEventsHandler extends ModClientEventsHandler {
	@SubscribeEvent
	public void onAnyGuiOpenEvent(GuiOpenEvent event) {
		ModFunctionManager.reloadLanguageList(false);
	}
}
