package com.imeautochange.config;

import java.util.HashMap;
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
	/**
	 * The class of the Screen this ClassConfigInfo represents.
	 * If that Gui is IOverlayAdapter, this should be null.
	 */
	public Class<?> clazz;
	/**
	 * The adapter for the Overlay this ClassConfigInfo represents.
	 * If that Gui is a Screen, this should be null.
	 */
	public IOverlayAdapter overlayAdapter;
	public ITextComponent displayName;
	public String description;
	public boolean isOverlay;
	public HashMap<String, ClassConfigItem> configItems;
	
	/**
	 * Deep copy constructor
	 * @param classConfigInfo
	 */
	public ClassConfigInfo(ClassConfigInfo classConfigInfo){
		this.clazz = classConfigInfo.clazz;
		this.overlayAdapter = classConfigInfo.overlayAdapter;
		this.displayName = classConfigInfo.displayName;
		this.description = classConfigInfo.description;
		this.isOverlay = classConfigInfo.isOverlay;
		this.configItems = new HashMap<String, ClassConfigItem>();
		for(Entry<String, ClassConfigItem> entry : classConfigInfo.configItems.entrySet()){
			this.configItems.put(entry.getKey(), new ClassConfigItem(entry.getValue()));
		}
	}
	
	/**
	 * Note here configItems is only a reference of input HashMap.
	 * @param clazz
	 * @param classDisplayName
	 * @param classDescription
	 * @param configItems
	 */
	public ClassConfigInfo(Class<?> clazz, String classDescription, ITextComponent classDisplayName, HashMap<String, ClassConfigItem> configItems){
		this.clazz = clazz;
		this.overlayAdapter = null;
		this.description = classDescription;
		this.displayName = classDisplayName;
		this.isOverlay = false;
		this.configItems = configItems;
	}
	
	/**
	 * A constructor with configItems built from its elements.
	 * @param clazz
	 * @param classDisplayName
	 * @param classDescription
	 * @param itemDescription
	 * @param itemDislayName
	 * @param defaultEnabled
	 * @param defaultIMEName
	 */
	public ClassConfigInfo(Class<?> clazz, String classDescription, ITextComponent classDisplayName, String[] itemDescription, ITextComponent[] itemDislayName, boolean[] defaultEnabled, String[] defaultIMEName){
		System.out.println("Creating ClassConfigInfo");
		this.clazz = clazz;
		this.overlayAdapter = null;
		this.displayName = classDisplayName;
		this.description = classDescription;
		this.isOverlay = false;
		this.configItems = new HashMap<String, ClassConfigItem>();
		int itemNum = itemDescription.length;
		if(itemNum>defaultEnabled.length) {
			itemNum = defaultEnabled.length;
		}
		if(itemNum>defaultIMEName.length) {
			itemNum = defaultIMEName.length;
		}
		if(itemNum > itemDislayName.length) {
			itemNum = itemDislayName.length;
		}
		for(int i =0;i<itemNum;i++) {
			System.out.println("itemDescription"+itemDescription[i]);
			this.configItems.put(itemDescription[i], new ClassConfigItem(itemDescription[i], itemDislayName[i], defaultIMEName[i], defaultEnabled[i]));
		}
	}
	
	/**
	 * Note here configItems is only a reference of input HashMap.
	 * @param clazz
	 * @param classDisplayName
	 * @param classDescription
	 * @param configItems
	 */
	public ClassConfigInfo(IOverlayAdapter overlayAdapter, String classDescription, ITextComponent classDisplayName, HashMap<String, ClassConfigItem> configItems){
		this.clazz = null;
		this.overlayAdapter = overlayAdapter;
		this.displayName = classDisplayName;
		this.description = classDescription;
		this.isOverlay = false;
		this.configItems = configItems;
	}
	
	/**
	 * A constructor with configItems built from its elements.
	 * @param overlayAdapter
	 * @param classDisplayName
	 * @param classDescription
	 * @param itemDescription
	 * @param itemDislayName
	 * @param defaultEnabled
	 * @param defaultIMEName
	 */
	public ClassConfigInfo(IOverlayAdapter overlayAdapter, String classDescription, ITextComponent classDisplayName, String[] itemDescription, ITextComponent[] itemDislayName, boolean[] defaultEnabled, String[] defaultIMEName){
		System.out.println("Creating ClassConfigInfo");
		this.clazz = null;
		this.overlayAdapter = overlayAdapter;
		this.displayName = classDisplayName;
		this.description = classDescription;
		this.isOverlay = true;
		this.configItems = new HashMap<String, ClassConfigItem>();
		int itemNum = itemDescription.length;
		if(itemNum>defaultEnabled.length) {
			itemNum = defaultEnabled.length;
		}
		if(itemNum>defaultIMEName.length) {
			itemNum = defaultIMEName.length;
		}
		if(itemNum > itemDislayName.length) {
			itemNum = itemDislayName.length;
		}
		for(int i =0;i<itemNum;i++) {
			System.out.println("itemDescription"+itemDescription[i]);
			this.configItems.put(itemDescription[i], new ClassConfigItem(itemDescription[i], itemDislayName[i], defaultIMEName[i], defaultEnabled[i]));
		}
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		if (isOverlay) {
			builder.append(String.format("Overlay Class Info\nclass name:\t%s\nConfig Description:\t%s",
					overlayAdapter.getOverlayClass().getName(), 
					description));
		} else {
			builder.append(String.format("Screen Class Info\nclass name:\t%s\nConfig Description:\t%s", 
					clazz.getName(),
					description));
		}
		builder.append("\nConfig Items:");
		int i = 0;
		for (Entry<String, ClassConfigItem> entry : configItems.entrySet()) {
			builder.append("\n(" + i + ") ");
			builder.append(entry.getValue().toString());
			i++;
		}
		return builder.toString();
	}
}
