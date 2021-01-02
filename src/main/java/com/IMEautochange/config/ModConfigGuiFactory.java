package com.IMEautochange.config;

import java.util.Collections;
import java.util.Set;

import com.IMEautochange.IMEautochange;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.GuiConfig;

public class ModConfigGuiFactory implements IModGuiFactory {
	@Override
    public void initialize(Minecraft mc) {
		
    }

    @Override
    public boolean hasConfigGui() {
        return true;
    }

    @Override
    public GuiScreen createConfigGui(GuiScreen parent) {
        return new GuiConfig(parent, ConfigElement.from(ModConfig.class).getChildElements(), IMEautochange.MODID, false, false, "First line", "General");
    }

    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
        return Collections.emptySet();
    }
}
