package com.IMEautochange.proxy;

//import org.codehaus.plexus.util.Os;

import com.IMEautochange.IMEautochange;
import com.IMEautochange.compat.IMEautochangeCompat;
import com.IMEautochange.event.ModClientEventHandler;
import com.IMEautochange.key.ModKeys;
import com.IMEautochange.proxy.CommonProxy;
import com.IMEautochange.util.OSChecker;

import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy
{
    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        super.preInit(event);
    }

    @Override
    public void init(FMLInitializationEvent event)
    {
    	IMEautochange.logger.info("IMEautochange start initializing.");
        super.init(event);
        //Since Windows native methods is used, this mod only works under Windows.
        if(OSChecker.isWindows())
        {
        	IMEautochange.logger.info("OS is Windows, main features enabled.");
        	ModKeys.registerKeys();
        	ModClientEventHandler.register();
        	IMEautochangeCompat.init();
        }
        else
        {
        	IMEautochange.logger.info("Current OS is not supprted, all features disabled.");
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