package com.IMEautochange.compat;

import com.IMEautochange.IMEautochange;
import com.IMEautochange.config.ModConfig;
import com.IMEautochange.event.AnvilEventsHandler;
import com.IMEautochange.event.BackToGameEventsHandler;
import com.IMEautochange.event.BookEditEventsHandler;
import com.IMEautochange.event.CreativeInventoryEventsHandler;
import com.IMEautochange.event.HotKeyInputEventsHandler;
import com.IMEautochange.event.ModClientEventsManager;
import com.IMEautochange.event.SignEditEventsHandler;
import com.IMEautochange.event.CreativeInventoryEventsHandler.CreativeTabOpenEventsHandler;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;

/**
 * Controls registration of compatibility event listeners.
 * @author IMXZ
 *
 */
public class ModCompatEventsManager {
	private static BotaniaCompatEventsHandler botaniaCompatEventsHandler;
	private static PatchouliCompatEventsHandler patchouliCompatEventsHandler;
	private static JEICompatEventsHandler jeiCompatEventsHandler;
	
	public static void init() {
		/* Instantiate event listeners. */
		botaniaCompatEventsHandler = new BotaniaCompatEventsHandler();
		patchouliCompatEventsHandler = new PatchouliCompatEventsHandler();
		jeiCompatEventsHandler = new JEICompatEventsHandler();
		/* Initialize event listeners. */
		if (Loader.isModLoaded("botania")) {
			botaniaCompatEventsHandler.setModLoadedState(true);
			IMEautochange.logger.info("Botania support is available.");
		}
		if (Loader.isModLoaded("patchouli")) {
			patchouliCompatEventsHandler.setModLoadedState(true);
			IMEautochange.logger.info("Ptchouli support is available.");
		}
		if (Loader.isModLoaded("jei")) {
			jeiCompatEventsHandler.setModLoadedState(true);
			IMEautochange.logger.info("JEI support is available.");
		}
		updateHandlerRegistrationState();
	}

	public static void updateHandlerRegistrationState() {
		if (ModConfig.isFunctionEnabledBotaniaSupport) {
			botaniaCompatEventsHandler.register();
		} else {
			botaniaCompatEventsHandler.unregister();
		}
		if (ModConfig.isFunctionEnabledPatchouliSupport) {
			patchouliCompatEventsHandler.register();
		} else {
			patchouliCompatEventsHandler.unregister();
		}
		if (ModConfig.isFunctionEnabledJEISupport) {
			jeiCompatEventsHandler.register();
		} else {
			jeiCompatEventsHandler.unregister();
		}
	}
}
