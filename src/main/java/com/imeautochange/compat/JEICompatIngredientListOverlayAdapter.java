package com.imeautochange.compat;

import java.lang.reflect.Field;
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
public class JEICompatIngredientListOverlayAdapter implements ICompatOverlayAdapter, IModPlugin {
//	private JEICompatIngredientListOverlayAdapter() {}
	public static JEICompatIngredientListOverlayAdapter INSTANCE = new JEICompatIngredientListOverlayAdapter();
	/**
	 * JEI IngredientListOverlay is not a Gui! Surprising!
	 */
	private static IngredientListOverlay ingredientListOverlay;
	public static boolean isRuntimeCalled = false;
	/**
	 * On startup, obtain JEI IngredientListOverlay instance.
	 */
	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
		isRuntimeCalled= true;
		System.out.println("runtime called! "+(isRuntimeCalled));
		ingredientListOverlay = Internal.getRuntime().getIngredientListOverlay();
		System.out.println("ingredientListOverlay is null? " + (ingredientListOverlay == null));
		ingredientListOverlay = (IngredientListOverlay) jeiRuntime.getIngredientListOverlay();
		System.out.println("ingredientListOverlay is null? " + (ingredientListOverlay == null));
	}
	@Override
	public ResourceLocation getPluginUid() {
		return new ResourceLocation(IMEautochange.MOD_ID, IMEautochange.MOD_ID);
	}
	@Override
	public boolean isOverlayDisplayed() {
		System.out.println("runtime is called? "+(isRuntimeCalled));
		System.out.println("ingredientListOverlay is null? "+(ingredientListOverlay==null));
//		ingredientListOverlay = Internal.getRuntime().getIngredientListOverlay();
		if(ingredientListOverlay == null) {
			return false;
		}else {
			System.out.println("ingredientListOverlay is displayed? "+(ingredientListOverlay.isListDisplayed()));
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
