package com.imeautochange.event;

public abstract class ModClientEventsHandlerSpecific extends ModClientEventsHandlerBase {
	
	protected String imeNameToSwitch;
	
	public int setIMENameToSwitch(String imeNameToSwitch) {
		this.imeNameToSwitch = imeNameToSwitch;
		return RESULT_SUCCESS;
	}
}
