package com.imeautochange.config.widget;

import com.imeautochange.config.ClassConfigInfo;
import com.imeautochange.config.ConfigScreen;
import com.imeautochange.config.IMEInfo;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.list.ExtendedList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.LanguageMap;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.client.gui.widget.ModListWidget.ModEntry;

public class ListenerClassEntryListWidget extends ExtendedList<ListenerClassEntryListWidget.ListenerClassEntry> {
	
    private final ConfigScreen parent;
    private final int listWidth;
	public ListenerClassEntryListWidget(ConfigScreen parent, int listWidth, int top, int bottom)
    {
        super(parent.getMinecraft(), listWidth, parent.height, top, bottom, parent.getFontRenderer().FONT_HEIGHT * 2 + 8);
        this.x1 = parent.width;
        this.x0 = x1 - listWidth;
        this.parent = parent;
        this.listWidth = listWidth;
        this.refreshList();
    }

    @Override
    protected int getScrollbarPosition()
    {
        return this.listWidth;
    }

    @Override
    public int getRowWidth()
    {
        return this.listWidth;
    }

    public void refreshList() {
        this.clearEntries();
        parent.refreshListenerClassList(this::addEntry, classConfigInfo->new ListenerClassEntry(classConfigInfo));
    }

    @Override
    protected void renderBackground(MatrixStack mStack)
    {
        this.parent.renderBackground(mStack);
    }
    
	public class ListenerClassEntry extends ExtendedList.AbstractListEntry<ListenerClassEntry>{
    	

        private final ClassConfigInfo classConfigInfo;
        
        ListenerClassEntry(ClassConfigInfo classConfigInfo){
        	this.classConfigInfo = classConfigInfo;
        }
        
        public ClassConfigInfo getClassConfigInfo() {
        	return classConfigInfo;
        }
        
    	@Override
    	public void render(MatrixStack mStack, int entryIdx, int top, int left, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean p_194999_5_, float partialTicks) {
            FontRenderer font = parent.getFontRenderer();
            font.func_238422_b_(mStack, LanguageMap.getInstance().func_241870_a(ITextProperties.func_240655_a_(font.func_238417_a_(classConfigInfo.displayName, listWidth))), left + 3, top + 2, 0xFFFFFF);
    	}
    	@Override
        public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_)
        {
    		ListenerClassEntryListWidget.this.setSelected(this);
            parent.setSelectedListenerClassEntry(this);
            return false;
        }

    }
}
