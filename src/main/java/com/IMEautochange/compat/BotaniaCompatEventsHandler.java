package com.IMEautochange.compat;

import org.lwjgl.input.Mouse;

import com.IMEautochange.config.ModConfig;
import com.IMEautochange.nativefunction.ModFunctionManager;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.client.gui.lexicon.GuiLexicon;
import vazkii.botania.client.gui.lexicon.GuiLexiconIndex;

public class BotaniaCompatEventsHandler extends ModCompatEventsHandler {
	/**
	 * Have to use class outside Botania API
	 */
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onGuiLexiconIndexOpenEvent(GuiOpenEvent event) {
		if (event.getGui() instanceof GuiLexiconIndex) {
			ModFunctionManager.switchLanguageTo(ModFunctionManager.LANGUAGE_INGUI);
		}
	}

	/**
	 * Other Lexicon Guis are opened, which are without a GuiTextField.
	 * 
	 * @param event
	 */
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onGuiLexiconWithoutTextFieldOpenEvent(GuiOpenEvent event) {
		GuiScreen guiScreen = event.getGui();
		if (!(guiScreen instanceof GuiLexiconIndex) && guiScreen instanceof GuiLexicon
				&& !((GuiLexicon) guiScreen).notesEnabled) {
			ModFunctionManager.switchLanguageTo(ModFunctionManager.LANGUAGE_INGAME);
		}
	}

	/**
	 * Handles Note open/close actions in the Lexicon.
	 * Called after Botania logic is performed.
	 * 
	 * @param event
	 */
	@SideOnly(Side.CLIENT)
	@SubscribeEvent(priority = EventPriority.LOW)
	public void onGuiLexiconMouseInputEvent(GuiScreenEvent.MouseInputEvent event) {
		/* mouse button released */
		if (Mouse.getEventButton() == 1) {
			GuiScreen guiScreen = event.getGui();
			if (guiScreen instanceof GuiLexicon) {
				if (((GuiLexicon) guiScreen).notesEnabled) {
					ModFunctionManager.switchLanguageTo(ModFunctionManager.LANGUAGE_INGUI);
				} else if (!((GuiLexicon) guiScreen).notesEnabled
						&& !(guiScreen instanceof GuiLexiconIndex)) {
					ModFunctionManager.switchLanguageTo(ModFunctionManager.LANGUAGE_INGAME);
				}
			}
		}

	}
}
