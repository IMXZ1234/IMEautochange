package com.IMEautochange.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.IMEautochange.IMEautochange;
import com.IMEautochange.config.ModConfig;
import com.IMEautochange.jna.IMEChangeManager;
import com.IMEautochange.key.ModKeys;
import com.IMEautochange.util.ReflectionUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiRepair;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.GuiScreenEvent.KeyboardInputEvent;
import net.minecraftforge.client.event.GuiScreenEvent.MouseInputEvent;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;



//@Mod.EventBusSubscriber(Side.CLIENT)
public class ModClientEventHandler 
{
	static final String GuiRepairGuiTextFieldName = "field_147091_w";
	static final String GuiContainerCreativeGuiTextFieldName = "field_147062_A";
//	static final String GuiContainerCreativeSelectedTabIndexName = "field_147058_w";
	static final String GuiContainerCreativeTabPageName = "tabPage";
	static final String GuiContainerCreativeIsMouseOverTabName = "func_147049_a";

//	static final String GuiRepairGuiTextFieldName = "nameField";
//	static final String GuiContainerCreativeGuiTextFieldName = "searchField";
////	static final String GuiContainerCreativeSelectedTabIndexName = "selectedTabIndex";
//	static final String GuiContainerCreativeTabPageName = "tabPage";
//	static final String GuiContainerCreativeIsMouseOverTabName = "isMouseOverTab";
	
//	static boolean isGuiRepairGuiTextFieldFocused = false;
//	static boolean isGuiContainerCreativeGuiTextFieldFocused = false;
	static boolean isGuiOpenInGuiRepairPossible = false;
	
	public static void register() 
	{
		MinecraftForge.EVENT_BUS.register(ModClientEventHandler.class);
	}
	
	// ================================================================================================
	// Chat
	// ================================================================================================
	
	/**
	 * If registered hotkey(open chat with IME toggled) is pressed, change keyboard layout to keyboardLayoutInGui
	 * @param event
	 */
	//Chat gui is opened
	@SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onHotKeyInputEvent(InputEvent.KeyInputEvent event)
    {
        if (ModKeys.openChat.isPressed())
        {
        	IMEChangeManager.switchKL(false);
			Minecraft.getMinecraft().displayGuiScreen(new GuiChat());
		}
    }
	
	// ================================================================================================
	// Sign Edit
	// ================================================================================================
	
	@SubscribeEvent
    public static void onGuiSignOpenEvent(GuiOpenEvent event)
	{
		if (ModConfig.isFunctionEnabledGuiSign) 
		{
	        if (event.getGui() instanceof GuiEditSign)
	        {
	        	IMEChangeManager.switchKL(false);
	        }
		}
    }
    
	// ================================================================================================
	// Book Editing
	// ================================================================================================
	
    @SubscribeEvent
    public static void onGuiScreenBookOpenEvent(GuiOpenEvent event)
    {
    	if (ModConfig.isFunctionEnabledGuiScreenBook)
    	{
	        if (event.getGui() instanceof GuiScreenBook)
	        {
	        	IMEChangeManager.switchKL(false);
	        }
    	}
    }
    
	// ================================================================================================
	// Anvil Rename
	// ================================================================================================
	
    @SubscribeEvent
    public static void onGuiRepairClientTickEvent(ClientTickEvent event) throws NoSuchFieldException, IllegalAccessException 
    {
    	if (ModConfig.isFunctionEnabledGuiRepair) 
    	{
    		GuiScreen guiScreen = Minecraft.getMinecraft().currentScreen;
	    	if (guiScreen instanceof GuiRepair)
	    	{
	            GuiTextField guiRepairTextField = (GuiTextField) ReflectionUtil.getPrivateField(GuiRepair.class, GuiRepairGuiTextFieldName, guiScreen);
	            if (!IMEChangeManager.isKLInGame && !guiRepairTextField.isFocused())
	            {
	                IMEChangeManager.switchKL(true);
	                isGuiOpenInGuiRepairPossible = false;
	            }
	            else if (IMEChangeManager.isKLInGame && guiRepairTextField.isFocused())
	            {
	            	IMEChangeManager.switchKL(false);
	            }
	        }
    	}
    }
    /**
     * Fix GuiTextField in GuiRepair not losing focus when opening other Gui through mouse click.
     * This is because MinecraftForge gets Event Listeners added by other mods informed of mouse input before 
     * vanilla logic is performed.
     * @param event
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    @SubscribeEvent
    public static void onGuiInGuiRepairGuiOpenEvent(GuiOpenEvent event)
    {
    	/* not back to GuiInGame */
    	if (event.getGui() != null)
        {
    		/* other Gui opened in GuiRepair
    		 * may be JEI Gui(most likely), etc*/
    		if (isGuiOpenInGuiRepairPossible) 
    		{
    			IMEChangeManager.switchKL(true);
    		}
        }
    }
    
    @SubscribeEvent
    public static void onGuiRepairMouseEvent(GuiScreenEvent.MouseInputEvent.Pre event) throws NoSuchFieldException, IllegalAccessException 
    {
    	if (ModConfig.isFunctionEnabledGuiRepair) 
    	{
    		/*Now */
    		GuiScreen guiScreen = Minecraft.getMinecraft().currentScreen;
	    	if (guiScreen instanceof GuiRepair)
	    	{
	    		 /*if mouse left button down*/
		       	 if (Mouse.isButtonDown(0) && !IMEChangeManager.isKLInGame)
		       	 {
		       		isGuiOpenInGuiRepairPossible = true;
		       	 }
	        }
    	}
    }
    
	// ================================================================================================
	// Creative Tab Searching
	// ================================================================================================
	
//    @SubscribeEvent
//    public static void onGuiContainerCreativeClientTickEvent(ClientTickEvent event) throws NoSuchFieldException, IllegalAccessException 
//    {
//    	if (ModConfig.isFunctionEnabledGuiContainerCreative) 
//    	{
//    		GuiScreen guiScreen = Minecraft.getMinecraft().currentScreen;
//	     	if (guiScreen instanceof GuiContainerCreative)
//	     	{
//	             GuiTextField guiRepairTextField = (GuiTextField) ReflectionUtil.getPrivateField(GuiContainerCreative.class, GuiContainerCreativeGuiTextFieldName, guiScreen);
//	             if (!IMEChangeManager.isKLInGame && !guiRepairTextField.isFocused())
//	             {
//	                 IMEChangeManager.switchKL(true);
//	             }
//	             else if (IMEChangeManager.isKLInGame && guiRepairTextField.isFocused())
//	             {
//	             	IMEChangeManager.switchKL(false);
//	             }
//	         }
//    	}
//    }
    /**
     * On opening GuiContainerCreative, switch IME if current tab has a GuiTextField
     * @param event
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    @SubscribeEvent
	public static void onGuiContainerCreativeOpenEvent(GuiOpenEvent event) throws NoSuchFieldException, IllegalAccessException 
	{
    	if (ModConfig.isFunctionEnabledGuiContainerCreative) 
		{
			GuiScreen guiScreen = event.getGui();
			if (guiScreen instanceof GuiContainerCreative)
			{
				int selectedTabIndex = ((GuiContainerCreative) guiScreen).getSelectedTabIndex();
				if (CreativeTabs.CREATIVE_TAB_ARRAY[selectedTabIndex].hasSearchBar())
				{
					IMEChangeManager.switchKL(false);
				}
				else
				{
					IMEChangeManager.switchKL(true);
				}
			}
		}
	}
    
	/**
	 * Handle mouse click in GuiContainerCreative,
	 * switch IME according to mouse position.
	 * @param event
	 * @throws NoSuchFieldException
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 */
	@SubscribeEvent
	public static void onGuiContainerCreativeMouseInputEvent(GuiScreenEvent.MouseInputEvent.Pre event) throws NoSuchFieldException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException 
	{
		/*if mouse left or right button clicked*/
		if (Mouse.getEventButtonState())
		{
			/*if mouse left button down*/
			if (Mouse.isButtonDown(0))
			{
				System.out.println("button down");
				if (ModConfig.isFunctionEnabledGuiContainerCreative) 
				{
					GuiScreen guiScreen = event.getGui();
					if (guiScreen instanceof GuiContainerCreative)
					{
						GuiTextField guiRepairTextField = (GuiTextField) ReflectionUtil.getPrivateField(GuiContainerCreative.class, GuiContainerCreativeGuiTextFieldName, guiScreen);
						/*if GuiTextField is focused, in other word, if in the search tab of GuiContainerCreative.*/
						if (guiRepairTextField.isFocused())
						{
							int mouseX = Mouse.getEventX() * guiScreen.width / guiScreen.mc.displayWidth;
							int mouseY = guiScreen.height - Mouse.getEventY() * guiScreen.height / guiScreen.mc.displayHeight - 1;
							/*if mouse clicked in GuiTextField*/
							if (mouseX < guiRepairTextField.x || mouseX > guiRepairTextField.x + guiRepairTextField.width 
									|| mouseY < guiRepairTextField.y || mouseY > guiRepairTextField.y + guiRepairTextField.height)
							{
								IMEChangeManager.switchKL(true);
							}
							else 
							{
								IMEChangeManager.switchKL(false);
							}
						}
					}
				}
			}
		}
		/*mouse button released*/
		else if (Mouse.getEventButton() != -1)
		{
			System.out.println("button lift");
			if (ModConfig.isFunctionEnabledGuiContainerCreative) 
			{
				GuiScreen guiScreen = event.getGui();
				if (guiScreen instanceof GuiContainerCreative)
				{
					GuiTextField guiRepairTextField = (GuiTextField) ReflectionUtil.getPrivateField(GuiContainerCreative.class, GuiContainerCreativeGuiTextFieldName, guiScreen);
					int tabPage = (int) ReflectionUtil.getPrivateField(GuiContainerCreative.class, GuiContainerCreativeTabPageName, guiScreen);
//					Method isMouseOverTab = ReflectionUtil.getPrivateMethod(GuiContainerCreative.class, GuiContainerCreativeIsMouseOverTabName, guiScreen, CreativeTabs.class, int.class, int.class);
//					ReflectionUtil.printReflectedMethodInfo(isMouseOverTab);
					for (CreativeTabs tab : CreativeTabs.CREATIVE_TAB_ARRAY)
					{
						/* check if mouse is over a creative tab, if so that tab is going to be activated
						 * using code from GuiContainerCreative.isMouseOverTab */
						if (tab.getTabPage() != tabPage)
				        {
				            if (tab != CreativeTabs.SEARCH && tab != CreativeTabs.INVENTORY)
				            {
				                continue;
				            }
				        }
						int i = tab.getColumn();
				        int j = 28 * i;
				        int k = 0;

				        if (tab.isAlignedRight())
				        {
				            j = ((GuiContainerCreative) guiScreen).getXSize() - 28 * (6 - i) + 2;
				        }
				        else if (i > 0)
				        {
				            j += i;
				        }

				        if (tab.isOnTopRow())
				        {
				            k = k - 32;
				        }
				        else
				        {
				            k = k + ((GuiContainerCreative) guiScreen).getYSize();
				        }
				        int mouseX = Mouse.getEventX() * guiScreen.width / guiScreen.mc.displayWidth - ((GuiContainer) guiScreen).getGuiLeft();
						int mouseY = guiScreen.height - Mouse.getEventY() * guiScreen.height / guiScreen.mc.displayHeight - 1 - ((GuiContainer) guiScreen).getGuiTop();
						
				        if (mouseX >= j && mouseX <= j + 28 && mouseY >= k && mouseY <= k + 32)
				        {
				        	if (tab.hasSearchBar())
		                    {
		                    	IMEChangeManager.switchKL(false);
		                    }
		                    else
		                    {
		                    	IMEChangeManager.switchKL(true);
		                    }
		                    break;
				        }
					}
				}
			}
		}
	}
    
	// ================================================================================================
	// In Game
	// ================================================================================================
	
	@SubscribeEvent
	public static void onGuiInGameOpenEvent(GuiOpenEvent event)
	{
	    if (event.getGui() == null)
	    {
	    	IMEChangeManager.switchKL(true);
	    }
	}
    
    // ================================================================================================
 	// Keyboard Layout Newly Installed
 	// ================================================================================================
 	
     /**
      * When any Gui is opened, reload the keyboard layout list, in case there is newly installed keyboard layout.
      * @param event
      */
     @SubscribeEvent
     public static void onAnyGuiOpenEvent(GuiOpenEvent event)
     {
         if (event.getGui() != null)
         {
         	IMEChangeManager.reloadhKLList();
         	IMEChangeManager.sethKLInGame(ModConfig.keyboardLayoutInGame, true);
            IMEChangeManager.sethKLInGui(ModConfig.keyboardLayoutInGui, true);
         }
     }
     
 	
 	// ================================================================================================
 	// Configuration change
 	// ================================================================================================
 	
 	//Config is changed
     @SubscribeEvent
     public static void onConfigChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event) 
     {
         if (event.getModID().equals(IMEautochange.MODID)) 
         {
             ConfigManager.sync(IMEautochange.MODID, Config.Type.INSTANCE);
             IMEChangeManager.sethKLInGame(ModConfig.keyboardLayoutInGame, false);
             IMEChangeManager.sethKLInGui(ModConfig.keyboardLayoutInGui, false);
         }
     }
    
//    /**
//     * Print a warning message when first joining a world if there is problem with language support/availability
//     * @param event
//     */
//    @SubscribeEvent
//    public static void onWorldLoaded(WorldEvent.Load event)
//    {
//    	System.out.println("world loaded");
//    	if (!IMEChangeManager.hasKLInGame())
//    	{
//    		Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentTranslation(IMEChangeManager.keyWarningKLInGameNotInstalled));
//    	}
//    	if (!IMEChangeManager.hasKLInGui())
//    	{
//    		Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentTranslation(IMEChangeManager.keyWarningKLInGuiNotInstalled));
//    	}
//    	if (!IMEChangeManager.isKLSupported(IMEChangeManager.gethKLInGame()))
//    	{
//    		Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentTranslation(IMEChangeManager.keyWarningKLInGameNotSupported));
//    	}
//    	if (!IMEChangeManager.isKLSupported(IMEChangeManager.gethKLInGui()))
//    	{
//    		Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentTranslation(IMEChangeManager.keyWarningKLInGuiNotSupported));
//    	}
//    }
    
}
