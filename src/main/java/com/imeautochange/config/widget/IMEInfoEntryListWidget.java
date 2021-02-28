package com.imeautochange.config.widget;

import com.imeautochange.config.ConfigScreen;
import com.imeautochange.config.IMEInfo;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.list.ExtendedList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.LanguageMap;
import net.minecraft.util.text.StringTextComponent;

public class IMEInfoEntryListWidget extends ExtendedList<IMEInfoEntryListWidget.IMEInfoEntry> {
	
    private final ConfigScreen parent;
    private final int listWidth;
	public IMEInfoEntryListWidget(ConfigScreen parent, int listWidth, int top, int bottom)
    {
        super(parent.getMinecraft(), listWidth, parent.height, top, bottom, parent.getFontRenderer().FONT_HEIGHT * 2 + 8);
        this.parent = parent;
        this.listWidth = listWidth;
        this.refreshList();
    }

    @Override
    public int getScrollbarPosition() {
        return this.x1;
     }

    @Override
    public int getRowWidth()
    {
        return this.listWidth;
    }
    
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
    	System.out.println("IMEInfoEntryListWidget mouse clicked");
    	boolean result = super.mouseClicked(mouseX, mouseY, button);
    	System.out.println("result: "+result);
    	return result;
    }

    public void refreshList() {
        this.clearEntries();
        for(IMEInfo imeInfo : parent.getIMEList()) {
        	System.out.println(imeInfo);
        	addEntry(new IMEInfoEntry(imeInfo));
        }
    }

    @Override
    protected void renderBackground(MatrixStack mStack)
    {
        this.parent.renderBackground(mStack);
    }
    
	public class IMEInfoEntry extends ExtendedList.AbstractListEntry<IMEInfoEntry>{
    	

        private final IMEInfo imeInfo;
        
        IMEInfoEntry(IMEInfo imeInfo){
        	this.imeInfo = imeInfo;
        }
        
        public IMEInfo getIMEInfo() {
			return imeInfo;
		}
        
    	@Override
    	public void render(MatrixStack mStack, int entryIdx, int top, int left, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean p_194999_5_, float partialTicks) {
    		ITextComponent name = new StringTextComponent(imeInfo.name);
            ITextComponent id = new StringTextComponent(imeInfo.id);
            FontRenderer font = parent.getFontRenderer();
            font.func_238422_b_(mStack, LanguageMap.getInstance().func_241870_a(ITextProperties.func_240655_a_(font.func_238417_a_(name, listWidth))), left + 3, top + 2, 0xFFFFFF);
            font.func_238422_b_(mStack, LanguageMap.getInstance().func_241870_a(ITextProperties.func_240655_a_(font.func_238417_a_(id, listWidth))), left + 3, top + 2 + font.FONT_HEIGHT, 0xCCCCCC);
    	}
    	@Override
        public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_)
        {
    		IMEInfoEntryListWidget.this.setSelected(this);
            parent.setSelectedIMEInfoEntry(this);
            return false;
        }

    }
}
