package com.imeautochange.event;

import com.imeautochange.config.ModKeyBinding;
import com.imeautochange.gui.ConfigSelectScreen;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.event.InputEvent.KeyInputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * This is not supposed to be unregistered.
 * @author IMXZ
 *
 */
public class KeyBindingInputEventsHandler extends ModClientEventsHandlerBase {
	@SubscribeEvent
	public void onKeyBindingInputEvent(KeyInputEvent event) {
//		System.out.println("onKeyBindingInputEvent");
		if (ModKeyBinding.OPEN_CONFIG.isPressed()) {
			System.out.println("OPEN_CONFIG");
			Minecraft.getInstance()
					.displayGuiScreen(new ConfigSelectScreen(new TranslationTextComponent("imeautochange.gui.screen.configselectscreen")));
		}
	}
	
}
