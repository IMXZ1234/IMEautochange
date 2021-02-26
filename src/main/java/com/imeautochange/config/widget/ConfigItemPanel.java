package com.imeautochange.config.widget;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.imeautochange.config.ConfigScreen;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.client.gui.ScrollPanel;

public class ConfigItemPanel extends ScrollPanel{
	public final int PADDING =6;
	private List<Widget> children = new LinkedList<Widget>();
	private ConfigScreen parent;
	
	public ConfigItemPanel(Minecraft client, int width, int height, int top, int left, ConfigScreen parent) {
		super(client, width, height, top, left);
		this.parent = parent;
	}

	@Override
	protected int getContentHeight() {
		int contentHeight = 0;
		for (Widget child:children) {
			contentHeight += child.getHeightRealms();
			contentHeight += PADDING;
		}
		return contentHeight;
	}

	@Override
	protected void drawPanel(MatrixStack mStack, int entryRight, int relativeY, Tessellator tess, int mouseX,
			int mouseY) {
		// TODO Auto-generated method stub
		
	}
	
	public void addWidget(Widget widget) {
		
	}

}
