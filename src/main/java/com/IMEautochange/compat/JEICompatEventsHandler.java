package com.IMEautochange.compat;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.IMEautochange.IMEautochange;
import com.IMEautochange.config.ModConfig;
import com.IMEautochange.event.ModClientEventsManager;
import com.IMEautochange.nativefunction.ModFunctionManager;
import com.IMEautochange.util.MouseOverUtil;
import com.IMEautochange.util.ReflectionConstants;
import com.IMEautochange.util.ReflectionUtil;

import mezz.jei.api.IIngredientListOverlay;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JEIPlugin;
import mezz.jei.gui.overlay.IngredientListOverlay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiRepair;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@JEIPlugin
public class JEICompatEventsHandler extends ModCompatEventsHandler implements IModPlugin {
	/**
	 * JEI IngredientListOverlay is not a Gui! Surprising!
	 */
	private static IngredientListOverlay IngredientListOverlay;
	/**
	 * When these prefixes are typed in JEI IngredientListOverlay search box, IME
	 * should be switched to IME in Game.
	 */
	public final char[] JEIPrefixList = new char[] { '^', '@', '$', '&' };
	public final int JEIPrefixListLen = 4;

	/**
	 * On startup, obtain JEI IngredientListOverlay instance.
	 */
	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
		IngredientListOverlay = (IngredientListOverlay)jeiRuntime.getIngredientListOverlay();
	}
	
	/**
	 * Should be called after IME switching by Anvil/CreativeInventory event
	 * listeners. Since Anvil event listeners acts after JEI logic, naturally this
	 * is also called after JEI logic.
	 * 
	 * @param event
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 */
	@SideOnly(Side.CLIENT)
	@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
	public void onIngredientListOverlayMouseInputEvent(GuiScreenEvent.MouseInputEvent event)
			throws NoSuchFieldException, IllegalAccessException {
		if (IngredientListOverlay.isListDisplayed()) {
			GuiTextField ingredientListOverlayTextField = (GuiTextField) ReflectionUtil.getPrivateField(
					IngredientListOverlay.class, ReflectionConstants.IngredientListOverlayGuiTextFieldName,
					IngredientListOverlay);
			GuiScreen guiScreen = Minecraft.getMinecraft().currentScreen;
			/* if mouse clicked */
			if (Mouse.getEventButtonState()) {
				if (!MouseOverUtil.isMouseOverGuiTextField(guiScreen, ingredientListOverlayTextField)) {
					/* Deal with situations that there are two GuiTextField being displayed. */
					if (guiScreen instanceof GuiRepair) {
						GuiTextField guiRepairTextField = (GuiTextField) ReflectionUtil.getPrivateField(GuiRepair.class,
								ReflectionConstants.GuiRepairGuiTextFieldName, guiScreen);
						/*
						 * Here, logic by JEI and other event listeners in this mod has already been
						 * performed.
						 */
						if (MouseOverUtil.isMouseOverGuiTextField(guiScreen, guiRepairTextField)) {
							if (ModClientEventsManager.isAnvilEventsHandlerRegistered()) {
								ModFunctionManager.switchLanguageTo(ModFunctionManager.LANGUAGE_INGUI);
								return;
							}
						} else {
							/*
							 * If guiRepairTextField will not lose focus due to event being canceled by JEI,
							 * just leave IME as it is. Otherwise fall back to default.
							 */
							if (event.isCanceled() && guiRepairTextField.isFocused()) {
								return;
							}
						}
					} else if (guiScreen instanceof GuiContainerCreative) {
						GuiTextField guiContainerCreativeTextField = (GuiTextField) ReflectionUtil.getPrivateField(
								GuiContainerCreative.class, ReflectionConstants.GuiContainerCreativeGuiTextFieldName,
								guiScreen);
						if (guiContainerCreativeTextField.isFocused()) {
							if (MouseOverUtil.isMouseOverGuiTextField(guiScreen, guiContainerCreativeTextField)) {
								if (ModClientEventsManager.isCreativeInventoryEventsHandlerRegistered()) {
									ModFunctionManager.switchLanguageTo(ModFunctionManager.LANGUAGE_INGUI);
									return;
								}
							}
						}
					}
					/*
					 * Default situation. It is most likely that mouse clicks outside both
					 * GuiTextFields.
					 */
					ModFunctionManager.switchLanguageTo(ModFunctionManager.LANGUAGE_INGAME);

				} else {
					ModFunctionManager.switchLanguageTo(ModFunctionManager.LANGUAGE_INGUI);
				}
			}
		}
	}
	
	/**
	 * Switch IME to IME in Game if characters in JEIPrefixList are typed.
	 * 
	 * @param event
	 */
	@SideOnly(Side.CLIENT)
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onIngredientListOverlayKeyInputEvent(GuiScreenEvent.KeyboardInputEvent event) {
		if (IngredientListOverlay.hasKeyboardFocus()) {
			int eventKey = Keyboard.getEventKey();
			/*Keyboard Input ESCAPE/ENTER, IngredientListOverlay will lose focus.*/
			if(eventKey == Keyboard.KEY_ESCAPE || eventKey == Keyboard.KEY_RETURN) {
				
			}
			for (int i = 0; i < JEIPrefixListLen; i++) {
				if (Keyboard.getEventCharacter() == JEIPrefixList[i]) {
					ModFunctionManager.switchLanguageTo(ModFunctionManager.LANGUAGE_INGAME);
					return;
				}
			}
		}
	}
}