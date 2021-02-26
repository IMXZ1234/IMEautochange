package com.imeautochange.compat;

import java.util.ArrayList;

import com.imeautochange.IMEautochange;
import com.imeautochange.util.ReflectionUtil;

import mezz.jei.Internal;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.runtime.IJeiRuntime;
import mezz.jei.gui.overlay.IngredientListOverlay;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.util.ResourceLocation;

@JeiPlugin
public class JEICompatIngredientListOverlayAdapter implements IOverlayAdapter, IModPlugin {
//	private JEICompatIngredientListOverlayAdapter() {}
	public static JEICompatIngredientListOverlayAdapter INSTANCE = new JEICompatIngredientListOverlayAdapter();
	
	private static IngredientListOverlay ingredientListOverlay;
	public static boolean isRuntimeCalled = false;
	/**
	 * On JEI startup, obtain JEI IngredientListOverlay instance.
	 */
	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
		isRuntimeCalled= true;
		ingredientListOverlay = (IngredientListOverlay) jeiRuntime.getIngredientListOverlay();
	}
	@Override
	public ResourceLocation getPluginUid() {
		return new ResourceLocation(IMEautochange.MOD_ID, IMEautochange.MOD_ID);
	}
	@Override
	public boolean isOverlayDisplayed() {
		if(ingredientListOverlay == null) {
			return false;
		}else {
			return ingredientListOverlay.isListDisplayed();
		}
	}
	@Override
	public boolean getTextFieldWidgets(ArrayList<TextFieldWidget> textFieldWidgets) {
		if(ingredientListOverlay == null) {
			return false;
		}else {
			return ReflectionUtil.getFieldObjectList(IngredientListOverlay.class, TextFieldWidget.class, ingredientListOverlay, textFieldWidgets);
		}
	}
	@Override
	public Class<?> getOverlayClass() {
		return IngredientListOverlay.class;
	}
}
