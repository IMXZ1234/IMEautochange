package com.imeautochange.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.imeautochange.compat.IOverlayAdapter;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;

/**
 * Every ClassConfigInfo should represent either a Screen or an Overlay.
 * Configurations about IMEautochange's function on that Gui are expected to be included in configItems.
 * Every entry in configItems should describe one certain IMEautochange's function on that Gui.
 * Since one event handler corresponds to one certain IMEautochange's function,
 * every entry in configItems should represent a combination of an event handler and its configuration.
 * Each entry in configItems is a pair of a String specifying a certain handler and a ConfigItem structure 
 * containing information about that handler, namely whether the handler is enabled and which IME the handler 
 * should switch to for this Screen/Overlay.
 * Note here clazz is not strictly restricted to Class Object of classes derived from {@link Screen} to accommodate {@link net.minecraft.client.gui.IngameGui},
 * but it should only be assigned Class Objects of those Gui classes with the property that when it is displayed, no other Screens will be displayed,
 * which is naturally possessed by classes derived from {@link Screen}.
 * 
 * @author IMXZ
 *
 */
public class ClassConfigInfo {
	public Class<?> clazz;
	public IOverlayAdapter overlayAdapter;
	public ITextComponent displayName;
	public boolean isOverlay;
	public HashMap<String, ConfigItem> configItems;
	
	/**
	 * Deep copy constructor
	 * @param classConfigInfo
	 */
	public ClassConfigInfo(ClassConfigInfo classConfigInfo){
		this.clazz = classConfigInfo.clazz;
		this.overlayAdapter = classConfigInfo.overlayAdapter;
		this.displayName = classConfigInfo.displayName;
		this.isOverlay = classConfigInfo.isOverlay;
		this.configItems = new HashMap<String, ConfigItem>();
		for(Entry<String, ConfigItem> entry : classConfigInfo.configItems.entrySet()){
			this.configItems.put(entry.getKey(), new ConfigItem(entry.getValue()));
		}
	}
	
	/**
	 * Note here configItems is only a reference of input HashMap.
	 * @param clazz
	 * @param imeName
	 * @param displayName
	 * @param isOverlay
	 * @param configItems
	 */
	public ClassConfigInfo(Class<?> clazz, ITextComponent displayName, HashMap<String, ConfigItem> configItems){
		this.clazz = clazz;
		this.overlayAdapter = null;
		this.displayName = displayName;
		this.isOverlay = false;
		this.configItems = configItems;
	}
	
	/**
	 * A constructor with configItems built from its elements.
	 * @param clazz
	 * @param imeName
	 * @param displayName
	 * @param isOverlay
	 * @param description
	 * @param value
	 */
	public ClassConfigInfo(Class<?> clazz, ITextComponent displayName, String[] description, boolean[] defaultEnabled, String[] defaultIMEName){
		System.out.println("Creating ClassConfigInfo");
		this.clazz = clazz;
		this.overlayAdapter = null;
		this.displayName = displayName;
		this.isOverlay = false;
		this.configItems = new HashMap<String, ConfigItem>();
		int itemNum = description.length;
		if(itemNum>defaultEnabled.length) {
			itemNum = defaultEnabled.length;
		}
		if(itemNum>defaultIMEName.length) {
			itemNum = defaultIMEName.length;
		}
		for(int i =0;i<itemNum;i++) {
			System.out.println("description"+description[i]);
			this.configItems.put(description[i], new ConfigItem(defaultIMEName[i], defaultEnabled[i]));
		}
	}
	
	/**
	 * Note here configItems is only a reference of input HashMap.
	 * @param clazz
	 * @param imeName
	 * @param displayName
	 * @param isOverlay
	 * @param configItems
	 */
	public ClassConfigInfo(IOverlayAdapter overlayAdapter, ITextComponent displayName, HashMap<String, ConfigItem> configItems){
		this.clazz = null;
		this.overlayAdapter = overlayAdapter;
		this.displayName = displayName;
		this.isOverlay = false;
		this.configItems = configItems;
	}
	
	/**
	 * A constructor with configItems built from its elements.
	 * @param clazz
	 * @param imeName
	 * @param displayName
	 * @param isOverlay
	 * @param description
	 * @param value
	 */
	public ClassConfigInfo(IOverlayAdapter overlayAdapter, ITextComponent displayName, String[] description, boolean[] defaultEnabled, String[] defaultIMEName){
		System.out.println("Creating ClassConfigInfo");
		this.clazz = null;
		this.overlayAdapter = overlayAdapter;
		this.displayName = displayName;
		this.isOverlay = true;
		this.configItems = new HashMap<String, ConfigItem>();
		int itemNum = description.length;
		if(itemNum>defaultEnabled.length) {
			itemNum = defaultEnabled.length;
		}
		if(itemNum>defaultIMEName.length) {
			itemNum = defaultIMEName.length;
		}
		for(int i =0;i<itemNum;i++) {
			System.out.println("description"+description[i]);
			this.configItems.put(description[i], new ConfigItem(defaultIMEName[i], defaultEnabled[i]));
		}
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		if (isOverlay) {
			builder.append(String.format("Overlay Class Info\nclass name:\t%s\nConfig Display Name:\t%s",
					overlayAdapter.getOverlayClass().getName(), 
					displayName));
		} else {
			builder.append(String.format("Screen Class Info\nclass name:\t%s\nConfig Display Name:\t%s", 
					clazz.getName(),
					displayName));
		}
		builder.append("\nConfig Items:");
		int i = 0;
		for (Entry<String, ConfigItem> entry : configItems.entrySet()) {
			builder.append("\n(" + i + ") ");
			builder.append(entry.getKey());
			ConfigItem configItem = entry.getValue();
			builder.append("\n\tDefault Config:");
			builder.append(configItem.defaultIMEName);
			builder.append("\t");
			builder.append(configItem.defaultEnabled ? "Enabled" : "Disabled");
			builder.append("\n\tCurrent Config:");
			builder.append(configItem.imeName);
			builder.append("\t");
			builder.append(configItem.enabled ? "Enabled" : "Disabled");
			i++;
		}
		return builder.toString();
	}
}
