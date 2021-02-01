package com.IMEautochange.event;

import org.lwjgl.input.Mouse;

import com.IMEautochange.nativefunction.ModFunctionManager;
import com.IMEautochange.util.MouseOverUtil;
import com.IMEautochange.util.ReflectionConstants;
import com.IMEautochange.util.ReflectionUtil;

import net.minecraft.client.gui.GuiRepair;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AnvilEventsHandler extends ModClientEventsHandler {
	private static boolean isGuiRepairGuiTextFieldFocused = false;
	
	/**
	 * Fix GuiTextField in GuiRepair not losing focus when opening other Gui(JEI
	 * RecipesGui e.g.) through mouse click. This is because MinecraftForge informs
	 * Event Listeners added by other mods of mouse input before vanilla logic is
	 * performed.
	 * 
	 * @param event
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 */
	@SubscribeEvent
	public void onGuiInGuiRepairGuiOpenEvent(GuiOpenEvent event) {
		/* not back to GuiInGame */
		if (event.getGui() != null) {
			/*
			 * other Gui opened in GuiRepair may be JEI Gui(most likely), etc.
			 */
			if (isGuiRepairGuiTextFieldFocused) {
				ModFunctionManager.switchLanguageTo(ModFunctionManager.LANGUAGE_INGAME);
				isGuiRepairGuiTextFieldFocused = false;
			}
		}
	}

	/**
	 * When mouse clicks inside GuiTextField in GuiRepair, switch IME to IME in Game, else switch to IME in Gui. 
	 * Note that if the mouse input event is handled by JEI first, then it will be cancelled if
	 * JEI acted on this event. In this case vanilla logic will not be performed, which means that GuiTextField
	 * keeps its original focus state.
	 * 
	 * @param event
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 */
	@SubscribeEvent(priority = EventPriority.LOW, receiveCanceled = true)
	public void onGuiRepairMouseInputEvent(GuiScreenEvent.MouseInputEvent event)
			throws NoSuchFieldException, IllegalAccessException {
		GuiScreen guiScreen = event.getGui();
		if (guiScreen instanceof GuiRepair) {
			GuiTextField guiRepairTextField = (GuiTextField) ReflectionUtil.getPrivateField(GuiRepair.class,
					ReflectionConstants.GuiRepairGuiTextFieldName, guiScreen);
			/* If mouse left/right button down. */
			if (Mouse.getEventButtonState()) {
				/*
				 * Here vanilla logic has not yet been performed. If guiRepairTextField is
				 * focused before current mouse input, it still remains being focused now.
				 */
				if (guiRepairTextField.isFocused()) {
					isGuiRepairGuiTextFieldFocused = true;
				}
				if(MouseOverUtil.isMouseOverGuiTextField(guiScreen, guiRepairTextField)) {
					ModFunctionManager.switchLanguageTo(ModFunctionManager.LANGUAGE_INGUI);
				} else {
					/*
					 * Only switched to IME In Game if event is not canceled,
					 * that is, JEI doesn't act on it. 
					 */
					if (!event.isCanceled()) {
						ModFunctionManager.switchLanguageTo(ModFunctionManager.LANGUAGE_INGAME);
					}
				}
			}
		}
	}
}
