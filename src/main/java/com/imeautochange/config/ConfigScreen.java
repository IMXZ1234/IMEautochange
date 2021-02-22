package com.imeautochange.config;

import java.util.function.Consumer;
import java.util.function.Function;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Lists;
import com.imeautochange.ModFunctionManager;
import com.imeautochange.config.widget.IMEInfoEntryListWidget;
import com.imeautochange.config.widget.IMEInfoEntryListWidget.IMEInfoEntry;
import com.imeautochange.config.widget.ListenerClassEntryListWidget;
import com.imeautochange.config.widget.ListenerClassEntryListWidget.ListenerClassEntry;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.GameSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.SettingsScreen;
import net.minecraft.client.gui.widget.OptionSlider;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.list.ExtendedList;
import net.minecraft.client.settings.SliderPercentageOption;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.MavenVersionStringHelper;
import net.minecraftforge.fml.client.gui.widget.ModListWidget;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;

@OnlyIn(Dist.CLIENT)
public class ConfigScreen extends Screen {
	private static final int PADDING = 6;

	TextFieldWidget selectedIMEName;
    Button confirmButton;
    OptionSlider optionSlider;
    String content = "Hello";
    SliderPercentageOption sliderPercentageOption;
    Widget sliderBar;
    ListenerClassEntryListWidget listenerClassEntryListWidget;
    IMEInfoEntryListWidget imeInfoEntryListWidget;
    
    protected final List<Widget> extendedChildren = Lists.newLinkedList();
    
    HashMap<Class<?>, ClassConfigInfo> cachedChanges;
    
    HashMap<Class<?>, ClassConfigInfo> listenerClassInfo;
    ArrayList<IMEInfo> IMEs;
    
    ListenerClassEntry selectedListenerClassEntry;
    IMEInfoEntry selectedIMEInfoEntry;
	private int listWidth;
    
    public ConfigScreen(ITextComponent titleIn) {
    	super(titleIn);
    	IMEs = ModFunctionManager.getIMEInfoList();
    	listenerClassInfo = ModFunctionManager.getListenerClassConfigInfo();
    	cachedChanges = new HashMap<Class<?>, ClassConfigInfo>();
    }
    
    public FontRenderer getFontRenderer()
    {
        return font;
    }
    
    public void setSelectedIMEInfoEntry(IMEInfoEntry entry)
    {
        this.selectedIMEInfoEntry = entry == this.selectedIMEInfoEntry ? null : entry;
    }
    
    public void setSelectedListenerClassEntry(ListenerClassEntry entry)
    {
    	this.selectedListenerClassEntry = entry == this.selectedListenerClassEntry ? null : entry;
    	updateExtendedChildren();
    }
    
    private void updateExtendedChildren() {
//    	Iterator<Entry<Class<?>, ClassConfigInfo>> iter=listenerClassInfo.entrySet().iterator();
//    	while(iter.hasNext())
//    	{
//    		System.out.println(ite.next());
//    	}
    	children.removeAll(extendedChildren);
    	extendedChildren.clear();
    	ClassConfigInfo selectedClassConfigInfo = selectedListenerClassEntry.getClassConfigInfo();
    	HashMap<String, ConfigItem> configItems = selectedClassConfigInfo.configItems;
    	int configItemsNum = configItems.size();
    	int i = 0;
    	int y;
    	for(Entry<String, ConfigItem> entry : configItems.entrySet()) {
    		y = 96 + 25 * i;
    		extendedChildren.add(new Button(this.width / 2 - 40, y, 80, 20, 
    		entry.getValue().enabled ? new TranslationTextComponent("imeautochange.gui.configscreen.button.configelementenabled")
    								 : new TranslationTextComponent("imeautochange.gui.configscreen.button.configelementdisabled"), 
    		(button)->{
            	System.out.println("button pressed "+"button.x "+button.x+"button.y "+button.y);
            }));
    		extendedChildren.add(new TextFieldWidget(font, this.width / 2 + 45, y, 80, 20, new StringTextComponent(entry.getValue().imeName)));
    		i++;
    	}
    	Iterator<Widget> iter=extendedChildren.iterator();
    	while(iter.hasNext())
        {
            System.out.println(iter.next());
        }
	}

	@Override
    protected void init() {
//        this.minecraft.keyboardListener.enableRepeatEvents(true);
//        this.textFieldWidget = new TextFieldWidget(this.font, this.width / 2 - 100, 66, 200, 20, new StringTextComponent("Context"));
//        this.children.add(this.textFieldWidget);
//		cachedChanges.clear();
		for (IMEInfo imeInfo:IMEs)
        {
            listWidth = Math.max(listWidth,getFontRenderer().getStringWidth(imeInfo.name) + 10);
            listWidth = Math.max(listWidth,getFontRenderer().getStringWidth(imeInfo.id) + 5);
        }
        listWidth = Math.max(Math.min(listWidth, width/3), 100);
        int y0 = PADDING + 20;
        int y1 = this.height - y0;
        
        this.selectedIMEName = new TextFieldWidget(getFontRenderer(), PADDING + 1, y1, listWidth - 2, 14, new TranslationTextComponent("imeautochange.gui.configscreen.textfield.selectedimename"));
        selectedIMEName.setEnabled(false);
        this.children.add(selectedIMEName);
        this.confirmButton = new Button(this.width / 2 - 40, y1 - 20, 80, 20, new TranslationTextComponent("imeautochange.gui.configscreen.button.confirm"), (button)->confirmChanges()) ;
        this.addButton(confirmButton);
        
        this.imeInfoEntryListWidget = new IMEInfoEntryListWidget(this, listWidth, y0, y1);
        this.children.add(imeInfoEntryListWidget);
        this.listenerClassEntryListWidget = new ListenerClassEntryListWidget(this, listWidth, y0, y1);
        this.children.add(listenerClassEntryListWidget);
//        this.sliderPercentageOption = new SliderPercentageOption("neutrino.sliderbar", 5, 100, 5, (setting) -> {
//            return Double.valueOf(0);
//        }, (setting, value) -> {
//        }, (gameSettings, sliderPercentageOption1) -> new StringTextComponent("test"));
//        this.sliderBar = this.sliderPercentageOption.createWidget(Minecraft.getInstance().gameSettings, this.width / 2 - 100, 120, 200);
//        this.children.add(this.sliderBar);

        super.init();
    }


	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(matrixStack);
//		this.minecraft.getTextureManager().bindTexture(OBSIDIAN_FIRST_GUI_TEXTURE);
		int textureWidth = 208;
		int textureHeight = 156;
		this.blit(matrixStack, this.width / 2 - 150, 10, 0, 0, 300, 200, textureWidth, textureHeight);
//		drawCenteredString(matrixStack, this.font, content, this.width / 2 - 10, 30, 0xeb0505);
//		this.textFieldWidget.render(matrixStack, mouseX, mouseY, partialTicks);
//		this.button.render(matrixStack, mouseX, mouseY, partialTicks);
//		this.sliderBar.render(matrixStack, mouseX, mouseY, partialTicks);
		this.imeInfoEntryListWidget.render(matrixStack, mouseX, mouseY, partialTicks);
		this.listenerClassEntryListWidget.render(matrixStack, mouseX, mouseY, partialTicks);
		for(Widget child:extendedChildren) {
    		child.render(matrixStack, mouseX, mouseY, partialTicks);
    	}
		super.render(matrixStack, mouseX, mouseY, partialTicks);
	}

	public <T extends ExtendedList.AbstractListEntry<T>> void refreshIMEInfoList(Consumer<T> imeInfoListViewConsumer, Function<IMEInfo, T> newEntry){
        IMEs.forEach(imeInfo->imeInfoListViewConsumer.accept(newEntry.apply(imeInfo)));
    }
	
	public <T extends ExtendedList.AbstractListEntry<T>> void refreshListenerClassList(Consumer<T> listenerClassInfoViewConsumer, Function<ClassConfigInfo, T> newEntry){
		for(Entry<Class<?>, ClassConfigInfo> entry : listenerClassInfo.entrySet()){
			listenerClassInfoViewConsumer.accept(newEntry.apply(entry.getValue()));
		}
	}

	private Object confirmChanges() {
		ModFunctionManager.updateHandlersListenerTable(cachedChanges);
		closeScreen();
		return null;
	}

}
