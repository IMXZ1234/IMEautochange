package com.imeautochange.gui.widget;

import java.util.Map.Entry;

import com.imeautochange.config.ClassConfigInfo;
import com.imeautochange.gui.FunctionConfigScreen;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.list.ExtendedList;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.LanguageMap;

public class ListenerClassEntryListWidget extends ExtendedList<ListenerClassEntryListWidget.ListenerClassEntry> {
	
    private final FunctionConfigScreen parent;
    private final int listWidth;
	public ListenerClassEntryListWidget(FunctionConfigScreen parent, int listWidth, int top, int bottom)
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
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
    	boolean result = super.mouseClicked(mouseX, mouseY, button);
    	return result;
    }

    @Override
    public int getRowWidth()
    {
        return this.listWidth;
    }

    public void refreshList() {
        this.clearEntries();
        for(Entry<String, ClassConfigInfo> entry : parent.getListenerClassInfo().entrySet()) {
        	addEntry(new ListenerClassEntry(entry.getValue()));
        }
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
