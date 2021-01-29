package com.IMEautochange.event;

import com.IMEautochange.nativefunction.ModFunctionManager;

import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BackToGameEventsHandler extends ModClientEventsHandler {
	@SubscribeEvent
	public void onGuiInGameOpenEvent(GuiOpenEvent event) {
		if (event.getGui() == null) {
			ModFunctionManager.switchLanguageTo(ModFunctionManager.LANGUAGE_INGAME);
		}
	}
}
