package com.IMEautochange.event;

import org.lwjgl.input.Keyboard;

import com.IMEautochange.jna.ModNativeMethods;
import com.IMEautochange.key.ModKeys;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent.KeyboardInputEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;




@Mod.EventBusSubscriber(Side.CLIENT)
public class ModClientEvents {
	
	/*
	 * If registered hotkey(openchat with IME toggled) is pressed, change keyboard layout to zh_cn
	 * If chat gui is close, either by pressing enter or esc in chat gui screen, change keyboard layout to en_us
	 */
	//Chat gui is opened
	@SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onHotKeyInputEvent(InputEvent.KeyInputEvent event)
    {
        if (ModKeys.openChat.isPressed())
        {
//        	System.out.println("openchat pressed!");
        	//ModNativeMethods.ToggleIME();
        	ModNativeMethods.SetKLzh_cn();
			Minecraft.getMinecraft().displayGuiScreen(new GuiChat());
		}
    }
	//Chat gui is closed
	@SideOnly(Side.CLIENT)
	@SubscribeEvent//(priority = EventPriority.HIGHEST)
	public static void onGuiChatKeyboardInputEvent(KeyboardInputEvent.Pre event)
	{
		if(Minecraft.getMinecraft().currentScreen instanceof GuiChat) {
//			if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
//				System.out.println("chat close by esc!");
//				//ModNativeMethods.ToggleIME();
//				ModNativeMethods.SetKLen_us();
//			}else if(Keyboard.isKeyDown(Keyboard.KEY_RETURN)) {
//				System.out.println("chat close by esc!");
//				ModNativeMethods.SetKLen_us();
//			}
			if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) || Keyboard.isKeyDown(Keyboard.KEY_RETURN)) {
				ModNativeMethods.SetKLen_us();
			}
		}
	     
	}
}
