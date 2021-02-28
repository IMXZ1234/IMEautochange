package com.imeautochange.event;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Map.Entry;

import com.imeautochange.compat.IOverlayAdapter;
import com.imeautochange.nativefunction.NativeFunctionManager;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraftforge.client.event.GuiScreenEvent.MouseClickedEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class MouseEventsHandler extends ModClientEventsHandler {
	private ArrayList<TextFieldWidget> textFieldWidgets = new ArrayList<TextFieldWidget>();
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onMouseClickedEvent(MouseClickedEvent event) {
		if (event.getButton() == 0) {
			Screen screen = event.getGui();
			if(screen == null) {
				return;
			}
			Class<?> screenClass = screen.getClass();
			double mouseX = event.getMouseX();
			double mouseY = event.getMouseY();
			IOverlayAdapter overlayAdapter;
			for (Entry<IOverlayAdapter, String> entry : overlayIMETable.entrySet()) {
				overlayAdapter = entry.getKey();
				if (overlayAdapter.isOverlayDisplayed()) {
					overlayAdapter.getTextFieldWidgets(textFieldWidgets);
					for (TextFieldWidget textField : textFieldWidgets) {
						if (textField.getVisible()) {
							if (textField.isMouseOver(mouseX, mouseY)) {
								NativeFunctionManager.switchIMETo(overlayIMETable.get(entry.getKey()));
								return;
							}
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
					if (textField.getVisible()) {
						if (textField.isMouseOver(mouseX, mouseY)) {
							NativeFunctionManager.switchIMETo(imeName);
							return;
						}
					}
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
			if (GuiOpenEventsHandler.IngameGuiIMEName != null) {
				NativeFunctionManager.switchIMETo(GuiOpenEventsHandler.IngameGuiIMEName);
			}
		}
	}
}
