package com.IMEautochange.config;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ModKeys {
	public static KeyBinding openChat;
	public static KeyBinding reloadLanguageList;

	public static void init() {
		bindKeys();
		registerKeys();
	}

	public static void bindKeys() {
		openChat = new KeyBinding("key.imeautochange.openChatWithIMEToggled", KeyConflictContext.IN_GAME,
				Keyboard.KEY_RETURN, "key.categories.imeautochange");
		reloadLanguageList = new KeyBinding("key.imeautochange.reloadLanguageList",
				KeyConflictContext.IN_GAME, Keyboard.KEY_NONE, "key.categories.imeautochange");
	}

	public static void registerKeys() {
		ClientRegistry.registerKeyBinding(openChat);
		ClientRegistry.registerKeyBinding(reloadLanguageList);
	}
}
