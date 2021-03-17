package com.imeautochange.gui;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class ConfigSelectScreen extends Screen {
	
	public static final TranslationTextComponent title = new TranslationTextComponent("imeautochange.gui.configscreen.title.configselectscreen");
	private Button backToGameButton;
	private Button functionConfig;
	private Button defaultIMEConfig;

	public ConfigSelectScreen(ITextComponent titleIn) {
		super(titleIn);
	}
	
	@Override
    protected void init() {
		this.functionConfig = new Button(this.width / 2 - 120, this.height / 2 - 40, 240, 20, new TranslationTextComponent("imeautochange.gui.configscreen.button.functionconfig"), (button)->{
			Minecraft.getInstance()
			.displayGuiScreen(new FunctionConfigScreen(new TranslationTextComponent("imeautochange.gui.screen.functionconfigscreen")));
		});
		this.addButton(functionConfig);
		
		this.defaultIMEConfig = new Button(this.width / 2 - 120, this.height / 2 - 10, 240, 20, new TranslationTextComponent("imeautochange.gui.configscreen.button.defaultimeconfig"), (button)->{
			Minecraft.getInstance()
			.displayGuiScreen(new DefaultIMESelectionConfigScreen(new TranslationTextComponent("imeautochange.gui.screen.configselectscreen")));
		});
		this.addButton(defaultIMEConfig);
		
        this.backToGameButton = new Button(this.width / 2 - 120, this.height / 2 + 20, 240, 20, new TranslationTextComponent("imeautochange.gui.configscreen.button.backtogame"), (button)-> {
        	Minecraft.getInstance()
			.displayGuiScreen((Screen) null);
        });
        this.addButton(backToGameButton);
        
		super.init();
	}
	
	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.renderDirtBackground(0);
		font.func_238422_b_(matrixStack, title.func_241878_f(), (this.width - font.getStringPropertyWidth(title)) / 2, 26, 0xFFFFFF);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
	}

}
