package com.imeautochange.config;

import net.minecraft.util.text.ITextComponent;

/**
 * Holds all of the display-related information and configuration of a certain
 * event handler on a certain Gui.
 * 
 * @author IMXZ
 *
 */
public class ConfigItem {
	/**
	 * Used to identify a certain handler in the internal, also serving as the key
	 * for this ConfigItem in the configuration file.
	 */
	public String description;
	/**
	 * Used to be displayed in {@link ConfigScreen}
	 */
	public ITextComponent displayName;
	public String imeName;
	public boolean enabled;
	public String defaultIMEName;
	public boolean defaultEnabled;

	public ConfigItem(ConfigItem configItem) {
		this.description = configItem.description;
		this.displayName = configItem.displayName;
		this.defaultEnabled = configItem.defaultEnabled;
		this.enabled = configItem.enabled;
		this.defaultIMEName = configItem.defaultIMEName;
		this.imeName = configItem.imeName;
	}

	public ConfigItem(String description, ITextComponent displayName, String defaultIMEName, boolean defaultEnabled) {
		this.description = description;
		this.displayName = displayName;
		this.defaultEnabled = defaultEnabled;
		this.enabled = defaultEnabled;
		this.defaultIMEName = defaultIMEName;
		this.imeName = defaultIMEName;
	}

	public ConfigItem(String description, ITextComponent displayName, String imeName, boolean enabled,
			String defaultIMEName, boolean defaultEnabled) {
		this.description = description;
		this.displayName = displayName;
		this.defaultEnabled = defaultEnabled;
		this.enabled = enabled;
		this.defaultIMEName = defaultIMEName;
		this.imeName = imeName;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ConfigItem:\t");
		builder.append(description);
		builder.append("\nDefault Config:");
		builder.append(defaultIMEName);
		builder.append("\t");
		builder.append(defaultEnabled ? "Enabled" : "Disabled");
		builder.append("\nCurrent Config:");
		builder.append(imeName);
		builder.append("\t");
		builder.append(enabled ? "Enabled" : "Disabled");
		return builder.toString();
	}
}
