package com.IMEautochange.util;

import org.lwjgl.input.Mouse;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.creativetab.CreativeTabs;

public class MouseOverUtil {
	/**
	 * Checks if mouse is over a specific GuiTextField in some GuiScreen.
	 * 
	 * @param guiScreen
	 * @param guiTextField
	 * @return
	 */
	public static boolean isMouseOverGuiTextField(GuiScreen guiScreen, GuiTextField guiTextField) {
		int mouseX = Mouse.getEventX() * guiScreen.width / guiScreen.mc.displayWidth;
		int mouseY = guiScreen.height - Mouse.getEventY() * guiScreen.height / guiScreen.mc.displayHeight - 1;
		if (mouseX < guiTextField.x || mouseX > guiTextField.x + guiTextField.width || mouseY < guiTextField.y
				|| mouseY > guiTextField.y + guiTextField.height) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Checks if mouse is over a creative tab, if so that tab is going to be
	 * activated. Using similar code from GuiContainerCreative.isMouseOverTab().
	 * 
	 * @param guiScreen
	 * @param tab
	 * @return
	 */
	public static boolean isMouseOverCreativeTabs(GuiScreen guiScreen, CreativeTabs tab, int tabPage) {
		/* If is tab that always exists in the Creative Inventory Gui. */
		if (tab.getTabPage() != tabPage) {
			/* Normally this will not happen. */
			if (tab != CreativeTabs.SEARCH && tab != CreativeTabs.INVENTORY) {
				return false;
			}
		}
		int i = tab.getColumn();
		int j = 28 * i;
		int k = 0;

		if (tab.isAlignedRight()) {
			j = ((GuiContainerCreative) guiScreen).getXSize() - 28 * (6 - i) + 2;
		} else if (i > 0) {
			j += i;
		}

		if (tab.isOnTopRow()) {
			k = k - 32;
		} else {
			k = k + ((GuiContainerCreative) guiScreen).getYSize();
		}
		int mouseX = Mouse.getEventX() * guiScreen.width / guiScreen.mc.displayWidth
				- ((GuiContainer) guiScreen).getGuiLeft();
		int mouseY = guiScreen.height - Mouse.getEventY() * guiScreen.height / guiScreen.mc.displayHeight - 1
				- ((GuiContainer) guiScreen).getGuiTop();

		if (mouseX >= j && mouseX <= j + 28 && mouseY >= k && mouseY <= k + 32) {
			return true;
		} else {
			return false;
		}
	}
}
