package com.imeautochange.startup;

import java.util.List;

import com.imeautochange.config.ClassConfigInfo;
import com.imeautochange.event.GuiOpenEventsHandler;
import com.imeautochange.event.KeyboardEventsHandler;
import com.imeautochange.event.ModClientEventsHandler;
import com.imeautochange.event.MouseEventsHandler;

import net.minecraft.client.gui.IngameGui;
import net.minecraft.client.gui.screen.EditBookScreen;
import net.minecraft.client.gui.screen.EditSignScreen;
import net.minecraft.client.gui.screen.inventory.AnvilScreen;
import net.minecraft.client.gui.screen.inventory.CreativeScreen;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TranslationTextComponent;

public class BuiltInSupport extends IMESupport{
	
	@Override
	public void initMembers() {
		handlerDescriptions = new String[] {
				DESCRIPTION_MOUSEINPUTAWARE, 
				DESCRIPTION_KEYBOARDINPUTAWARE, 
				DESCRIPTION_GUIOPENAWARE
		};
		handlers = new ModClientEventsHandler[] {
				new MouseEventsHandler(),
				new KeyboardEventsHandler(),
				new GuiOpenEventsHandler()
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
		classConfigInfos = new ClassConfigInfo[] {
				ingameGui, editBookScreen, editSignScreen, anvilScreen, creativeScreen
		};
	}

}
