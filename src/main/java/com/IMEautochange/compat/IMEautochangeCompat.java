package com.IMEautochange.compat;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;

public class IMEautochangeCompat 
{
	public static void init()
	{
		if (Loader.isModLoaded("patchouli"))
		{
			IMEautochangePatchouliCompat.init();
		}
		if (Loader.isModLoaded("botania"))
		{
			IMEautochangeBotaniaCompat.init();
		}
	}
	

}
