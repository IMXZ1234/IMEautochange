package com.imeautochange.gui;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import com.google.common.collect.Lists;
import com.imeautochange.ModFunctionManager;
import com.imeautochange.config.ClassConfigInfo;
import com.imeautochange.config.ClassConfigItem;
import com.imeautochange.config.ConfigManager;
import com.imeautochange.gui.widget.IMEInfoEntryListWidget.IMEInfoEntry;
import com.imeautochange.gui.widget.IdentifiableButton;
import com.imeautochange.gui.widget.ListenerClassEntryListWidget;
import com.imeautochange.gui.widget.ListenerClassEntryListWidget.ListenerClassEntry;
import com.imeautochange.gui.widget.SelectableStaticTextField;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.ScrollPanel;

@OnlyIn(Dist.CLIENT)
public class FunctionConfigScreen extends IMEInfoEntryListScreen {
	public static final int PADDING = 6;
	
	Minecraft mc;

    Button confirmButton;
    Button cancelButton;
    Button defaultButton;
    
    ListenerClassEntryListWidget listenerClassEntryListWidget;
    ClassConfigInfoPanel classConfigInfoPanel;
    
    HashMap<String, ClassConfigInfo> cachedChanges;
    
    HashMap<String, ClassConfigInfo> listenerClassInfo;
    
    ListenerClassEntry selectedListenerClassEntry;
    private SelectableStaticTextField selectedStatic;
    
	private int listenerClassEntryListWidth;

    public FunctionConfigScreen(ITextComponent titleIn) {
    	super(titleIn);
    	listenerClassInfo = ModFunctionManager.getListenerClassConfigInfo();
    	cachedChanges = new HashMap<String, ClassConfigInfo>();
    }
    
    public HashMap<String, ClassConfigInfo> getListenerClassInfo() {
    	return listenerClassInfo;
    }
    
    @Override
    public void setSelectedIMEInfoEntry(IMEInfoEntry entry)
    {
    	super.setSelectedIMEInfoEntry(entry);
        String imeName = entry.getIMEInfo().name;
		if (selectedStatic != null) {
			this.selectedStatic.setText(imeName);
			addConfigChangeToCache(selectedListenerClassEntry.getClassConfigInfo().description, selectedStatic.getId(), imeName);
		}
    }
    
    public void setSelectedListenerClassEntry(ListenerClassEntry entry)
    {
    	this.selectedListenerClassEntry = entry;
    	classConfigInfoPanel.refreshDisplayedClassConfigInfo(getClassConfigInfoForDisplay());
    }
	
	private void addConfigChangeToCache(String description, String configItemName, boolean enabled) {
		ClassConfigInfo classConfigInfoCachedChanges = cachedChanges.get(description);
		if(classConfigInfoCachedChanges == null) {
			classConfigInfoCachedChanges = new ClassConfigInfo(listenerClassInfo.get(description));
			cachedChanges.put(description, classConfigInfoCachedChanges);
		}
		ClassConfigItem configItemCachedChanges = classConfigInfoCachedChanges.configItems.get(configItemName);
		configItemCachedChanges.enabled = enabled;
	}
	
	private void addConfigChangeToCache(String description, String configItemName, String imeName) {
		ClassConfigInfo classConfigInfoCachedChanges = cachedChanges.get(description);
		if(classConfigInfoCachedChanges == null) {
			classConfigInfoCachedChanges = new ClassConfigInfo(listenerClassInfo.get(description));
			cachedChanges.put(description, classConfigInfoCachedChanges);
		}
		ClassConfigItem configItemCachedChanges = classConfigInfoCachedChanges.configItems.get(configItemName);
		configItemCachedChanges.imeName = imeName;
	}
	
	private ClassConfigInfo getClassConfigInfoForDisplay() {
		if (selectedListenerClassEntry == null) {
			System.out.println("selectedListenerClassEntry is null!");
			return null;
		}
		// Check if displayed ClassConfigInfo is changed already and saved in cachedChanges.
    	ClassConfigInfo selectedClassConfigInfo = cachedChanges.get(selectedListenerClassEntry.getClassConfigInfo().description);
    	if(selectedClassConfigInfo == null) {
    		selectedClassConfigInfo = listenerClassInfo.get(selectedListenerClassEntry.getClassConfigInfo().description);
    	}
    	return selectedClassConfigInfo;
	}
	
	@SuppressWarnings("unused")
	private boolean isConfigChangeRestored(String description, String configItemName, boolean enabled) {
		ClassConfigItem originalConfigItem = listenerClassInfo.get(description).configItems.get(configItemName);
		return (enabled == originalConfigItem.enabled);
	}
	
	@SuppressWarnings("unused")
	private boolean isConfigChangeRestored(String description, String configItemName, String imeName) {
		ClassConfigItem originalConfigItem = listenerClassInfo.get(description).configItems.get(configItemName);
		return (imeName.equals(originalConfigItem.imeName));
	}
	
	
	
	@Override
    protected void init() {
		this.mc = Minecraft.getInstance();
		for (Entry<String, ClassConfigInfo> entry:listenerClassInfo.entrySet())
		{
			listenerClassEntryListWidth = Math.max(listenerClassEntryListWidth,getFontRenderer().getStringPropertyWidth(entry.getValue().displayName) + 10);
		}
        listenerClassEntryListWidth = Math.max(Math.min(listenerClassEntryListWidth, width/3), 100);
        int y0 = PADDING + 10;
        int y1 = this.height - PADDING - 40;
        
        this.confirmButton = new Button(this.width / 2 - 150, y1 + PADDING, 80, 20, new TranslationTextComponent("imeautochange.gui.configscreen.button.confirm"), (button)->confirmChanges()) ;
        this.addButton(confirmButton);
        this.defaultButton = new Button(this.width / 2 - 40, y1 + PADDING, 80, 20, new TranslationTextComponent("imeautochange.gui.configscreen.button.default"), (button)->resetToDefault()) ;
        this.addButton(defaultButton);
        this.cancelButton = new Button(this.width / 2 + 70, y1 + PADDING, 80, 20, new TranslationTextComponent("imeautochange.gui.configscreen.button.cancel"), (button)->cancel()) ;
        this.addButton(cancelButton);
        
        this.listenerClassEntryListWidget = new ListenerClassEntryListWidget(this, listenerClassEntryListWidth, y0, y1);
        this.children.add(listenerClassEntryListWidget);
        this.listenerClassEntryListWidget.setLeftPos(this.width - listenerClassEntryListWidth - PADDING);
        super.init();
        this.classConfigInfoPanel = new ClassConfigInfoPanel(mc, this.width - imeInfoEntryListWidth - listenerClassEntryListWidth - PADDING * 2, y1 - y0 - font.FONT_HEIGHT - PADDING, y0 + font.FONT_HEIGHT + PADDING, imeInfoEntryListWidth + PADDING);
        this.children.add(classConfigInfoPanel);
    }

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		renderDirtBackground(0);
		this.listenerClassEntryListWidget.render(matrixStack, mouseX, mouseY, partialTicks);
		this.classConfigInfoPanel.render(matrixStack, mouseX, mouseY, partialTicks);
		// Title
		ClassConfigInfo displayedClassConfigInfo = this.classConfigInfoPanel.getDisplayedClassConfigInfo();
		if (displayedClassConfigInfo != null) {
			font.func_238422_b_(matrixStack, displayedClassConfigInfo.displayName.func_241878_f(),
					this.classConfigInfoPanel.centerxPos - font.getStringPropertyWidth(displayedClassConfigInfo.displayName) / 2, PADDING, 0xFFFFFF);
		}
		super.render(matrixStack, mouseX, mouseY, partialTicks);
	}
	
	@Override
	public void closeScreen() {
		cachedChanges.clear();
		this.minecraft.displayGuiScreen(new ConfigSelectScreen(new TranslationTextComponent("imeautochange.gui.screen.configselectscreen")));
	}

	private void confirmChanges() {
		ConfigManager.updateFunctionConfigChanges(cachedChanges);
		closeScreen();
	}

	private void resetToDefault() {
		cachedChanges.clear();
		for(Entry<String, ClassConfigInfo> classConfigInfoEntry : listenerClassInfo.entrySet()) {
			ClassConfigInfo classConfigInfo = classConfigInfoEntry.getValue();
			ClassConfigInfo newDefaultClassConfigInfo = new ClassConfigInfo(classConfigInfo);
			cachedChanges.put(classConfigInfoEntry.getKey(), newDefaultClassConfigInfo);
			for (Entry<String, ClassConfigItem> configItemsEntry : newDefaultClassConfigInfo.configItems.entrySet()) {
				ClassConfigItem configItem = configItemsEntry.getValue();
				configItem.enabled = configItem.defaultEnabled;
				configItem.imeName = configItem.defaultIMEName;
			}
		}
		if(selectedListenerClassEntry != null) {
			classConfigInfoPanel.refreshDisplayedClassConfigInfo(getClassConfigInfoForDisplay());
		}
	}

	private void cancel() {
		closeScreen();
	}

	public class ClassConfigInfoPanel extends ScrollPanel{
		public final int PADDING =6;
		public final int LINE_SPACING = 3;
		public final ITextComponent TIP_LINE1 = new TranslationTextComponent("imeautochange.gui.configscreen.tip.line1");
		public final ITextComponent TIP_LINE2 = new TranslationTextComponent("imeautochange.gui.configscreen.tip.line2");
		public final ITextComponent TIP_LINE3 = new TranslationTextComponent("imeautochange.gui.configscreen.tip.line3");
		public final ITextComponent TIP_LINE4 = new TranslationTextComponent("imeautochange.gui.configscreen.tip.line4");
		public final ITextComponent TIP_LINE5 = new TranslationTextComponent("imeautochange.gui.configscreen.tip.line5");
		public final int centerxPos;
		public final int configItemStartyPos =  top + border + PADDING + 10;
		public final int configItemyPosInterval =  3 * PADDING + font.FONT_HEIGHT + 20;
		
		private List<Widget> children = new LinkedList<Widget>();
		private List<Integer> childreny = new LinkedList<Integer>();
	    protected final List<ITextComponent> configItemDisplayNameList = Lists.newLinkedList();
		private ClassConfigInfo displayedClassConfigInfo;
		public boolean isBarDisplayed;
		
		public ClassConfigInfo getDisplayedClassConfigInfo() {
			return displayedClassConfigInfo;
		}
		
		public void refreshDisplayedClassConfigInfo(ClassConfigInfo displayedClassConfigInfo) {
			this.displayedClassConfigInfo = displayedClassConfigInfo;
			configItemDisplayNameList.clear();
			children.clear();
			childreny.clear();
			HashMap<String, ClassConfigItem> configItems = displayedClassConfigInfo.configItems;
	    	int y = configItemStartyPos;
	    	for(Entry<String, ClassConfigItem> entry : configItems.entrySet()) {
	    		configItemDisplayNameList.add(entry.getValue().displayName);
	    		children.add(new IdentifiableButton(centerxPos - 80, y, 60, 20, 
	    		entry.getValue().enabled ? new TranslationTextComponent("imeautochange.gui.configscreen.button.configelementenabled")
	    								 : new TranslationTextComponent("imeautochange.gui.configscreen.button.configelementdisabled"), 
	    		(button)->{
	            	ClassConfigItem oldConfigItem = getClassConfigInfoForDisplay().configItems.get(((IdentifiableButton)button).getId());
	            	boolean newEnabledState = !oldConfigItem.enabled;
	            	button.setMessage(newEnabledState ? new TranslationTextComponent("imeautochange.gui.configscreen.button.configelementenabled")
	    								 : new TranslationTextComponent("imeautochange.gui.configscreen.button.configelementdisabled"));
	            	addConfigChangeToCache(selectedListenerClassEntry.getClassConfigInfo().description, ((IdentifiableButton)button).getId(), newEnabledState);
	            }, entry.getKey()));
	    		childreny.add(y);
	    		SelectableStaticTextField selectableTextField = 
	    			new SelectableStaticTextField(font, centerxPos - 20 + PADDING, y, 100, 20, new StringTextComponent(entry.getValue().imeName), 
								(staticTextField) -> {
									selectedStatic = staticTextField;
								}, 
	    					entry.getKey());
	    		selectableTextField.setText(entry.getValue().imeName);
	    		children.add(selectableTextField);
	    		childreny.add(y);
	    		y += configItemyPosInterval;
	    	}
	    	if((this.getContentHeight() + border - height)>0) {
				isBarDisplayed = true;
			}else {
				isBarDisplayed = false;
			}
		}
		
		public ClassConfigInfoPanel(Minecraft client, int width, int height, int top, int left) {
			super(client, width, height, top, left);
			centerxPos = left + width / 2;
			isBarDisplayed = false;
			if (selectedListenerClassEntry != null) {
				refreshDisplayedClassConfigInfo(getClassConfigInfoForDisplay());
			}
		}

		@Override
		protected int getContentHeight() {
			return children.size() * configItemyPosInterval / 2;
		}

		@Override
		protected void drawPanel(MatrixStack mStack, int entryRight, int relativeY, Tessellator tess, int mouseX,
				int mouseY) {
			if (displayedClassConfigInfo != null) {
				// Widgets
				for(Widget child:children) {
					child.render(mStack, mouseX, mouseY, 0);
				}
				// Config Item Names
				int y = configItemStartyPos - font.FONT_HEIGHT - PADDING;
				if(isBarDisplayed) {
					y = y - (int) this.scrollDistance;
				}
				for (ITextComponent configItemDisplayName:configItemDisplayNameList) {
					font.func_238422_b_(mStack, configItemDisplayName.func_241878_f(), centerxPos - font.getStringPropertyWidth(configItemDisplayName) / 2, y, 0xFFFFFF);
					y += configItemyPosInterval;
				}
			}else {
				font.func_238422_b_(mStack, TIP_LINE1.func_241878_f(),
						centerxPos - font.getStringPropertyWidth(TIP_LINE1) / 2, configItemStartyPos, 0xFFFFFF);
				font.func_238422_b_(mStack, TIP_LINE2.func_241878_f(),
						centerxPos - font.getStringPropertyWidth(TIP_LINE2) / 2, configItemStartyPos + (font.FONT_HEIGHT + LINE_SPACING), 0xFFFFFF);
				font.func_238422_b_(mStack, TIP_LINE3.func_241878_f(),
						centerxPos - font.getStringPropertyWidth(TIP_LINE3) / 2, configItemStartyPos + 2 * (font.FONT_HEIGHT + LINE_SPACING), 0xFFFFFF);
				font.func_238422_b_(mStack, TIP_LINE4.func_241878_f(),
						centerxPos - font.getStringPropertyWidth(TIP_LINE4) / 2, configItemStartyPos + 3 * (font.FONT_HEIGHT + LINE_SPACING), 0xFFFFFF);
				font.func_238422_b_(mStack, TIP_LINE5.func_241878_f(),
						centerxPos - font.getStringPropertyWidth(TIP_LINE5) / 2, configItemStartyPos + 4 * (font.FONT_HEIGHT + LINE_SPACING), 0xFFFFFF);
			}
		}
		@Override
	    protected boolean clickPanel(double mouseX, double mouseY, int button) { 
			double screenMouseX = mouseX + left;
			double screenMouseY = mouseY + this.top - (int)this.scrollDistance + border;
			for(Widget child:children) {
//				System.out.println("checking "+child.getMessage());
				if(child.mouseClicked(screenMouseX, screenMouseY, button)) {
					return true;
				}
			}
			return false;
		}
		
		@Override
		public boolean mouseScrolled(double mouseX, double mouseY, double scroll) {
			if (super.mouseScrolled(mouseX, mouseY, scroll)) {
				updateChildrenY();
				return true;
			}
			return false;
		}

		private void updateChildrenY() {
			if (isBarDisplayed) {
				for (int i = 0; i < children.size(); i++) {
					children.get(i).y = (int) (childreny.get(i) - this.scrollDistance);
				}
			}
		}

		@Override
	    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY)
	    {
	        if(super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)) {
	        	updateChildrenY();
	        	return true;
	        }
	        return false;
	    }
		
		public void addWidget(Widget widget) {
			children.add(widget);
		}

	}
}
