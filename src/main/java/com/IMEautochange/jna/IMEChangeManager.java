package com.IMEautochange.jna;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentTranslation;

public class IMEChangeManager 
{
	public static final String keyWarningKLInGameNotInstalled = "text.imeautochange.warningKLInGameNotInstalled";
	public static final String keyWarningKLInGuiNotInstalled = "text.imeautochange.warningKLInGuiNotInstalled";
	public static final String keyWarningKLInGameNotSupported = "text.imeautochange.warningKLInGameNotSupported";
	public static final String keyWarningKLInGuiNotSupported = "text.imeautochange.warningKLInGuiNotSupported";
	public static final String keyInfoKLInGameInstalled = "text.imeautochange.infoKLInGameInstalled";
	public static final String keyInfoKLInGuiInstalled = "text.imeautochange.warningKLInGuiInstalled";
	private static long hKLInGame = 0x04090409;
	private static long hKLInGui = 0x08040804;
	private static boolean hasKLInGame;
	private static boolean hasKLInGui;
	private static long[] hKLList;
	private static int hKLnum;
	
	
	static {
		hKLnum = ModNativeMethods.gethKLListLen();
		hKLList = new long[hKLnum];
		ModNativeMethods.gethKLList(hKLnum, hKLList);
		hasKLInGame = hasKL(hKLInGame);
		hasKLInGui = hasKL(hKLInGui);
	}
	
	public static boolean hasKLInGame() 
	{
		return hasKLInGame;
	}
	
	public static boolean hasKLInGui() 
	{
		return hasKLInGui;
	}
	
	public static long gethKLInGame() 
	{
		return hKLInGame;
	}
	
	public static long gethKLInGui() 
	{
		return hKLInGui;
	}
	
	public static void reloadhKLList() 
	{
		hKLnum = ModNativeMethods.gethKLListLen();
		hKLList = new long[hKLnum];
		ModNativeMethods.gethKLList(hKLnum, hKLList);
	}
	
	/**
	 * Check if given keyboard layout is supported by this mod.
	 * @param hKL
	 * @return if is supported
	 */
	public static boolean isKLSupported(long hKL) 
	{
		return ModNativeMethods.KLTable.containsValue(hKL);
	}
	
	/**
	 * Check if given keyboard layout is installed in OS.
	 * @param hKL
	 * @return if is installed
	 */
	public static boolean hasKL(long hKL) 
	{
		boolean hasKL = false;
		for (int i = 0; i < hKLnum; i++) 
		{
			if (hKLList[i] == hKL) 
			{
				hasKL = true;
			}
		}
		return hasKL;
	}
	
	/**
	 * Set keyboard layout in game.
	 * @param keyboardLayoutInGame
	 * * @param suppressWarning If false, and if a keyboard layout is newly installed, will send an info message.
	 * @return if has successfully changed to a valid keyboard layout
	 */
	public static boolean sethKLInGame(String keyboardLayoutInGame, boolean suppressWarning) 
	{
		if (ModNativeMethods.KLTable.containsKey(keyboardLayoutInGame)) 
		{
			boolean hasKLInGameOld = hasKLInGame;
			hKLInGame = ModNativeMethods.KLTable.get(keyboardLayoutInGame);
			hasKLInGame = hasKL(hKLInGame);
			if (hasKLInGame)
			{
				/*newly installed keyboard layout*/
				if (suppressWarning && !hasKLInGameOld)
					Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentTranslation(keyInfoKLInGameInstalled));
				return true;
			}
			else
			{
				if (!suppressWarning)
					Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentTranslation(keyWarningKLInGameNotInstalled));
				return false;
			}
		}
		else 
		{
			hasKLInGame = false;
			if (!suppressWarning)
				Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentTranslation(keyWarningKLInGameNotSupported));
			return false;
		}
	}
	
	/**
	 * Set keyboard layout in Gui.
	 * @param keyboardLayoutInGui
	 * @param suppressWarning If false, and if a keyboard layout is newly installed, will send an info message.
	 * @return if has successfully changed to a valid keyboard layout
	 */
	public static boolean sethKLInGui(String keyboardLayoutInGui, boolean suppressWarning) 
	{
		if (ModNativeMethods.KLTable.containsKey(keyboardLayoutInGui)) 
		{
			boolean hasKLInGuiOld = hasKLInGui;
			hKLInGui = ModNativeMethods.KLTable.get(keyboardLayoutInGui);
			hasKLInGui = hasKL(hKLInGui);
			if (hasKLInGui)
			{
				/*newly installed keyboard layout*/
				if (suppressWarning && !hasKLInGuiOld)
					Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentTranslation(keyInfoKLInGuiInstalled));
				return true;
			}
			else
			{
				if (!suppressWarning)
					Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentTranslation(keyWarningKLInGuiNotInstalled));
				return false;
			}
		}
		else 
		{
			hasKLInGui = false;
			if (!suppressWarning)
				Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentTranslation(keyWarningKLInGuiNotSupported));
			return false;
		}
	}
	
	/**
	 * Switch Keyboard Layout
	 * @param      switchTo		true: switch to Keyboard Layout in game; 
	 * 						  	false: switch to Keyboard Layout in gui.
     * @return     if successfully switched.
     * 			   switching may not be successful since desired keyboard layout may have not been installed.
	 */
	public static boolean switchKL(boolean switchTo) {
		if (switchTo)
		{
			if (hasKLInGame)
			{
				ModNativeMethods.activateKL(hKLInGame);
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			if (hasKLInGui)
			{
				ModNativeMethods.activateKL(hKLInGui);
				return true;
			}
			else
			{
				return false;
			}
		}
	}

}
