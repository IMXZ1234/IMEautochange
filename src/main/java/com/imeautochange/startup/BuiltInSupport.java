package com.imeautochange.startup;

import com.imeautochange.config.ClassConfigInfo;
import com.imeautochange.event.GuiOpenEventsHandler;
import com.imeautochange.event.KeyboardEventsHandler;
import com.imeautochange.event.ModClientEventsHandlerBase;
import com.imeautochange.event.MouseEventsHandler;
import com.imeautochange.event.OpenChatKeyBindingInputEventsHandler;

import net.minecraft.client.gui.IngameGui;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.EditBookScreen;
import net.minecraft.client.gui.screen.EditSignScreen;
import net.minecraft.client.gui.screen.inventory.AnvilScreen;
import net.minecraft.client.gui.screen.inventory.CreativeScreen;
import net.minecraft.util.text.ITextComponent;

public class BuiltInSupport extends IMESupport{
	
	@Override
	public void initMembers() {
		handlerDescriptions = new String[] {
				DESCRIPTION_MOUSEINPUTAWARE, 
				DESCRIPTION_KEYBOARDINPUTAWARE, 
				DESCRIPTION_GUIOPENAWARE,
				DESCRIPTION_OPENCHATKEYBINGINGINPUTAWARE
		};
		handlers = new ModClientEventsHandlerBase[] {
				new MouseEventsHandler(),
				new KeyboardEventsHandler(),
				new GuiOpenEventsHandler(),
				new OpenChatKeyBindingInputEventsHandler()
		};
		ClassConfigInfo ingameGui = new ClassConfigInfo(IngameGui.class, 
				DESCRIPTION_INGAMEGUI,
				DISPLAYNAME_INGAMEGUI,
				new String[] { DESCRIPTION_GUIOPENAWARE }, 
				new ITextComponent[] { DISPLAYNAME_GUIOPENAWARE }, 
				new boolean[] { true }, 
				new String[] { englishIMEName });
		ClassConfigInfo editBookScreen = new ClassConfigInfo(EditBookScreen.class, 
				DESCRIPTION_EDITBOOKSCREEN,
				DISPLAYNAME_EDITBOOKSCREEN,
				new String[] { DESCRIPTION_GUIOPENAWARE }, 
				new ITextComponent[] { DISPLAYNAME_GUIOPENAWARE }, 
				new boolean[] { true }, 
				new String[] { defaultIMEName });
		ClassConfigInfo editSignScreen = new ClassConfigInfo(EditSignScreen.class, 
				DESCRIPTION_EDITSIGNSCREEN,
				DISPLAYNAME_EDITSIGNSCREEN,
				new String[] { DESCRIPTION_GUIOPENAWARE }, 
				new ITextComponent[] { DISPLAYNAME_GUIOPENAWARE }, 
				new boolean[] { true }, 
				new String[] { defaultIMEName });
		ClassConfigInfo anvilScreen = new ClassConfigInfo(AnvilScreen.class, 
				DESCRIPTION_ANVILSCREEN,
				DISPLAYNAME_ANVILSCREEN,
				new String[] { DESCRIPTION_GUIOPENAWARE, DESCRIPTION_MOUSEINPUTAWARE, DESCRIPTION_KEYBOARDINPUTAWARE }, 
				new ITextComponent[] { DISPLAYNAME_GUIOPENAWARE, DISPLAYNAME_MOUSEINPUTAWARE, DISPLAYNAME_KEYBOARDINPUTAWARE }, 
				new boolean[] { true, true, true }, 
				new String[] { defaultIMEName, defaultIMEName, englishIMEName });
		ClassConfigInfo creativeScreen = new ClassConfigInfo(CreativeScreen.class, 
				DESCRIPTION_CREATIVESCREEN,
				DISPLAYNAME_CREATIVESCREEN,
				new String[] { DESCRIPTION_MOUSEINPUTAWARE, DESCRIPTION_KEYBOARDINPUTAWARE }, 
				new ITextComponent[] { DISPLAYNAME_MOUSEINPUTAWARE, DISPLAYNAME_KEYBOARDINPUTAWARE }, 
				new boolean[] { true, true }, 
				new String[] { defaultIMEName, englishIMEName });
		ClassConfigInfo chatScreen = new ClassConfigInfo(ChatScreen.class, 
				DESCRIPTION_CHATSCREEN,
				DISPLAYNAME_CHATSCREEN,
				new String[] { DESCRIPTION_OPENCHATKEYBINGINGINPUTAWARE }, 
				new ITextComponent[] { DISPLAYNAME_OPENCHATKEYBINGINGINPUTAWARE }, 
				new boolean[] { true }, 
				new String[] { defaultIMEName });
		classConfigInfos = new ClassConfigInfo[] {
				ingameGui, editBookScreen, editSignScreen, anvilScreen, creativeScreen, chatScreen
		};
	}

}
