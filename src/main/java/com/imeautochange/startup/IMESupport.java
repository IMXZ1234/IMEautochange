package com.imeautochange.startup;

import com.imeautochange.ModFunctionManager;
import com.imeautochange.config.ClassConfigInfo;
import com.imeautochange.config.ConfigManager;
import com.imeautochange.event.EventsHandlerManager;
import com.imeautochange.event.ModClientEventsHandlerBase;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public abstract class IMESupport {
	// Handlers
	public static final String DESCRIPTION_MOUSEINPUTAWARE = "Switching on Mouse Click";
	public static final String DESCRIPTION_KEYBOARDINPUTAWARE = "Switching on Key Pressed";
	public static final String DESCRIPTION_GUIOPENAWARE = "Switching on GUI Open";
	public static final String DESCRIPTION_JEIPREFIXINPUTAWARE = "Switching on JEI Search Field Prefix Input";
	public static final String DESCRIPTION_OPENCHATKEYBINGINGINPUTAWARE = "Switching on Open Chat";
	public static final ITextComponent DISPLAYNAME_MOUSEINPUTAWARE = new TranslationTextComponent("imeautochange.config.mouseinputaware");
	public static final ITextComponent DISPLAYNAME_KEYBOARDINPUTAWARE = new TranslationTextComponent("imeautochange.config.keyboardinputaware");
	public static final ITextComponent DISPLAYNAME_GUIOPENAWARE = new TranslationTextComponent("imeautochange.config.guiopenaware");
	public static final ITextComponent DISPLAYNAME_JEIPREFIXINPUTAWARE = new TranslationTextComponent("imeautochange.config.jeiprefixinputaware");
	public static final ITextComponent DISPLAYNAME_OPENCHATKEYBINGINGINPUTAWARE = new TranslationTextComponent("imeautochange.config.openchatkeybindinginputaware");
	// Classes
	public static final String DESCRIPTION_INGAMEGUI = "In Game";
	public static final String DESCRIPTION_EDITBOOKSCREEN = "Edit Book";
	public static final String DESCRIPTION_EDITSIGNSCREEN = "Edit Sign";
	public static final String DESCRIPTION_ANVILSCREEN = "Anvil";
	public static final String DESCRIPTION_CREATIVESCREEN = "Creative";
	public static final String DESCRIPTION_JEIINGREDIENTLISTOVERLAY = "JEI";
	public static final String DESCRIPTION_CHATSCREEN = "Chat Screen";
	public static final ITextComponent DISPLAYNAME_INGAMEGUI = new TranslationTextComponent("imeautochange.config.ingamegui");
	public static final ITextComponent DISPLAYNAME_EDITBOOKSCREEN = new TranslationTextComponent("imeautochange.config.editbookscreen");
	public static final ITextComponent DISPLAYNAME_EDITSIGNSCREEN = new TranslationTextComponent("imeautochange.config.editsignscreen");
	public static final ITextComponent DISPLAYNAME_ANVILSCREEN = new TranslationTextComponent("imeautochange.config.anvilscreen");
	public static final ITextComponent DISPLAYNAME_CREATIVESCREEN = new TranslationTextComponent("imeautochange.config.creativescreen");
	public static final ITextComponent DISPLAYNAME_JEIINGREDIENTLISTOVERLAY = new TranslationTextComponent("imeautochange.config.jeiingredientlistoverlay");
	public static final ITextComponent DISPLAYNAME_CHATSCREEN = new TranslationTextComponent("imeautochange.config.chatscreen");
	
	public static String defaultIMEName = ModFunctionManager.getDefaultIMEName();
	public static String englishIMEName = ModFunctionManager.getEnglishIMEName();

	public String[] handlerDescriptions;
	public ModClientEventsHandlerBase[] handlers;
	public ClassConfigInfo[] classConfigInfos;
	
	public final void initIMESupport() {
		initMembers();
		int handlerNum = handlers.length;
		if (handlerNum > handlerDescriptions.length) {
			handlerNum = handlerDescriptions.length;
		}
		for (int i = 0; i < handlerNum; i++) {
			System.out.println(handlerDescriptions[i]);
			EventsHandlerManager.registerHandler(handlerDescriptions[i], handlers[i]);
		}
		for (int i = 0; i < classConfigInfos.length; i++) {
			System.out.println("initIMESupport"+classConfigInfos);
			ConfigManager.registerClassConfigInfo(classConfigInfos[i]);
		}
	}
	
	public abstract void initMembers();
}
