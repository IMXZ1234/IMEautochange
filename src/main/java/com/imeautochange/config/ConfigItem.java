package com.imeautochange.config;

public class ConfigItem {
	public String imeName;
	public boolean enabled;
	public String defaultIMEName;
	public boolean defaultEnabled;
	
	public ConfigItem(ConfigItem configItem) {
		this.defaultEnabled = configItem.defaultEnabled;
		this.enabled = configItem.enabled;
		this.defaultIMEName = configItem.defaultIMEName;
		this.imeName = configItem.imeName;
	}
	
	public ConfigItem(String defaultIMEName, boolean defaultEnabled) {
		this.defaultEnabled = defaultEnabled;
		this.enabled = defaultEnabled;
		this.defaultIMEName = defaultIMEName;
		this.imeName = defaultIMEName;
	}
	
	public ConfigItem(String imeName, boolean enabled, String defaultIMEName, boolean defaultEnabled) {
		this.defaultEnabled = defaultEnabled;
		this.enabled = enabled;
		this.defaultIMEName = defaultIMEName;
		this.imeName = imeName;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Default Config:");
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
