package com.IMEautochange.compat.JEIPlugin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.KeyBinding;
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

import org.codehaus.plexus.util.Os;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.IMEautochange.IMEautochange;
import com.IMEautochange.jna.ModNativeMethods;
import com.IMEautochange.key.ModKeys;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IIngredientListOverlay;

@JEIPlugin
public class IMEautochangeJEIPlugin implements IModPlugin {
	static IIngredientListOverlay IngredientListOverlay;
//	static GuiScreen IngredientListOverlayGui;
//	static boolean isMouseClicked = true;
	static boolean isSearchFieldFocused = false;
	static boolean isFunctionEnabled = true;
	
	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
		if(Os.isFamily(Os.FAMILY_WINDOWS)) {
			IMEautochange.logger.debug("JEI support enabled.");
			this.IngredientListOverlay = jeiRuntime.getIngredientListOverlay();
			MinecraftForge.EVENT_BUS.register(this);
		}
	}
	
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent//(priority = EventPriority.LOWEST)
	public void onClientTickEvent(ClientTickEvent event) 
	{
		if(isFunctionEnabled) 
		{
			if(isSearchFieldFocused) 
			{
				if(!IngredientListOverlay.hasKeyboardFocus()) 
				{
					isSearchFieldFocused = false;
					ModNativeMethods.SetKLen_us();
				}
			}
			else 
			{
				if(IngredientListOverlay.hasKeyboardFocus()) 
				{
					isSearchFieldFocused=true;
					ModNativeMethods.SetKLzh_cn();
				}
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onHotKeyInputEvent(InputEvent.KeyInputEvent event)
    {
        if (ModKeys.toggleJEISupport.isPressed())
        {
//        	Minecraft.getMinecraft().ingameGUI.getChatGUI().addToSentMessages("JEItoggled");
        	isFunctionEnabled = !isFunctionEnabled;
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
