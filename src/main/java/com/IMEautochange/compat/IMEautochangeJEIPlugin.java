package com.IMEautochange.compat;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.GuiScreenEvent.KeyboardInputEvent;
import net.minecraftforge.client.event.GuiScreenEvent.MouseInputEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//import org.codehaus.plexus.util.Os;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.IMEautochange.IMEautochange;
import com.IMEautochange.config.ModConfig;
import com.IMEautochange.jna.IMEChangeManager;
import com.IMEautochange.jna.ModNativeMethods;
import com.IMEautochange.key.ModKeys;
import com.IMEautochange.util.OSChecker;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IIngredientListOverlay;

@JEIPlugin
public class IMEautochangeJEIPlugin implements IModPlugin 
{
	/**
	 * JEI IngredientListOverlay is not a Gui! Surprising!
	 */
	private static IIngredientListOverlay IngredientListOverlay;
	private static boolean isIngredientListOverlayGuiTextFieldFocused = false;
//	public final char[] JEIPrefixList = new char[] {'^','%','@','$','&','#'};
//	public final int JEIPrefixListLen = 6;
	public final char[] JEIPrefixList = new char[] {'^','@','$','&'};
	public final int JEIPrefixListLen = 4;
	
	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
		if (OSChecker.isWindows()) {
			this.IngredientListOverlay = jeiRuntime.getIngredientListOverlay();
			MinecraftForge.EVENT_BUS.register(this);
			IMEautochange.logger.info("JEI support is available.");
		}
	}
	
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent//(priority = EventPriority.LOWEST)
	public void onIngredientListOverlayClientTickEvent(ClientTickEvent event) 
	{
		if (ModConfig.isFunctionEnabledJEISupport) 
		{
			if (isIngredientListOverlayGuiTextFieldFocused) 
			{
				if (!IngredientListOverlay.hasKeyboardFocus()) 
				{
					isIngredientListOverlayGuiTextFieldFocused = false;
					IMEChangeManager.switchKL(true);
				}
			}
			else 
			{
				if (IngredientListOverlay.hasKeyboardFocus()) 
				{
					isIngredientListOverlayGuiTextFieldFocused = true;
					IMEChangeManager.switchKL(false);
				}
			}
		}
	}
	
	/**
	 * When typing in JEI IngredientListOverlay search field, 
	 * Switch IME to IME in Game if characters like
	 * @param event
	 */
	@SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onHotKeyInputEvent(GuiScreenEvent.KeyboardInputEvent event)
    {
        if (IngredientListOverlay.hasKeyboardFocus())
        {
        	for (int i = 0; i < JEIPrefixListLen; i++) 
        	{
        		if (Keyboard.getEventCharacter() == JEIPrefixList[i])
	        	{
	        		IMEChangeManager.switchKL(true);
	        		return;
	        	}
        	}
		}
    }
	
//	@SideOnly(Side.CLIENT)
//	@SubscribeEvent//(priority = EventPriority.LOWEST)
//	public void onMouseEventGetIsClicked(MouseInputEvent.Pre event) {
//		//Mouse clicked to make the search box gain focus
//		if(Mouse.isButtonDown(0)) {
//			isMouseClicked=true;
//			//save current gui
////			IngredientListOverlayGui = event.getGui();
////			MinecraftForge.EVENT_BUS.post(new MouseInputEvent(event.getGui()));
//		}
//	}
////	@SideOnly(Side.CLIENT)
////	@SubscribeEvent//(priority = EventPriority.LOWEST)
////	public void onIngredientListOverlayMouseEvent(MouseInputEvent.Post event) {
////		//Mouse clicked right before the search box gains focus
////		//This function is expected to be called after the box's gaining focus
////		//due to its lowest priority
////		if(isMouseClicked) {
////			if(this.IngredientListOverlay.hasKeyboardFocus()) {
////				ModNativeMethods.SetKLzh_cn();
////				IngredientListOverlayGui = event.getGui();
////				isMouseClicked=false;
////			}
////			//Mouse clicked to select an item from IngredientListOverlay
////			else {
////				if(event.getGui().equals(IngredientListOverlayGui))
////					ModNativeMethods.SetKLen_us();
////			}
////			
////		}
////		
////	}
//	
//	@SideOnly(Side.CLIENT)
//	@SubscribeEvent//(priority = EventPriority.LOWEST)
//	public void onIngredientListOverlayKeyboardInputEvent(KeyboardInputEvent.Pre event)
//	{
////		System.out.println("in onIngredientListOverlayKeyboardInputEvent");
//		if(this.IngredientListOverlay.hasKeyboardFocus()) {
////			System.out.println("key nothasKeyboardFocus");
//			if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)
//					|| Keyboard.isKeyDown(Keyboard.KEY_RETURN)) {
//				ModNativeMethods.SetKLen_us();
//				isSearchFieldFocused=false;
//			}
//		}
//	}
	
}
