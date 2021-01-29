package com.IMEautochange.compat;

import com.IMEautochange.config.ModConfig;
import com.IMEautochange.nativefunction.ModFunctionManager;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.patchouli.client.book.gui.GuiBook;
import vazkii.patchouli.client.book.gui.GuiBookEntryList;

public class PatchouliCompatEventsHandler extends ModCompatEventsHandler {
	/**
	 * Have to use class outside Patchouli API
	 */
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onGuiBookEntryListOpenEvent(GuiOpenEvent event) {
		if (event.getGui() instanceof GuiBookEntryList) {
			ModFunctionManager.switchLanguageTo(ModFunctionManager.LANGUAGE_INGUI);
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onGuiBookWithoutTextFieldOpenEvent(GuiOpenEvent event) {
		GuiScreen guiScreen = event.getGui();
		if (guiScreen instanceof GuiBook && !(guiScreen instanceof GuiBookEntryList)) {
			ModFunctionManager.switchLanguageTo(ModFunctionManager.LANGUAGE_INGAME);
		}
	}
}
