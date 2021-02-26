package com.imeautochange.compat;

import java.util.ArrayList;

import net.minecraft.client.gui.widget.TextFieldWidget;

public interface IOverlayAdapter {
	boolean isOverlayDisplayed();
	Class<?> getOverlayClass();
	boolean getTextFieldWidgets(ArrayList<TextFieldWidget> textFieldWidgets);
}
