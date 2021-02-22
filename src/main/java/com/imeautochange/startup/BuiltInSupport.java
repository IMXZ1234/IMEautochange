package com.imeautochange.startup;

import com.imeautochange.config.ClassConfigInfo;
import com.imeautochange.event.GuiOpenEventsHandler;
import com.imeautochange.event.KeyboardEventsHandler;
import com.imeautochange.event.ModClientEventsHandler;
import com.imeautochange.event.MouseEventsHandler;

import net.minecraft.client.gui.IngameGui;
import net.minecraft.client.gui.screen.EditBookScreen;
import net.minecraft.client.gui.screen.inventory.AnvilScreen;
import net.minecraft.client.gui.screen.inventory.CreativeScreen;
import net.minecraft.util.text.TranslationTextComponent;

public class BuiltInSupport extends IMESupport{
	
	@Override
	public void initMembers() {
		handlerDescriptions = new String[] {
				DESCRIPTION_MOUSEINPUTAWARE, 
				DESCRIPTION_KEYBOARDINPUTINPUTAWARE, 
				DESCRIPTION_GUIOPENAWARE
		};
		handlers = new ModClientEventsHandler[] {
				new MouseEventsHandler(),
				new KeyboardEventsHandler(),
				new GuiOpenEventsHandler()
		};
		ClassConfigInfo ingameGui = new ClassConfigInfo(IngameGui.class, new TranslationTextComponent("imeautochange.config.ingamegui"),
				new String[] { DESCRIPTION_GUIOPENAWARE }, new boolean[] { true }, new String[] { englishIMEName });
		ClassConfigInfo editBookScreen = new ClassConfigInfo(EditBookScreen.class, new TranslationTextComponent("imeautochange.config.editbookscreen"),
				new String[] { DESCRIPTION_GUIOPENAWARE }, new boolean[] { true }, new String[] { defaultIMEName });
		ClassConfigInfo anvilScreen = new ClassConfigInfo(AnvilScreen.class, new TranslationTextComponent("imeautochange.config.anvilscreen"),
				new String[] { DESCRIPTION_GUIOPENAWARE, DESCRIPTION_MOUSEINPUTAWARE }, new boolean[] { true, true }, new String[] { defaultIMEName, defaultIMEName });
		ClassConfigInfo creativeScreen = new ClassConfigInfo(CreativeScreen.class, new TranslationTextComponent("imeautochange.config.creativescreen"),
				new String[] { DESCRIPTION_MOUSEINPUTAWARE }, new boolean[] { true }, new String[] { defaultIMEName });
		classConfigInfos = new ClassConfigInfo[] {
				ingameGui, editBookScreen, anvilScreen, creativeScreen
		};
	}

}
