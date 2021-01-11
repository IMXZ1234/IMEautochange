package com.IMEautochange.config;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;

@Config(modid = "imeautochange")
@Config.LangKey("config.imeautochange.general")
public class ModConfig 
{
	@Config.Name("Keyboard Layout In Game")
	@Config.LangKey("config.imeautochange.general.keyboardLayoutInGame")
	@Comment("The language to be switched to in game.")
	public static String keyboardLayoutInGame = new String("en_us");
	
	@Config.Name("Keyboard Layout In Gui")
	@Config.LangKey("config.imeautochange.general.keyboardLayoutInGui")
	@Comment("The language to be switched to in gui: chatline, anvil textfield, etc.")
	public static String keyboardLayoutInGui = new String("zh_cn");
	
	@Config.Name("Enable Function Sign Editing")
	@Config.LangKey("config.imeautochange.general.isFunctionEnabledGuiSign")
	@Comment("Enable IME switching when editing sign.")
	public static boolean isFunctionEnabledGuiSign = true;
	
	@Config.Name("Enable Function Book Editing")
	@Config.LangKey("config.imeautochange.general.isFunctionEnabledGuiScreenBook")
	@Comment("Enable IME switching when editing book.")
	public static boolean isFunctionEnabledGuiScreenBook = true;
	
	@Config.Name("Enable Function Rename Item")
	@Config.LangKey("config.imeautochange.general.isFunctionEnabledGuiRepair")
	@Comment("Enable IME switching when renaming items in anvil.")
	public static boolean isFunctionEnabledGuiRepair = true;
	
	@Config.Name("Enable Function Creative Tab Searching")
	@Config.LangKey("config.imeautochange.general.isFunctionEnabledGuiContainerCreative")
	@Comment("Enable IME switching when searching in creative tab.")
	public static boolean isFunctionEnabledGuiContainerCreative = true;
	
	//Compatibility
	@Config.Name("Enable Function JEI Searching")
	@Config.LangKey("config.imeautochange.general.isFunctionEnabledJEISupport")
	@Comment("Enable IME switching when searching in JEI ingredient overlay.")
	public static boolean isFunctionEnabledJEISupport = true;
	
	@Config.Name("Enable Function Patchouli Book Searching")
	@Config.LangKey("config.imeautochange.general.isFunctionEnabledPatchouliSupport")
	@Comment("Enable IME switching when searching in Patchouli Gui BookEntryList.")
	public static boolean isFunctionEnabledPatchouliSupport = true;
	
	@Config.Name("Enable Function Botania Lexicon Searching")
	@Config.LangKey("config.imeautochange.general.isFunctionEnabledBotaniaSupport")
	@Comment("Enable IME switching when searching in Botania Lexicon Gui LexiconIndex.")
	public static boolean isFunctionEnabledBotaniaSupport = true;

}

