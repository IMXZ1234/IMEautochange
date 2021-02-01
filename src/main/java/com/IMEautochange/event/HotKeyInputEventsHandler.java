package com.IMEautochange.event;

import com.IMEautochange.config.ModKeys;
import com.IMEautochange.nativefunction.ModFunctionManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class HotKeyInputEventsHandler extends ModClientEventsHandler {
	public static final String keyInfoKLListReloaded = "text.imeautochange.infoKLListReloaded";
	/**
	 * Handles HotKey inputs.
	 * 
	 * @param event
	 */
	@SideOnly(Side.CLIENT)
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onHotKeyInputEvent(InputEvent.KeyInputEvent event) {
		// Chat is to be opened
		if (ModKeys.openChat.isPressed()) {
			ModFunctionManager.switchLanguageTo(ModFunctionManager.LANGUAGE_INGUI);
			Minecraft.getMinecraft().displayGuiScreen(new GuiChat());
		} else if (ModKeys.reloadLanguageList.isPressed()) {
			ModFunctionManager.reloadLanguageList(false);
			Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentTranslation(keyInfoKLListReloaded));
		}
	}
}
