package com.imeautochange.config;

public class ConfigItem {
	public String imeName;
	public boolean enabled;
	public String defaultIMEName;
	public boolean defaultEnabled;
	
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
}
