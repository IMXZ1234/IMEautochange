package com.imeautochange.gui;

import java.util.ArrayList;

import com.imeautochange.ModFunctionManager;
import com.imeautochange.config.IMEInfo;
import com.imeautochange.gui.widget.IMEInfoEntryListWidget;
import com.imeautochange.gui.widget.IMEInfoEntryListWidget.IMEInfoEntry;
import com.imeautochange.nativefunction.NativeFunctionManager;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class IMEInfoEntryListScreen extends Screen {
	public static final int PADDING = 6;
	
	protected ArrayList<IMEInfo> imeList;
	protected IMEInfoEntry selectedIMEInfoEntry;
	protected IMEInfoEntryListWidget imeInfoEntryListWidget;
    protected Button refreshIMEListButton;
	protected int imeInfoEntryListWidth;

	protected IMEInfoEntryListScreen(ITextComponent titleIn) {
		super(titleIn);
    	imeList = ModFunctionManager.getIMEInfoList();
	}
	public ArrayList<IMEInfo> getIMEList() {
    	return imeList;
    }
    public void setSelectedIMEInfoEntry(IMEInfoEntry entry)
    {
        this.selectedIMEInfoEntry = entry;
    }
	protected Object refreshIMEList() {
		NativeFunctionManager.reloadIMEInfoList();
		imeInfoEntryListWidget.refreshList();
		return null;
	}
	@Override
    protected void init() {
		for (IMEInfo imeInfo:imeList)
        {
            imeInfoEntryListWidth = Math.max(imeInfoEntryListWidth,font.getStringWidth(imeInfo.name) + 10);
        }
        imeInfoEntryListWidth = Math.max(Math.min(imeInfoEntryListWidth, width/3), 100);
        this.refreshIMEListButton = new Button(PADDING + imeInfoEntryListWidth / 2 - 40, PADDING + 10, 80, 20, new TranslationTextComponent("imeautochange.gui.configscreen.button.refreshimelist"), (button)->refreshIMEList()) ;
        this.addButton(refreshIMEListButton);
        
        this.imeInfoEntryListWidget = new IMEInfoEntryListWidget(this, imeInfoEntryListWidth, 2 * PADDING + 30, this.height - PADDING - 40);
        this.children.add(imeInfoEntryListWidget);
        this.imeInfoEntryListWidget.setLeftPos(PADDING);
        super.init();
    }

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.imeInfoEntryListWidget.render(matrixStack, mouseX, mouseY, partialTicks);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
	}
	
	public FontRenderer getFontRenderer() {
		return font;
	}
}
