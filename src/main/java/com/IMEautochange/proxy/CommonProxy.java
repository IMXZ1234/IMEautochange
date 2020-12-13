package com.IMEautochange.proxy;

import java.util.List;

import com.IMEautochange.IMEautochange;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreDictionary.OreRegisterEvent;

@Mod.EventBusSubscriber
public class CommonProxy
{
    public void preInit(FMLPreInitializationEvent event)
    {
    	
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
//    	ModItems.registerOreDicts();
//    	//OreDict
//    	List<ItemStack> ingotZincs = OreDictionary.getOres("ingotZinc");
//    	if(ingotZincs.isEmpty()) {
//    		System.out.println("No Ore Dict ingotZincs!");
//    	}else {
//    		for (ItemStack itemStack : ingotZincs)
//            {
//    			System.out.println("Ore Dict ingotZincs:"+itemStack.getItem().getRegistryName().toString());
//            }
//    	}
//    	OreDictionary.registerOre("ingotZinc",new ItemZincIngot());
//    	ingotZincs = OreDictionary.getOres("ingotZinc");
//    	if(ingotZincs.isEmpty()) {
//    		System.out.println("No Ore Dict ingotZincs!");
//    	}else {
//    		for (ItemStack itemStack : ingotZincs)
//            {
//    			System.out.println("Ore Dict ingotZincs:"+itemStack.getItem().getRegistryName().toString());
//            }
//    	}
    }

    public void postInit(FMLPostInitializationEvent event)
    {

    }
    
    @SubscribeEvent
    public static void registerBlocksEventHandler(RegistryEvent.Register<Block> event) 
    {
//    	//Blocks
//    	ModBlocks.registerBlocks(event.getRegistry());
//    	//TileEntities
//    	ModBlocks.registerTileEntities();
    }

    @SubscribeEvent
    public static void registerItemsEventHandler(RegistryEvent.Register<Item> event) 
    {
//    	//ItemBlocks
//    	ModBlocks.registerItemBlocks(event.getRegistry());
//    	//Items
//    	ModItems.registerItems(event.getRegistry());
    	
    }
}