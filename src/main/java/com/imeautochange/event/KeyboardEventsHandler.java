package com.imeautochange.event;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Map.Entry;

import org.lwjgl.glfw.GLFW;

import com.imeautochange.compat.IOverlayAdapter;
import com.imeautochange.nativefunction.NativeFunctionManager;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraftforge.client.event.GuiScreenEvent.KeyboardKeyPressedEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class KeyboardEventsHandler extends ModClientEventsHandler {
	ArrayList<TextFieldWidget> textFieldWidgets = new ArrayList<TextFieldWidget>();
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onKeyboardKeyPressedEvent(KeyboardKeyPressedEvent.Pre event) {
		int eventKey = event.getKeyCode();
		if(eventKey == GLFW.GLFW_KEY_ENTER || eventKey == GLFW.GLFW_KEY_ESCAPE) {
			Screen screen = event.getGui();
			if(screen == null) {
				return;
			}
			Class<?> screenClass = screen.getClass();
			IOverlayAdapter overlayAdapter;
			for(Entry<IOverlayAdapter, String> entry : overlayIMETable.entrySet()) {
				overlayAdapter = entry.getKey();
				if(overlayAdapter.isOverlayDisplayed()) {
					overlayAdapter.getTextFieldWidgets(textFieldWidgets);
					for (TextFieldWidget textField : textFieldWidgets) {
						if (textField.getVisible() && textField.isFocused()) {
							NativeFunctionManager.switchIMETo(overlayIMETable.get(entry.getKey()));
							return;
						}
					}
				}
			}
			String imeName = screenIMETable.get(screenClass);
			if (imeName == null) {
				return;
			}
			for (Field field : cachedScreenFieldTable.get(screenClass)) {
				TextFieldWidget textField;
				try {
					textField = (TextFieldWidget) field.get(screen);
					if (textField.getVisible() && textField.isFocused()) {
						NativeFunctionManager.switchIMETo(imeName);
						return;
					}
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
}
