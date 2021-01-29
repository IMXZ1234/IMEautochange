package com.IMEautochange.event;

import com.IMEautochange.IMEautochange;
import com.IMEautochange.compat.ModCompatEventsManager;
import com.IMEautochange.config.ModConfig;
import com.IMEautochange.config.ModKeys;
import com.IMEautochange.nativefunction.ModFunctionManager;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Controls registration of event listeners of this mod.
 * 
 * @author IMXZ
 *
 */
public class ModClientEventsManager {
	private static SignEditEventsHandler signEditEventsHandler;
	private static BookEditEventsHandler bookEditEventsHandler;
	private static BackToGameEventsHandler inGameEventsHandler;
	private static AnvilEventsHandler anvilEventsHandler;
	private static CreativeInventoryEventsHandler creativeInventoryEventsHandler;
	private static HotKeyInputEventsHandler hotKeyInputEventsHandler;
	private static CreativeInventoryEventsHandler.CreativeTabOpenEventsHandler creativeTabOpenEventsHandler;
	private static LanguageListReloadEventsHandler languageListReloadEventsHandler;
	
	public static void init() {
		/* Instantiate event listeners. */
		hotKeyInputEventsHandler = new HotKeyInputEventsHandler();
		signEditEventsHandler = new SignEditEventsHandler();
		bookEditEventsHandler = new BookEditEventsHandler();
		inGameEventsHandler = new BackToGameEventsHandler();
		anvilEventsHandler = new AnvilEventsHandler();
		creativeInventoryEventsHandler = new CreativeInventoryEventsHandler();
		creativeTabOpenEventsHandler = creativeInventoryEventsHandler.new CreativeTabOpenEventsHandler();
		languageListReloadEventsHandler = new LanguageListReloadEventsHandler();
		/* Register HotKeys input event listener, which will never be unregistered. */
		hotKeyInputEventsHandler.register();
		/* Register configuration change event listener, 
		 * which registers/unregisters other event listeners
		 * during the handling of configuration change event. */
		MinecraftForge.EVENT_BUS.register(ModClientEventsManager.class);
		/* Initialize compatibility. */
		ModCompatEventsManager.init();
		/* Initialize other event listeners. */
		updateHandlerRegistrationState();
	}

	/**
	 * When configuration is changed, enable/disable features by registering/unregistering
	 * corresponding event handlers.
	 * @param event
	 */
	@SubscribeEvent
	public static void onConfigChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.getModID().equals(IMEautochange.MODID)) {
			ConfigManager.sync(IMEautochange.MODID, Config.Type.INSTANCE);
			ModFunctionManager.setLanguageInGame(ModConfig.languageInGame, false);
			ModFunctionManager.setLanguageInGui(ModConfig.languageInGui, false);
			updateHandlerRegistrationState();
//			Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentString("option changed"));
		}
	}

	public static void updateHandlerRegistrationState() {
		if (ModConfig.isFunctionEnabledGuiSign) {
			signEditEventsHandler.register();
		} else {
			signEditEventsHandler.unregister();
		}
		if (ModConfig.isFunctionEnabledGuiScreenBook) {
			bookEditEventsHandler.register();
		} else {
			bookEditEventsHandler.unregister();
		}
		if (ModConfig.isFunctionEnabledGuiInGame) {
			inGameEventsHandler.register();
		} else {
			inGameEventsHandler.unregister();
		}
		if (ModConfig.isFunctionEnabledGuiRepair) {
			anvilEventsHandler.register();
		} else {
			anvilEventsHandler.unregister();
		}
		if (ModConfig.isFunctionEnabledGuiContainerCreative) {
			creativeInventoryEventsHandler.register();
			if (ModConfig.isFunctionEnabledCreativeTabOpen) {
				creativeTabOpenEventsHandler.register();
			} else {
				creativeTabOpenEventsHandler.unregister();
			}
		} else {
			creativeInventoryEventsHandler.unregister();
			creativeTabOpenEventsHandler.unregister();
		}
		if (ModConfig.automaticLanguageListReload) {
			languageListReloadEventsHandler.register();
		} else {
			languageListReloadEventsHandler.unregister();
		}
		/* Update compatibility registration state. */
		ModCompatEventsManager.updateHandlerRegistrationState();
	}
	
	public static boolean isCreativeInventoryEventsHandlerRegistered() {
		return creativeInventoryEventsHandler.getRegistrationState();
	}
	
	public static boolean isAnvilEventsHandlerRegistered() {
		return anvilEventsHandler.getRegistrationState();
	}
}
