package com.IMEautochange.config;

import net.minecraftforge.common.config.Config;

@Config(modid = "imeautochange")
@Config.LangKey("config.imeautochange.general") // 这个用于本地化，稍后会讲
public class ModConfig {
	
	@Config.Name("Keyboard Layout In Game")
	@Config.LangKey("config.imeautochange.general.keyboardLayoutInGame")
	public static String keyboardLayoutInGame = new String("en_us");
	
	@Config.Name("Keyboard Layout In Gui")
	@Config.LangKey("config.imeautochange.general.keyboardLayoutInGui")
	public static String keyboardLayoutInGui = new String("zh_cn");
	
	@Config.Name("Enable Function Sign Editing")
	@Config.LangKey("config.imeautochange.general.isFunctionEnabledGuiSign")
	public static boolean isFunctionEnabledGuiSign = true;
	
	@Config.Name("Enable Function Book Editing")
	@Config.LangKey("config.imeautochange.general.isFunctionEnabledGuiScreenBook")
	public static boolean isFunctionEnabledGuiScreenBook = true;
	
	@Config.Name("Enable Function Rename Item")
	@Config.LangKey("config.imeautochange.general.isFunctionEnabledGuiRepair")
	public static boolean isFunctionEnabledGuiRepair = true;
	
	@Config.Name("Enable Function Creative Tab Searching")
	@Config.LangKey("config.imeautochange.general.isFunctionEnabledGuiContainerCreative")
	public static boolean isFunctionEnabledGuiContainerCreative = true;
	
	//Compatibility
	@Config.Name("Enable Function JEI Searching")
	@Config.LangKey("config.imeautochange.general.isFunctionEnabledJEISupport")
	public static boolean isFunctionEnabledJEISupport = true;
	
}

