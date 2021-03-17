package com.imeautochange.gui;

import java.util.HashMap;

import com.imeautochange.config.ConfigManager;
import com.imeautochange.config.GeneralConfigItem;
import com.imeautochange.gui.widget.IMEInfoEntryListWidget.IMEInfoEntry;
import com.imeautochange.gui.widget.SelectableStaticTextField;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class DefaultIMESelectionConfigScreen extends IMEInfoEntryListScreen {
	private SelectableStaticTextField selectedStatic;
	private Button confirmButton;
	private Button cancelButton;
	private SelectableStaticTextField textInputIMENameStatic;
	private SelectableStaticTextField gameControlInputIMENameStatic;
	private HashMap<String, GeneralConfigItem> configItems;
	private HashMap<String, GeneralConfigItem> cachedChanges;
	public int centerxPos = 0;
	
	protected DefaultIMESelectionConfigScreen(ITextComponent titleIn) {
		super(titleIn);
		configItems = ConfigManager.getGeneralConfigItems();
		cachedChanges = new HashMap<String, GeneralConfigItem>();
	}
	
	@Override
    public void setSelectedIMEInfoEntry(IMEInfoEntry entry)
    {
    	super.setSelectedIMEInfoEntry(entry);
        String imeName = entry.getIMEInfo().name;
		if (selectedStatic != null) {
			this.selectedStatic.setText(imeName);
			String description = selectedStatic.getId();
			GeneralConfigItem newGeneralConfigItem = new GeneralConfigItem(configItems.get(description));
			newGeneralConfigItem.value = entry.getIMEInfo().name;
			cachedChanges.put(description, newGeneralConfigItem);
		}
    }
	@Override
    protected void init() {
        this.confirmButton = new Button(this.width / 2 - 100, this.height -40, 80, 20, new TranslationTextComponent("imeautochange.gui.configscreen.button.confirm"), (button)->confirmChanges()) ;
        this.addButton(confirmButton);
        this.cancelButton = new Button(this.width / 2 + 20, this.height -40, 80, 20, new TranslationTextComponent("imeautochange.gui.configscreen.button.cancel"), (button)->cancel()) ;
        this.addButton(cancelButton);
        super.init();
        centerxPos = (this.width + imeInfoEntryListWidth) / 2;
        String textInputIMEName = ConfigManager.getTextInputIMEName();
        System.out.println("textInputIMEName: "+textInputIMEName);
        textInputIMENameStatic = new SelectableStaticTextField(font, centerxPos + 20, PADDING + 30, 100, 20, new StringTextComponent(textInputIMEName), 
				(staticTextField) -> {
					selectedStatic = staticTextField;
				}, ConfigManager.DESCRIPTION_TEXTINPUTIMENAME);
        textInputIMENameStatic.setText(textInputIMEName);
        children.add(textInputIMENameStatic);
        String gameControlInputIMEName = ConfigManager.getGameControlInputIMEName();
        System.out.println("gameControlInputIMEName: "+gameControlInputIMEName);
		gameControlInputIMENameStatic = new SelectableStaticTextField(font, centerxPos + 20, PADDING + 56, 100, 20, new StringTextComponent(gameControlInputIMEName), 
				(staticTextField) -> {
					selectedStatic = staticTextField;
				}, ConfigManager.DESCRIPTION_GAMECONTROLINPUTIMENAME);
		gameControlInputIMENameStatic.setText(gameControlInputIMEName);
		children.add(gameControlInputIMENameStatic);
	}
	
	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		renderDirtBackground(0);
		font.func_238422_b_(matrixStack, new TranslationTextComponent("imeautochange.config.general.textinputimename").func_241878_f(),
				centerxPos - 120, PADDING + 30, 0xFFFFFF);
		font.func_238422_b_(matrixStack, new TranslationTextComponent("imeautochange.config.general.textinputimename").func_241878_f(),
				centerxPos - 120, PADDING + 56, 0xFFFFFF);
		textInputIMENameStatic.render(matrixStack, mouseX, mouseY, partialTicks);
		gameControlInputIMENameStatic.render(matrixStack, mouseX, mouseY, partialTicks);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
	}
	
	private void cancel() {
		Minecraft.getInstance()
		.displayGuiScreen(new ConfigSelectScreen(new TranslationTextComponent("imeautochange.gui.screen.configselectscreen")));
	}

	private void confirmChanges() {
		System.out.println("change confirmed");
		ConfigManager.updateDefaultIMEConfigChanges(cachedChanges);
		Minecraft.getInstance()
		.displayGuiScreen(new ConfigSelectScreen(new TranslationTextComponent("imeautochange.gui.screen.configselectscreen")));
	}
}
