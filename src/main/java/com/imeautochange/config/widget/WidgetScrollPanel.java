package com.imeautochange.config.widget;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.client.gui.ScrollPanel;

public class WidgetScrollPanel extends ScrollPanel{

	public WidgetScrollPanel(Minecraft client, int width, int height, int top, int left) {
		super(client, width, height, top, left);
	}

	@Override
	protected int getContentHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected void drawPanel(MatrixStack mStack, int entryRight, int relativeY, Tessellator tess, int mouseX,
			int mouseY) {
		// TODO Auto-generated method stub
		
	}

}
