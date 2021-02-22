package com.imeautochange.event;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Map.Entry;

import com.imeautochange.compat.ICompatOverlayAdapter;
import com.imeautochange.nativefunction.NativeFunctionManager;
import com.imeautochange.util.MouseOverUtil;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraftforge.client.event.GuiScreenEvent.MouseClickedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class MouseEventsHandler extends ModClientEventsHandler {
	private ArrayList<TextFieldWidget> textFieldWidgets = new ArrayList<TextFieldWidget>();
	@SubscribeEvent
	public void onMouseClickedEvent(MouseClickedEvent event) {
		Screen screen = event.getGui();
		Class<?> screenClass = screen.getClass();
		System.out.println(screenClass.getName());
		double mouseX = event.getMouseX();
		double mouseY = event.getMouseY();
		ICompatOverlayAdapter overlayAdapter;
		for(Entry<ICompatOverlayAdapter, String> entry : overlayIMETable.entrySet()) {
			overlayAdapter = entry.getKey();
			if(overlayAdapter.isOverlayDisplayed()) {
				overlayAdapter.getTextFieldWidgets(textFieldWidgets);
				for (TextFieldWidget textField : textFieldWidgets) {
					System.out.println("textField!");
					if (textField.getVisible()) {
						System.out.println("visible!");
						if (MouseOverUtil.isMouseOverTextFieldWidget(textField, mouseX, mouseY)) {
							System.out.println("in!");
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
				System.out.println("textField!");
				System.out.println("field name "+field.getName());
				if (textField.getVisible()) {
					System.out.println("visible!");
					if (MouseOverUtil.isMouseOverTextFieldWidget(textField, mouseX, mouseY)) {
						System.out.println("in!");
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
