package com.imeautochange.compat.startup;

import java.util.HashMap;
import java.util.LinkedList;

import com.imeautochange.ModFunctionManager;
import com.imeautochange.api.DummyClass;
import com.imeautochange.compat.JEICompatIngredientListOverlayAdapter;
import com.imeautochange.compat.event.JEICompatPrefixKeyboardInputHandler;
import com.imeautochange.config.ClassConfigInfo;
import com.imeautochange.event.ModClientEventsHandler;
import com.imeautochange.event.ModClientEventsHandlerBase;
import com.imeautochange.startup.IMESupport;

import mezz.jei.api.runtime.IIngredientListOverlay;
import mezz.jei.gui.overlay.IngredientListOverlay;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.ModList;

public class BuiltInModSupport extends IMESupport{
	public static final String DESCRIPTION_JEIPREFIXINPUTAWARE = "Enable Switching on JEI Search Field Prefix Input";
	private ModList modList;
	@Override
	public void initMembers() {
		modList = ModList.get();
		LinkedList<String> _handlerDescriptions = new LinkedList<String>();
		LinkedList<ModClientEventsHandlerBase> _handlers = new LinkedList<ModClientEventsHandlerBase>();
		LinkedList<ClassConfigInfo> _classConfigInfos = new LinkedList<ClassConfigInfo>();
		if (modList.isLoaded("jei")) {
			_handlerDescriptions.add(DESCRIPTION_JEIPREFIXINPUTAWARE);
			_handlers.add(new JEICompatPrefixKeyboardInputHandler());
			ClassConfigInfo jeiIngredientListOverlay = new ClassConfigInfo(JEICompatIngredientListOverlayAdapter.INSTANCE,
					new TranslationTextComponent("imeautochange.config.jeiingredientlistoverlay"),
					new String[] { DESCRIPTION_MOUSEINPUTAWARE, DESCRIPTION_KEYBOARDINPUTINPUTAWARE, DESCRIPTION_JEIPREFIXINPUTAWARE },
					new boolean[] { true, true, true },
					new String[] { defaultIMEName, englishIMEName, englishIMEName });
			_classConfigInfos.add(jeiIngredientListOverlay);
		}
		handlerDescriptions = new String[_handlerDescriptions.size()];
		handlerDescriptions = _handlerDescriptions.toArray(handlerDescriptions);
		handlers = new ModClientEventsHandlerBase[_handlers.size()];
		handlers = _handlers.toArray(handlers);
		classConfigInfos = new ClassConfigInfo[_classConfigInfos.size()];
		classConfigInfos = _classConfigInfos.toArray(classConfigInfos);
	}

}
