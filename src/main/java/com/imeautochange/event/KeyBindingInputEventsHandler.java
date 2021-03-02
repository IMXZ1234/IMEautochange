package com.imeautochange.event;

import com.imeautochange.IMEautochange;
import com.imeautochange.config.ConfigScreen;
import com.imeautochange.config.ModKeyBinding;

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
		if (ModKeyBinding.OPEN_CONFIG.isPressed()) {
			Minecraft.getInstance()
					.displayGuiScreen(new ConfigScreen(new TranslationTextComponent(IMEautochange.MOD_ID + ".test")));
		}

	}
	
}
