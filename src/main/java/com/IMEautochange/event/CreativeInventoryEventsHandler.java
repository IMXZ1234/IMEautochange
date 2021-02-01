package com.IMEautochange.event;

import java.lang.reflect.InvocationTargetException;

import org.lwjgl.input.Mouse;

import com.IMEautochange.nativefunction.ModFunctionManager;
import com.IMEautochange.util.MouseOverUtil;
import com.IMEautochange.util.ReflectionConstants;
import com.IMEautochange.util.ReflectionUtil;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CreativeInventoryEventsHandler extends ModClientEventsHandler {

	public class CreativeTabOpenEventsHandler extends ModClientEventsHandler {
		/**
		 * On opening GuiContainerCreative, switch IME if current tab has a GuiTextField
		 * 
		 * @param event
		 * @throws NoSuchFieldException
		 * @throws IllegalAccessException
		 */
		@SubscribeEvent
		public void onGuiContainerCreativeOpenEvent(GuiOpenEvent event)
				throws NoSuchFieldException, IllegalAccessException {
			GuiScreen guiScreen = event.getGui();
			if (guiScreen instanceof GuiContainerCreative) {
				int selectedTabIndex = ((GuiContainerCreative) guiScreen).getSelectedTabIndex();
				if (CreativeTabs.CREATIVE_TAB_ARRAY[selectedTabIndex].hasSearchBar()) {
					ModFunctionManager.switchLanguageTo(ModFunctionManager.LANGUAGE_INGUI);
				} else {
					ModFunctionManager.switchLanguageTo(ModFunctionManager.LANGUAGE_INGAME);
				}
			}
		}
		
		/**
		 * Handles mouse click over Creative Tabs, which will afterward cause tab change.
		 * 
		 * @param event
		 * @throws NoSuchFieldException
		 * @throws IllegalAccessException
		 */
		@SubscribeEvent
		public void onGuiContainerCreativeMouseInputEvent(GuiScreenEvent.MouseInputEvent event)
				throws NoSuchFieldException, IllegalAccessException {
			/* mouse button released */
			if (!Mouse.getEventButtonState() && Mouse.getEventButton() != -1) {
				GuiScreen guiScreen = event.getGui();
				if (guiScreen instanceof GuiContainerCreative) {
					int tabPage = (int) ReflectionUtil.getPrivateField(GuiContainerCreative.class,
							ReflectionConstants.GuiContainerCreativeTabPageName, guiScreen);
					for (CreativeTabs tab : CreativeTabs.CREATIVE_TAB_ARRAY) {
						if (MouseOverUtil.isMouseOverCreativeTabs(guiScreen, tab, tabPage)) {
							if (tab.hasSearchBar()) {
								ModFunctionManager.switchLanguageTo(ModFunctionManager.LANGUAGE_INGUI);
							}
							break;
						}
					}
				}
			}
		}
	}

	/**
	 * Handle mouse click in GuiContainerCreative, switch IME according to mouse
	 * position.
	 * 
	 * @param event
	 * @throws NoSuchFieldException
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	@SubscribeEvent(priority = EventPriority.LOW)
	public void onGuiContainerCreativeMouseInputEvent(GuiScreenEvent.MouseInputEvent event) throws NoSuchFieldException,
			NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		/* if mouse left button down */
		if (Mouse.getEventButtonState() && Mouse.getEventButton() == 0) {
			GuiScreen guiScreen = event.getGui();
			if (guiScreen instanceof GuiContainerCreative) {
				GuiTextField guiContainerCreativeTextField = (GuiTextField) ReflectionUtil.getPrivateField(
						GuiContainerCreative.class, ReflectionConstants.GuiContainerCreativeGuiTextFieldName,
						guiScreen);
				/*
				 * if GuiTextField is focused, in other word, if in the search tab of
				 * GuiContainerCreative.
				 */
				if (guiContainerCreativeTextField.isFocused()) {
					if (MouseOverUtil.isMouseOverGuiTextField(guiScreen, guiContainerCreativeTextField)) {
						ModFunctionManager.switchLanguageTo(ModFunctionManager.LANGUAGE_INGUI);
					} else {
						ModFunctionManager.switchLanguageTo(ModFunctionManager.LANGUAGE_INGAME);
					}
				}
			}
		}
	}
}
