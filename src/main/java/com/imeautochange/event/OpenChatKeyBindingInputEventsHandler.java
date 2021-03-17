package com.imeautochange.event;

import com.imeautochange.config.ModKeyBinding;
import com.imeautochange.nativefunction.NativeFunctionManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraftforge.client.event.InputEvent.KeyInputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Open chat KeyBinding, dealt with separately from other KeyBindingInputEvents.
 * @author IMXZ
 *
 */
public class OpenChatKeyBindingInputEventsHandler extends ModClientEventsHandlerSpecific {
	@SubscribeEvent
	public void onKeyBindingInputEvent(KeyInputEvent event) {
//		System.out.println("onKeyBindingInputEvent");
		if (ModKeyBinding.OPEN_CHAT_WITH_IME_TOGGLED.isPressed()) {
			System.out.println("OPEN_CHAT_WITH_IME_TOGGLED");
			Minecraft.getInstance().displayGuiScreen(new ChatScreen(""));
			NativeFunctionManager.switchIMETo(imeNameToSwitch);
		}
	}
	
}
