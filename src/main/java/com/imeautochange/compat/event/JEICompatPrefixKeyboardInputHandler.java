package com.imeautochange.compat.event;

import java.util.ArrayList;

import com.imeautochange.compat.JEICompatIngredientListOverlayAdapter;
import com.imeautochange.event.ModClientEventsHandlerSpecific;
import com.imeautochange.nativefunction.NativeFunctionManager;
import com.imeautochange.util.MouseOverUtil;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.runtime.IJeiRuntime;
import mezz.jei.gui.overlay.IngredientListOverlay;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiScreenEvent.KeyboardCharTypedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class JEICompatPrefixKeyboardInputHandler extends ModClientEventsHandlerSpecific {

	/**
	 * When these prefixes are typed in JEI IngredientListOverlay search box, IME
	 * should be switched to IME in Game.
	 */
	public final char[] JEIPrefixList = new char[] { '^', '@', '$', '&' };
	JEICompatIngredientListOverlayAdapter overlayAdapter = JEICompatIngredientListOverlayAdapter.INSTANCE;
	private final ArrayList<TextFieldWidget> textFieldList = new ArrayList<TextFieldWidget>();
	
	@SubscribeEvent
	public void onJEIIngredientListOverlayPrefixKeyboardInput(KeyboardCharTypedEvent event) {
		if (overlayAdapter.getTextFieldWidgets(textFieldList)) {
			if (!textFieldList.isEmpty()) {
				TextFieldWidget textField = textFieldList.get(0);
				if (textField.isFocused()) {
					for (int i = 0; i < JEIPrefixList.length; i++) {
						if (event.getCodePoint() == JEIPrefixList[i]) {
							NativeFunctionManager.switchIMETo(imeNameToSwitch);
						}
					}
				}
			}
		}
	}
}
