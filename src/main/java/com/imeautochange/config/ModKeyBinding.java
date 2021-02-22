package com.imeautochange.config;

import org.lwjgl.glfw.GLFW;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ModKeyBinding {
	public static final KeyBinding OPEN_CONFIG = new KeyBinding("Open Config GUI",
            KeyConflictContext.IN_GAME,
            KeyModifier.CONTROL,
            InputMappings.Type.KEYSYM,
            GLFW.GLFW_KEY_J,
            "key.category.imeautochange");
	public static final KeyBinding OPEN_CHAT_WITH_IME_TOGGLED = new KeyBinding("Open chat with IME toggled",
			KeyConflictContext.IN_GAME,
			KeyModifier.NONE,
			InputMappings.Type.KEYSYM,
			GLFW.GLFW_KEY_ENTER,
			"key.category.imeautochange");
	public static void registerKeyBinding() {
		ClientRegistry.registerKeyBinding(OPEN_CONFIG);
		ClientRegistry.registerKeyBinding(OPEN_CHAT_WITH_IME_TOGGLED);
	}
}
