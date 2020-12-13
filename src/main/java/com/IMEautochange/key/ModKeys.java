package com.IMEautochange.key;

import net.java.games.input.Component.Identifier.Key;
import org.lwjgl.input.Keyboard;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ModKeys {
	//public static KeyBinding closechat;
	public static KeyBinding openchat;

    public static void BindKeys()
    {
    	//ModKeys.closechat = new KeyBinding("key.imeautochange.closechat", KeyConflictContext.UNIVERSAL, Keyboard.KEY_ESCAPE, "key.categories.imeautochange");
    	ModKeys.openchat = new KeyBinding("key.imeautochange.openchatwithIMEtoggled", KeyConflictContext.UNIVERSAL, Keyboard.KEY_RETURN, "key.categories.imeautochange");
    }
    
    public static void registerKeys() {
    	BindKeys();
    	//ClientRegistry.registerKeyBinding(closechat);
    	ClientRegistry.registerKeyBinding(openchat);
    }
}
