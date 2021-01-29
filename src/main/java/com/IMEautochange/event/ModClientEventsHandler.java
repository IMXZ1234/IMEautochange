package com.IMEautochange.event;

import net.minecraftforge.common.MinecraftForge;

public abstract class ModClientEventsHandler {
	protected boolean isRegistered = false;

	public void updateRegistrationState() {
		if (!isRegistered) {
			MinecraftForge.EVENT_BUS.register(this);
			isRegistered = true;
		} else {
			MinecraftForge.EVENT_BUS.unregister(this);
			isRegistered = false;
		}
	}

	public void register() {
		if (!isRegistered) {
			MinecraftForge.EVENT_BUS.register(this);
			isRegistered = true;
		}
	}

	public void unregister() {
		if (isRegistered) {
			MinecraftForge.EVENT_BUS.unregister(this);
			isRegistered = false;
		}
	}
	
	public boolean getRegistrationState() {
		return isRegistered;
	}
}
