package com.IMEautochange.proxy;

import java.io.File;

import com.IMEautochange.IMEautochange;
import com.IMEautochange.config.ModConfig;
import com.IMEautochange.config.ModKeys;
import com.IMEautochange.event.ModClientEventsManager;
import com.IMEautochange.nativefunction.ModFunctionManager;
import com.IMEautochange.util.OSChecker;

import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy
{
	public static File supportedLanguageListDirFile;
    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        super.preInit(event);
        supportedLanguageListDirFile = event.getModConfigurationDirectory().getParentFile();
    }

    @Override
    public void init(FMLInitializationEvent event)
    {
        IMEautochange.logger.info("IMEautochange start initializing...");
        super.init(event);
		ModFunctionManager.getNativeFunctionProvider();
		/* If current OS is supported. */
		if (ModFunctionManager.doesFunctionProviderExist()) {
			IMEautochange.logger.info("Current Platform: " + OSChecker.getPlatformFullName() + ".");
			/* Register HotKeys */
			ModKeys.init();
			/* Initialize event listeners. */
			ModClientEventsManager.init();
			ModFunctionManager.init(ModConfig.languageInGame, ModConfig.languageInGui, supportedLanguageListDirFile);
		} else {
			IMEautochange.logger.error("Stop Initialization, All Features Disabled.");
		}
    }

    @Override
    public void postInit(FMLPostInitializationEvent event)
    {
        super.postInit(event);
    }
    
    
    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
//        ModBlocks.initModels();
//        ModItems.initModels();
    }

}