package com.imeautochange.compat.startup;

import java.util.LinkedList;

import com.imeautochange.compat.JEICompatIngredientListOverlayAdapter;
import com.imeautochange.compat.event.JEICompatPrefixKeyboardInputHandler;
import com.imeautochange.config.ClassConfigInfo;
import com.imeautochange.event.ModClientEventsHandlerBase;
import com.imeautochange.startup.IMESupport;

import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.ModList;

public class BuiltInModSupport extends IMESupport{
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
					DESCRIPTION_JEIINGREDIENTLISTOVERLAY,
					DISPLAYNAME_JEIINGREDIENTLISTOVERLAY,
					new String[] { DESCRIPTION_MOUSEINPUTAWARE, DESCRIPTION_KEYBOARDINPUTAWARE, DESCRIPTION_JEIPREFIXINPUTAWARE },
					new ITextComponent[] { DISPLAYNAME_MOUSEINPUTAWARE, DISPLAYNAME_KEYBOARDINPUTAWARE, DISPLAYNAME_JEIPREFIXINPUTAWARE }, 
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
