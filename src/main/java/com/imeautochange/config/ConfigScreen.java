package com.imeautochange.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import com.google.common.collect.Lists;
import com.imeautochange.ModFunctionManager;
import com.imeautochange.config.widget.IMEInfoEntryListWidget;
import com.imeautochange.config.widget.IMEInfoEntryListWidget.IMEInfoEntry;
import com.imeautochange.config.widget.IdentifiableButton;
import com.imeautochange.config.widget.ListenerClassEntryListWidget;
import com.imeautochange.config.widget.ListenerClassEntryListWidget.ListenerClassEntry;
import com.imeautochange.config.widget.SelectableStaticTextField;
import com.imeautochange.nativefunction.NativeFunctionManager;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
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
public class ConfigScreen extends Screen {
	public static final int PADDING = 6;
	
	Minecraft mc;

    Button confirmButton;
    Button cancelButton;
    Button defaultButton;
    Button refreshIMEListButton;
    
    ListenerClassEntryListWidget listenerClassEntryListWidget;
    IMEInfoEntryListWidget imeInfoEntryListWidget;
    ClassConfigInfoPanel classConfigInfoPanel;
    
    protected final List<Widget> extendedChildren = Lists.newLinkedList();
    protected final List<String> configItemNameList = Lists.newLinkedList();
    
    HashMap<Class<?>, ClassConfigInfo> cachedChanges;
    
    HashMap<Class<?>, ClassConfigInfo> listenerClassInfo;
    ArrayList<IMEInfo> imeList;
    
    ListenerClassEntry selectedListenerClassEntry;
    IMEInfoEntry selectedIMEInfoEntry;
    private SelectableStaticTextField selectedStatic;
    
	private int imeInfoEntryListWidth;
	private int listenerClassEntryListWidth;

    public ConfigScreen(ITextComponent titleIn) {
    	super(titleIn);
    	imeList = ModFunctionManager.getIMEInfoList();
    	listenerClassInfo = ModFunctionManager.getListenerClassConfigInfo();
    	cachedChanges = new HashMap<Class<?>, ClassConfigInfo>();
    }
    
    public HashMap<Class<?>, ClassConfigInfo> getListenerClassInfo() {
    	return listenerClassInfo;
    }
    
    public ArrayList<IMEInfo> getIMEList() {
    	return imeList;
    }
    
    public FontRenderer getFontRenderer()
    {
        return font;
    }
    
    public void setSelectedIMEInfoEntry(IMEInfoEntry entry)
    {
//    	System.out.println("IMEInfoEntry selected");
        this.selectedIMEInfoEntry = entry;
        String imeName = entry.getIMEInfo().name;
		if (selectedStatic != null) {
			this.selectedStatic.setText(imeName);
			addConfigChangeToCache(selectedListenerClassEntry.getClazz(), selectedStatic.getId(), imeName);
//			System.out.println("addConfigChangeToCache imeName");
//			System.out.println("new config: "+getClassConfigInfoForDisplay().configItems.get(selectedStatic.getId()).imeName);
//			System.out.println("is restored?: "+isConfigChangeRestored(selectedListenerClassEntry.getClazz(), selectedStatic.getId(), imeName));
		}
    }
    
    public void setSelectedListenerClassEntry(ListenerClassEntry entry)
    {
//    	System.out.println("ListenerClassEntry selected");
    	this.selectedListenerClassEntry = entry;
    	classConfigInfoPanel.refreshDisplayedClassConfigInfo(selectedListenerClassEntry.getClazz(), getClassConfigInfoForDisplay());
    }
	
	private void addConfigChangeToCache(Class<?> clazz, String configItemName, boolean enabled) {
		ClassConfigInfo classConfigInfoCachedChanges = cachedChanges.get(clazz);
		if(classConfigInfoCachedChanges == null) {
			classConfigInfoCachedChanges = new ClassConfigInfo(listenerClassInfo.get(clazz));
			cachedChanges.put(clazz, classConfigInfoCachedChanges);
		}
		ConfigItem configItemCachedChanges = classConfigInfoCachedChanges.configItems.get(configItemName);
		configItemCachedChanges.enabled = enabled;
	}
	
	private void addConfigChangeToCache(Class<?> clazz, String configItemName, String imeName) {
		ClassConfigInfo classConfigInfoCachedChanges = cachedChanges.get(clazz);
		if(classConfigInfoCachedChanges == null) {
			classConfigInfoCachedChanges = new ClassConfigInfo(listenerClassInfo.get(clazz));
			cachedChanges.put(clazz, classConfigInfoCachedChanges);
		}
		ConfigItem configItemCachedChanges = classConfigInfoCachedChanges.configItems.get(configItemName);
		configItemCachedChanges.imeName = imeName;
	}
	
	private ClassConfigInfo getClassConfigInfoForDisplay() {
		// Check if displayed ClassConfigInfo is changed already and saved in cachedChanges.
    	ClassConfigInfo selectedClassConfigInfo = cachedChanges.get(selectedListenerClassEntry.getClazz());
    	if(selectedClassConfigInfo == null) {
    		selectedClassConfigInfo = listenerClassInfo.get(selectedListenerClassEntry.getClazz());
    	}
    	else{
    	}
    	return selectedClassConfigInfo;
	}
	
	private boolean isConfigChangeRestored(Class<?> clazz, String configItemName, boolean enabled) {
		ConfigItem originalConfigItem = listenerClassInfo.get(clazz).configItems.get(configItemName);
		return (enabled == originalConfigItem.enabled);
	}
	
	private boolean isConfigChangeRestored(Class<?> clazz, String configItemName, String imeName) {
		ConfigItem originalConfigItem = listenerClassInfo.get(clazz).configItems.get(configItemName);
		return (imeName.equals(originalConfigItem.imeName));
	}
	
	
	
	@Override
    protected void init() {
		this.mc = Minecraft.getInstance();
		for (IMEInfo imeInfo:imeList)
        {
            imeInfoEntryListWidth = Math.max(imeInfoEntryListWidth,getFontRenderer().getStringWidth(imeInfo.name) + 10);
        }
		for (Entry<Class<?>, ClassConfigInfo> entry:listenerClassInfo.entrySet())
		{
			listenerClassEntryListWidth = Math.max(listenerClassEntryListWidth,getFontRenderer().getStringPropertyWidth(entry.getValue().displayName) + 10);
		}
        imeInfoEntryListWidth = Math.max(Math.min(imeInfoEntryListWidth, width/3), 100);
        listenerClassEntryListWidth = Math.max(Math.min(listenerClassEntryListWidth, width/3), 100);
        int y0 = PADDING + 10;
        int y1 = this.height - PADDING - 40;
        
        this.confirmButton = new Button(this.width / 2 - 150, y1 + PADDING, 80, 20, new TranslationTextComponent("imeautochange.gui.configscreen.button.confirm"), (button)->confirmChanges()) ;
        this.addButton(confirmButton);
        this.defaultButton = new Button(this.width / 2 - 40, y1 + PADDING, 80, 20, new TranslationTextComponent("imeautochange.gui.configscreen.button.default"), (button)->resetToDefault()) ;
        this.addButton(defaultButton);
        this.cancelButton = new Button(this.width / 2 + 70, y1 + PADDING, 80, 20, new TranslationTextComponent("imeautochange.gui.configscreen.button.cancel"), (button)->cancel()) ;
        this.addButton(cancelButton);
        this.refreshIMEListButton = new Button(PADDING + imeInfoEntryListWidth / 2 - 40, y0, 80, 20, new TranslationTextComponent("imeautochange.gui.configscreen.button.refreshimelist"), (button)->refreshIMEList()) ;
        this.addButton(refreshIMEListButton);
        
        this.listenerClassEntryListWidget = new ListenerClassEntryListWidget(this, listenerClassEntryListWidth, y0, y1);
        this.children.add(listenerClassEntryListWidget);
        this.listenerClassEntryListWidget.setLeftPos(this.width - listenerClassEntryListWidth - PADDING);
        this.imeInfoEntryListWidget = new IMEInfoEntryListWidget(this, imeInfoEntryListWidth, y0 + 20 + PADDING, y1);
        this.children.add(imeInfoEntryListWidget);
        this.imeInfoEntryListWidget.setLeftPos(PADDING);
        this.classConfigInfoPanel = new ClassConfigInfoPanel(mc, this.width - imeInfoEntryListWidth - listenerClassEntryListWidth - PADDING * 2, y1 - y0, y0, imeInfoEntryListWidth + PADDING);
        this.children.add(classConfigInfoPanel);
        super.init();
    }

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.renderDirtBackground(0);
		this.imeInfoEntryListWidget.render(matrixStack, mouseX, mouseY, partialTicks);
		this.listenerClassEntryListWidget.render(matrixStack, mouseX, mouseY, partialTicks);
		this.classConfigInfoPanel.render(matrixStack, mouseX, mouseY, partialTicks);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
	}
	
	@Override
	public void closeScreen() {
		cachedChanges.clear();
		this.minecraft.displayGuiScreen((Screen) null);
	}

	private void confirmChanges() {
		ConfigManager.updateConfigChanges(cachedChanges);
		closeScreen();
	}

	private void resetToDefault() {
		cachedChanges.clear();
		for(Entry<Class<?>, ClassConfigInfo> classConfigInfoEntry : listenerClassInfo.entrySet()) {
			ClassConfigInfo classConfigInfo = classConfigInfoEntry.getValue();
			cachedChanges.put(classConfigInfoEntry.getKey(), new ClassConfigInfo(classConfigInfo));
			for (Entry<String, ConfigItem> configItemsEntry : classConfigInfo.configItems.entrySet()) {
				ConfigItem configItem = configItemsEntry.getValue();
				configItem.enabled = configItem.defaultEnabled;
				configItem.imeName = configItem.defaultIMEName;
			}
		}
		if(selectedListenerClassEntry != null) {
			classConfigInfoPanel.refreshDisplayedClassConfigInfo(selectedListenerClassEntry.getClazz(), getClassConfigInfoForDisplay());
		}
	}

	private void cancel() {
		closeScreen();
	}

	private Object refreshIMEList() {
		NativeFunctionManager.reloadIMEInfoList();
		imeInfoEntryListWidget.refreshList();
		return null;
	}

	public class ClassConfigInfoPanel extends ScrollPanel{
		public final int PADDING =6;
		private int classDisplayNameCenterxPos;
		private final int configItemStartyPos = 3 * PADDING + font.FONT_HEIGHT * 2 + 10;
		private final int configItemyPosInterval = 3 * PADDING + font.FONT_HEIGHT + 20;
		
		private List<Widget> children = new LinkedList<Widget>();
		private Class<?> dislpayedClazz;
		private ClassConfigInfo displayedClassConfigInfo;
		
		public void refreshDisplayedClassConfigInfo(Class<?> dislpayedClazz, ClassConfigInfo displayedClassConfigInfo) {
			this.dislpayedClazz = dislpayedClazz;
			this.displayedClassConfigInfo = displayedClassConfigInfo;
			configItemNameList.clear();
			children.clear();
			HashMap<String, ConfigItem> configItems = displayedClassConfigInfo.configItems;
	    	int y = configItemStartyPos;
	    	for(Entry<String, ConfigItem> entry : configItems.entrySet()) {
	    		configItemNameList.add(entry.getKey());
	    		children.add(new IdentifiableButton(classDisplayNameCenterxPos - 80, y, 60, 20, 
	    		entry.getValue().enabled ? new TranslationTextComponent("imeautochange.gui.configscreen.button.configelementenabled")
	    								 : new TranslationTextComponent("imeautochange.gui.configscreen.button.configelementdisabled"), 
	    		(button)->{
//	            	System.out.println("button pressed "+((IdentifiableButton)button).getId());
	            	ConfigItem oldConfigItem = getClassConfigInfoForDisplay().configItems.get(((IdentifiableButton)button).getId());
//	            	System.out.println("old config: "+oldConfigItem.enabled);
	            	boolean newEnabledState = !oldConfigItem.enabled;
	            	button.setMessage(newEnabledState ? new TranslationTextComponent("imeautochange.gui.configscreen.button.configelementenabled")
	    								 : new TranslationTextComponent("imeautochange.gui.configscreen.button.configelementdisabled"));
	            	addConfigChangeToCache(selectedListenerClassEntry.getClazz(), ((IdentifiableButton)button).getId(), newEnabledState);
//	            	System.out.println("addConfigChangeToCache enabled");
//	    			System.out.println("new config: "+getClassConfigInfoForDisplay().configItems.get(((IdentifiableButton)button).getId()).enabled);
//	    			System.out.println("is restored?: "+isConfigChangeRestored(selectedListenerClassEntry.getClazz(), ((IdentifiableButton)button).getId(), newEnabledState));
	            }, entry.getKey()));
	    		SelectableStaticTextField selectableTextField = 
	    			new SelectableStaticTextField(font, classDisplayNameCenterxPos - 20 + PADDING, y, 100, 20, new StringTextComponent(entry.getValue().imeName), 
								(staticTextField) -> {
//									System.out.println("static pressed " + staticTextField.getText());
									selectedStatic = staticTextField;
//									System.out.println("set selectedStatic:"+ selectedStatic.getId());
								}, 
	    					entry.getKey());
	    		selectableTextField.setText(entry.getValue().imeName);
	    		children.add(selectableTextField);
	    		y += configItemyPosInterval;
	    	}
		}
		
		public ClassConfigInfoPanel(Minecraft client, int width, int height, int top, int left) {
			super(client, width, height, top, left);
			classDisplayNameCenterxPos = left + width / 2;
			if (selectedListenerClassEntry != null) {
				refreshDisplayedClassConfigInfo(selectedListenerClassEntry.getClazz(), getClassConfigInfoForDisplay());
			}
		}

		@Override
		protected int getContentHeight() {
			return children.size() * configItemyPosInterval / 2;
		}

		@Override
		protected void drawPanel(MatrixStack mStack, int entryRight, int relativeY, Tessellator tess, int mouseX,
				int mouseY) {
			// Title
			if (displayedClassConfigInfo != null) {
				ITextComponent displayName = displayedClassConfigInfo.displayName;
				font.func_238422_b_(mStack, displayName.func_241878_f(),
						classDisplayNameCenterxPos - font.getStringPropertyWidth(displayName), PADDING, 0xFFFFFF);
			}
			// Widgets
			for(Widget child:children) {
				child.render(mStack, mouseX, mouseY, 0);
			}
			// Config Item Names
			int y = configItemStartyPos - font.FONT_HEIGHT - PADDING;
	    	for (String configItemName:configItemNameList) {
	    		font.func_238422_b_(mStack, new TranslationTextComponent(configItemName).func_241878_f(), classDisplayNameCenterxPos - font.getStringWidth(configItemName) / 2, y, 0xFFFFFF);
	    		y += configItemyPosInterval;
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
		    public boolean mouseClicked(double mouseX, double mouseY, int button) {
		        return super.mouseClicked(mouseX, mouseY, button);
		    }

		
		public void addWidget(Widget widget) {
			children.add(widget);
		}

	}
}
