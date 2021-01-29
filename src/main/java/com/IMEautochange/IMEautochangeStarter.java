package com.IMEautochange;

import com.IMEautochange.config.ModConfig;
import com.IMEautochange.config.ModKeys;
import com.IMEautochange.event.ModClientEventsManager;
import com.IMEautochange.nativefunction.ModFunctionManager;
import com.IMEautochange.util.OSChecker;

import net.minecraftforge.common.MinecraftForge;

/**
 * Used for startup, register event listeners, register HotKeys.
 * 
 * @author IMXZ
 *
 */
public class IMEautochangeStarter {
	public static void init() {
		ModFunctionManager.getNativeFunctionProvider();
		/* If current OS is supported. */
		if (ModFunctionManager.doesFunctionProviderExist()) {
			IMEautochange.logger.info("Current Platform: " + OSChecker.getPlatformFullName() + ".");
			/* Register HotKeys */
			ModKeys.init();
			/* Initialize event listeners. */
			ModClientEventsManager.init();
			ModFunctionManager.init(ModConfig.languageInGame, ModConfig.languageInGui);
		} else {
			IMEautochange.logger.error("Stop Initialization, All Features Disabled.");
		}
	}

}
