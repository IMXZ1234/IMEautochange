package com.IMEautochange.key;

import net.java.games.input.Component.Identifier.Key;
import org.lwjgl.input.Keyboard;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ModKeys 
{
	public static KeyBinding openChat;
	public static KeyBinding toggleJEISupport;

    public static void BindKeys()
    {
//    	ModKeys.toggleJEISupport = new KeyBinding("key.imeautochange.toggleJEISupport", KeyConflictContext.IN_GAME, Keyboard.KEY_J, "key.categories.imeautochange");
    	ModKeys.openChat = new KeyBinding("key.imeautochange.openChatWithIMEToggled", KeyConflictContext.IN_GAME, Keyboard.KEY_RETURN, "key.categories.imeautochange");
    }
    
    public static void registerKeys() {
    	BindKeys();
    	//ClientRegistry.registerKeyBinding(closechat);
    	ClientRegistry.registerKeyBinding(openChat);
//    	ClientRegistry.registerKeyBinding(toggleJEISupport);
    }
}
