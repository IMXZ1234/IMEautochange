package com.IMEautochange.event;

import com.IMEautochange.config.ModConfig;
import com.IMEautochange.nativefunction.ModFunctionManager;

import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SignEditEventsHandler extends ModClientEventsHandler {
	@SubscribeEvent
	public void onGuiSignOpenEvent(GuiOpenEvent event) {
		if (event.getGui() instanceof GuiEditSign) {
			ModFunctionManager.switchLanguageTo(ModFunctionManager.LANGUAGE_INGUI);
		}
	}
}
