package com.imeautochange.startup;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class IMESupportManager {
	public static ArrayList<IMESupport> supportList = new ArrayList<IMESupport>();
	private static Logger LOGGER = LogManager.getLogger();
	public static void registerIMESupport(IMESupport support) {
		supportList.add(support);
	}
	public static void initAllIMESupports() {
		for(IMESupport support : supportList) {
			LOGGER.info("Initializing "+support.getClass().getName());
			support.initIMESupport();
		}
	}
}
