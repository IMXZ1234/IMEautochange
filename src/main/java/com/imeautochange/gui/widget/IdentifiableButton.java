package com.imeautochange.gui.widget;

import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;

public class IdentifiableButton extends Button{
	private String Id;
	public IdentifiableButton(int x, int y, int width, int height, ITextComponent title, IPressable pressedAction, String Id) {
		super(x, y, width, height, title, pressedAction);
		this.Id = Id;
	}
	
	public IdentifiableButton(int x, int y, int width, int height, ITextComponent title, Button.IPressable pressedAction, Button.ITooltip onTooltip, String Id) {
	      super(x, y, width, height, title, pressedAction, onTooltip);
	      this.Id = Id;
	}
	public String getId() {
		return Id;
	}
	public void setId(String Id) {
		this.Id = Id;
	}
}
