package com.imeautochange.gui.widget;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SelectableStaticTextField extends TextFieldWidget {
	private boolean isSelected;
	protected final SelectableStaticTextField.ISelectable onSelect;
	private String Id;
	
	public SelectableStaticTextField(FontRenderer p_i232260_1_, int p_i232260_2_, int p_i232260_3_, int p_i232260_4_,
			int p_i232260_5_, ITextComponent p_i232260_6_, SelectableStaticTextField.ISelectable onSelect, String Id) {
		super(p_i232260_1_, p_i232260_2_, p_i232260_3_, p_i232260_4_, p_i232260_5_, p_i232260_6_);
		this.onSelect = onSelect;
		this.Id = Id;
	}

	public SelectableStaticTextField(FontRenderer p_i232259_1_, int p_i232259_2_, int p_i232259_3_, int p_i232259_4_,
			int p_i232259_5_, TextFieldWidget p_i232259_6_, ITextComponent p_i232259_7_, SelectableStaticTextField.ISelectable onSelect, String Id) {
		super(p_i232259_1_, p_i232259_2_, p_i232259_3_, p_i232259_4_, p_i232259_5_, p_i232259_6_, p_i232259_7_);
		this.onSelect = onSelect;
		this.Id = Id;
	}
	
	public boolean isSelected() {
		return isSelected;
	}
	
	@Override
	public boolean canWrite() {
		return false;
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if(isMouseOver(mouseX, mouseY) && button == 0) {
			this.onSelect.onSelected(this);
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}
	
	@OnlyIn(Dist.CLIENT)
	public interface ISelectable {
		void onSelected(SelectableStaticTextField staticTextField);
	}
	
	public String getId() {
		return Id;
	}
	public void setId(String Id) {
		this.Id = Id;
	}
}
