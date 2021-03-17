package com.imeautochange.config;

import net.minecraft.util.text.ITextComponent;

public class GeneralConfigItem {
	/**
	 * Serving as the key for this GeneralConfigItem in the configuration file.
	 */
	public String description;
	/**
	 * Used to be displayed in Config Screens
	 */
	public ITextComponent displayName;
	public Object value;
	public GeneralConfigItem(String description, ITextComponent displayName, Object value){
		this.description = description;
		this.displayName = displayName;
		this.value = value;
	}
	public GeneralConfigItem(GeneralConfigItem generalConfigItem){
		this.description = generalConfigItem.description;
		this.displayName = generalConfigItem.displayName;
		this.value = generalConfigItem.value;
	}
}
