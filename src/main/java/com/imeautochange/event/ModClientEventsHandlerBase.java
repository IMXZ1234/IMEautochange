package com.imeautochange.event;

import net.minecraftforge.common.MinecraftForge;

public abstract class ModClientEventsHandlerBase {
	public static final int RESULT_SUCCESS = 0x00000000;
	public static final int RESULT_FAIL = 0x10000000;
	
	protected boolean isRegistered;
	
	public ModClientEventsHandlerBase() {
		isRegistered = false;
	}
	
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
