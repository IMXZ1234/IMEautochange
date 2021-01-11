package com.IMEautochange;

import com.sun.jna.Library;
import com.sun.jna.Native;

import org.apache.logging.log4j.Logger;

import com.IMEautochange.proxy.CommonProxy;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;

/**
 * @author IMXZ
 */
@Mod(modid = IMEautochange.MODID, name = IMEautochange.NAME, version = IMEautochange.VERSION, acceptedMinecraftVersions = "[1.12,1.13)", dependencies = "after:jei")
public class IMEautochange
{
    public static final String MODID = "imeautochange";
    public static final String NAME = "IME Auto Change";
    public static final String VERSION = "1.3.0";
    
    public static Logger logger;

    @Instance(IMEautochange.MODID)
    public static IMEautochange instance;
    
    @SidedProxy(clientSide = "com.IMEautochange.proxy.ClientProxy", serverSide = "com.IMEautochange.proxy.CommonProxy")
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    	logger = event.getModLog();
    	proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	proxy.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
    	proxy.postInit(event);
    }
}