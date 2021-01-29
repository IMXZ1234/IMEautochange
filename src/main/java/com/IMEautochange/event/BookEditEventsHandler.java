package com.IMEautochange.event;

import com.IMEautochange.nativefunction.ModFunctionManager;

import net.minecraft.client.gui.GuiScreenBook;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BookEditEventsHandler extends ModClientEventsHandler {
	@SubscribeEvent
	public void onGuiScreenBookOpenEvent(GuiOpenEvent event) {
		if (event.getGui() instanceof GuiScreenBook) {
			ModFunctionManager.switchLanguageTo(ModFunctionManager.LANGUAGE_INGUI);
		}
	}
}
