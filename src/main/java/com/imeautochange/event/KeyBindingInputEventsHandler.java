package com.imeautochange.event;

import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.Iterator;

import com.imeautochange.IMEautochange;
import com.imeautochange.config.ConfigScreen;
import com.imeautochange.config.IMEInfo;
import com.imeautochange.config.ModKeyBinding;
import com.imeautochange.nativefunction.NativeFunctionManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent.KeyInputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * This is not supposed to be unregistered.
 * @author IMXZ
 *
 */
public class KeyBindingInputEventsHandler extends ModClientEventsHandlerBase {
	public static IMEInfo imeInChat;
	@SubscribeEvent
	public void onKeyBindingInputEvent(KeyInputEvent event) {
		if (ModKeyBinding.OPEN_CHAT_WITH_IME_TOGGLED.isPressed()) {
			System.out.println("OPEN_CHAT_WITH_IME_TOGGLED");
//			ArrayList<IMEInfo> imeInfo = NativeFunctionManager.getIMEInfoList();
//			Iterator<IMEInfo> iter = imeInfo.iterator();
//			while (iter.hasNext()) {
//				IMEInfo info = iter.next();
//				System.out.println(info);
//			}
//			ArrayList<ModClientEventsHandlerBase> eventsHandlerList = EventsHandlerManager.getEventsHandlerList();
//			for(ModClientEventsHandlerBase handler:eventsHandlerList) {
//				System.out.println(handler.getClass().getName()+handler.HANDLER_ID);
//			}
			IMEInfo defaultIME = NativeFunctionManager.getDefaultIME();
			NativeFunctionManager.switchIMETo(defaultIME);
			System.out.println("defaultIME"+defaultIME);
			IMEInfo englishIME = NativeFunctionManager.getEnglishIME();
//			NativeFunctionManager.switchIMETo(englishIME);
			System.out.println("englishIME"+englishIME);
			Minecraft.getInstance().displayGuiScreen(new ChatScreen(""));
		} else if (ModKeyBinding.OPEN_CONFIG.isPressed()) {
//			if (Minecraft.getInstance().world.isRemote) {
//				DistExecutor.safeCallWhenOn(Dist.CLIENT, () -> OpenGUI::new);
//			}
			Minecraft.getInstance()
					.displayGuiScreen(new ConfigScreen(new TranslationTextComponent(IMEautochange.MOD_ID + ".test")));
		}

	}
	
}
