package com.imeautochange.compat;

import com.imeautochange.event.ModClientEventsHandler;

import net.minecraftforge.common.MinecraftForge;

public abstract class ModCompatEventsHandler extends ModClientEventsHandler {
	private boolean isModLoaded = false;
	
	@Override
	public void updateRegistrationState() {
		if (isModLoaded) {
			if (!isRegistered) {
				MinecraftForge.EVENT_BUS.register(this);
				isRegistered = true;
			} else {
				MinecraftForge.EVENT_BUS.unregister(this);
				isRegistered = false;
			}
		}
	}

	@Override
	public void register() {
		if (isModLoaded) {
			if (!isRegistered) {
				MinecraftForge.EVENT_BUS.register(this);
				isRegistered = true;
			}
		}
	}

	@Override
	public void unregister() {
		if (isModLoaded) {
			if (isRegistered) {
				MinecraftForge.EVENT_BUS.unregister(this);
				isRegistered = false;
			}
		}
	}
	
	public void setModLoadedState(boolean isLoaded) {
		isModLoaded = isLoaded;
	}
	
	public boolean getModLoadedState() {
		return isModLoaded;
	}
}
