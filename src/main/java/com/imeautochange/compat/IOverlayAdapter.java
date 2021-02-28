package com.imeautochange.compat;

import java.util.ArrayList;

import net.minecraft.client.gui.widget.TextFieldWidget;

/**
 * The adapter provides all the information about a certain overlay IMEautochange needs to know.
 * @author IMXZ
 *
 */
public interface IOverlayAdapter {
	boolean isOverlayDisplayed();
	/**
	 * 
	 * @return The Class Object of the Overlay this adapter represents.
	 */
	Class<?> getOverlayClass();
	boolean getTextFieldWidgets(ArrayList<TextFieldWidget> textFieldWidgets);
}
