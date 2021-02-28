package com.imeautochange.config.widget;

import java.util.Map;
import java.util.Map.Entry;

import com.imeautochange.config.ClassConfigInfo;
import com.imeautochange.config.ConfigScreen;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.list.ExtendedList;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.LanguageMap;

public class ListenerClassEntryListWidget extends ExtendedList<ListenerClassEntryListWidget.ListenerClassEntry> {
	
    private final ConfigScreen parent;
    private final int listWidth;
	public ListenerClassEntryListWidget(ConfigScreen parent, int listWidth, int top, int bottom)
    {
        super(parent.getMinecraft(), listWidth, parent.height, top, bottom, parent.getFontRenderer().FONT_HEIGHT * 2 + 8);
        
        System.out.print("ListenerClassEntryListWidget init");
//        this.x1 = parent.width;
//        this.x0 = x1 - listWidth;
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
    	System.out.println("ListenerClassEntryListWidget mouse clicked");
    	boolean result = super.mouseClicked(mouseX, mouseY, button);
    	System.out.println("result: "+result);
    	return result;
    }

    @Override
    public int getRowWidth()
    {
        return this.listWidth;
    }

    public void refreshList() {
        this.clearEntries();
        for(Entry<Class<?>, ClassConfigInfo> entry : parent.getListenerClassInfo().entrySet()) {
        	System.out.println("ListenerClassEntry added"+entry.getKey().getName());
        	addEntry(new ListenerClassEntry(entry.getKey(), entry.getValue()));
        }
    }

    @Override
    protected void renderBackground(MatrixStack mStack)
    {
        this.parent.renderBackground(mStack);
    }
    
	public class ListenerClassEntry extends ExtendedList.AbstractListEntry<ListenerClassEntry>{
    	

        private final ClassConfigInfo classConfigInfo;
        private final Class<?> clazz;
        
        ListenerClassEntry(Class<?> clazz, ClassConfigInfo classConfigInfo){
        	this.clazz = clazz;
        	this.classConfigInfo = classConfigInfo;
        }
        
        public ClassConfigInfo getClassConfigInfo() {
        	return classConfigInfo;
        }
        
        public Class<?> getClazz() {
			return clazz;
		}
        
    	@Override
    	public void render(MatrixStack mStack, int entryIdx, int top, int left, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean p_194999_5_, float partialTicks) {
            FontRenderer font = parent.getFontRenderer();
            font.func_238422_b_(mStack, LanguageMap.getInstance().func_241870_a(ITextProperties.func_240655_a_(font.func_238417_a_(classConfigInfo.displayName, listWidth))), left + 3, top + 2, 0xFFFFFF);
    	}
    	@Override
        public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_)
        {
    		System.out.println("ListenerClassEntry clicked");
    		ListenerClassEntryListWidget.this.setSelected(this);
            parent.setSelectedListenerClassEntry(this);
            return false;
        }

    }
}
