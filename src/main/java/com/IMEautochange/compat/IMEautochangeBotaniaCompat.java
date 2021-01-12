package com.IMEautochange.compat;

import java.io.InputStream;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.lwjgl.input.Mouse;

import com.IMEautochange.IMEautochange;
import com.IMEautochange.config.ModConfig;
import com.IMEautochange.jna.IMEChangeManager;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.client.gui.lexicon.GuiLexicon;
import vazkii.botania.client.gui.lexicon.GuiLexiconIndex;
import vazkii.patchouli.api.IMultiblock;
import vazkii.patchouli.api.IStateMatcher;
import vazkii.patchouli.api.PatchouliAPI.IPatchouliAPI;
import vazkii.patchouli.client.book.gui.GuiBookEntryList;

public class IMEautochangeBotaniaCompat 
{
	public static void init()
	{
		IMEautochange.logger.info("Botania support is available.");
		MinecraftForge.EVENT_BUS.register(IMEautochangeBotaniaCompat.class);
	}
	
	/**
	 * Have to use class outside Botania API
	 */
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void onGuiLexiconIndexOpenEvent(GuiOpenEvent event)
	{
		if(ModConfig.isFunctionEnabledBotaniaSupport)
		{
			if (event.getGui() instanceof GuiLexiconIndex)
			{
				IMEChangeManager.switchKL(false);
			}
		}
	}
	
	/**
	 * Other Lexicon Guis are opened, which are without a GuiTextField.
	 * @param event
	 */
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void onGuiLexiconWithoutTextFieldOpenEvent(GuiOpenEvent event)
	{
		if(ModConfig.isFunctionEnabledBotaniaSupport)
		{
			GuiScreen guiScreen = event.getGui();
			if (!(guiScreen instanceof GuiLexiconIndex) && guiScreen instanceof GuiLexicon && !((GuiLexicon) guiScreen).notesEnabled)
			{
				IMEChangeManager.switchKL(true);
			}
		}
	}
	
	/**
	 * Handles Note open/close actions in the Lexicon.
	 * @param event
	 */
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void onGuiLexiconMouseInputEvent(GuiScreenEvent.MouseInputEvent event)
	{
		/*mouse button released*/
		if (Mouse.getEventButton() != -1 && Mouse.getEventButtonState())
		{
			if(ModConfig.isFunctionEnabledBotaniaSupport)
			{
				GuiScreen guiScreen = event.getGui();
				if (guiScreen instanceof GuiLexicon)
				{
					if (IMEChangeManager.isKLInGame && ((GuiLexicon) guiScreen).notesEnabled)
					{
						IMEChangeManager.switchKL(false);
					}
					else if (!IMEChangeManager.isKLInGame && !((GuiLexicon) guiScreen).notesEnabled && !(guiScreen instanceof GuiLexiconIndex))
					{
						IMEChangeManager.switchKL(true);
					}
				}
			}
		}
		
	}
}
