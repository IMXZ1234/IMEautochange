package com.imeautochange;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/**
 * @author IMXZ
 */
//The value here should match an entry in the META-INF/mods.toml file
@Mod("imeautochange")
public class IMEautochange
{
 // Directly reference a log4j logger.
 private static final Logger LOGGER = LogManager.getLogger();
 public static final String MOD_ID = "imeautochange";

 public IMEautochange() {
     // Register the setup method for modloading
     FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
     // Register the enqueueIMC method for modloading
     FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
     // Register the processIMC method for modloading
     FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
     // Register the doClientStuff method for modloading
     FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
//     ModLoadingContext.get().registerConfig(type, spec);
     // Register ourselves for server and other game events we are interested in
     MinecraftForge.EVENT_BUS.register(this);
 }

 private void setup(final FMLCommonSetupEvent event)
 {
     // some preinit code
 }

 private void doClientStuff(final FMLClientSetupEvent event) {
     // do something that can only be done on the client
     ModFunctionManager.init(event);
     if(!ModFunctionManager.isFunctionEnabled()) {
    	 LOGGER.error("Error in initializing, all functions disabled.");
    	 return;
     }
     LOGGER.info("IMEautochange successfully initialized.");
 }

 private void enqueueIMC(final InterModEnqueueEvent event)
 {
     // some example code to dispatch IMC to another mod
 }

 private void processIMC(final InterModProcessEvent event)
 {
     // some example code to receive and process InterModComms from other mods
 }
 // You can use SubscribeEvent and let the Event Bus discover methods to call
 @SubscribeEvent
 public void onServerStarting(FMLServerStartingEvent event) {
     // do something when the server starts
 }

 // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
 // Event bus for receiving Registry Events)
 @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
 public static class RegistryEvents {
     @SubscribeEvent
     public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
         // register a new block here
     }
 }
}
