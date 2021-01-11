package com.IMEautochange.compat;

import java.io.InputStream;
import java.util.function.Predicate;
import java.util.function.Supplier;

import com.IMEautochange.IMEautochange;
import com.IMEautochange.config.ModConfig;
import com.IMEautochange.jna.IMEChangeManager;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.patchouli.api.IMultiblock;
import vazkii.patchouli.api.IStateMatcher;
import vazkii.patchouli.api.PatchouliAPI.IPatchouliAPI;
import vazkii.patchouli.client.book.gui.GuiBookEntryList;

public class IMEautochangePatchouliCompat 
{
	static boolean isLastGuiScreenGuiBookEntryList = false;
	public static void init()
	{
		IMEautochange.logger.info("Patchouli support is available.");
		MinecraftForge.EVENT_BUS.register(IMEautochangePatchouliCompat.class);
	}
	
	/**
	 * Have to use class outside Patchouli API
	 */
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void onGuiBookEntryListOpenEvent(GuiOpenEvent event)
	{
		if(ModConfig.isFunctionEnabledPatchouliSupport)
		{
			if (event.getGui() instanceof GuiBookEntryList)
			{
				IMEChangeManager.switchKL(false);
				isLastGuiScreenGuiBookEntryList = true;
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void onAnyGuiOpenEvent(GuiOpenEvent event)
	{
		if(ModConfig.isFunctionEnabledPatchouliSupport)
		{
			if (isLastGuiScreenGuiBookEntryList)
			{
				IMEChangeManager.switchKL(true);
				isLastGuiScreenGuiBookEntryList = false;
			}
		}
	}
}
