package com.IMEautochange.event;

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
public class ModClientEventHandler {
	
	static final String GuiRepairGuiTextFieldName = "field_147091_w";
	static final String GuiContainerCreativeGuiTextFieldName = "field_147062_A";
//	static final String GuiRepairGuiTextFieldName = "nameField";
//	static final String GuiContainerCreativeGuiTextFieldName = "searchField";
	static boolean isGuiRepairGuiTextFieldFocused = false;
	static boolean isGuiContainerCreativeGuiTextFieldFocused = false;
	static boolean isGuiOpenInGuiRepairPossible = false;
	
	public static void register() 
	{
		MinecraftForge.EVENT_BUS.register(ModClientEventHandler.class);
	}
	
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
//	/**
//	 * If chat gui is close, either by pressing enter or esc in chat gui screen, change keyboard layout to keyboardLayoutInGame
//	 * @param event
//	 */
//	//Chat gui is closed
//	@SideOnly(Side.CLIENT)
//	@SubscribeEvent//(priority = EventPriority.HIGHEST)
//	public static void onGuiChatKeyboardInputEvent(KeyboardInputEvent.Pre event)
//	{
//		if(Minecraft.getMinecraft().currentScreen instanceof GuiChat) {
////			if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
////				System.out.println("chat close by esc!");
////				//ModNativeMethods.ToggleIME();
////				ModNativeMethods.SetKLen_us();
////			}else if(Keyboard.isKeyDown(Keyboard.KEY_RETURN)) {
////				System.out.println("chat close by esc!");
////				ModNativeMethods.SetKLen_us();
////			}
//			if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) || Keyboard.isKeyDown(Keyboard.KEY_RETURN)) {
//				IMEChangeManager.switchKL(true);
//			}
//		}
//	}
	
	
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
	
    @SubscribeEvent
    public static void onGuiRepairClientTickEvent(ClientTickEvent event) throws NoSuchFieldException, IllegalAccessException 
    {
    	if (ModConfig.isFunctionEnabledGuiRepair) 
    	{
    		GuiScreen guiScreen = Minecraft.getMinecraft().currentScreen;
	    	if (guiScreen instanceof GuiRepair)
	    	{
	//    		guiScreen = (GuiRepair) guiScreen;
	            GuiTextField guiRepairTextField = (GuiTextField) ReflectionUtil.getPrivateField(GuiRepair.class, GuiRepairGuiTextFieldName, guiScreen);
	            if (isGuiRepairGuiTextFieldFocused && !guiRepairTextField.isFocused())
	            {
	                IMEChangeManager.switchKL(true);
	                isGuiRepairGuiTextFieldFocused = false;
	                isGuiOpenInGuiRepairPossible = false;
	            }
	            else if (!isGuiRepairGuiTextFieldFocused && guiRepairTextField.isFocused())
	            {
	            	IMEChangeManager.switchKL(false);
	            	isGuiRepairGuiTextFieldFocused = true;
	            }
	        }
    	}
    }
    /**
     * Fix GuiTextField in GuiRepair not losing focus when opening other Gui through mouse click
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
    		 * may be JEI Gui, etc
    		 */
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
		       	 if (Mouse.isButtonDown(0) && isGuiRepairGuiTextFieldFocused)
		       	 {
		       		isGuiOpenInGuiRepairPossible = true;
		       	 }
	        }
    	}
    }
    
    
    @SubscribeEvent
    public static void onGuiContainerCreativeClientTickEvent(ClientTickEvent event) throws NoSuchFieldException, IllegalAccessException 
    {
    	if (ModConfig.isFunctionEnabledGuiContainerCreative) 
    	{
    		GuiScreen guiScreen = Minecraft.getMinecraft().currentScreen;
	     	if (guiScreen instanceof GuiContainerCreative)
	     	{
	             GuiTextField guiRepairTextField = (GuiTextField) ReflectionUtil.getPrivateField(GuiContainerCreative.class, GuiContainerCreativeGuiTextFieldName, guiScreen);
	             if (isGuiContainerCreativeGuiTextFieldFocused && !guiRepairTextField.isFocused())
	             {
	                 IMEChangeManager.switchKL(true);
	                 isGuiContainerCreativeGuiTextFieldFocused = false;
	             }
	             else if (!isGuiContainerCreativeGuiTextFieldFocused && guiRepairTextField.isFocused())
	             {
	             	IMEChangeManager.switchKL(false);
	             	isGuiContainerCreativeGuiTextFieldFocused = true;
	             }
	         }
    	}
    }
    
    /**
     * 
     */
    
//    /**
//     * If mouse is clicked outside the GuiTextField in GuiContainerCreative,
//     * switch IME to language in game
//     * @param event
//     * @throws NoSuchFieldException
//     * @throws IllegalAccessException
//     */
//    @SubscribeEvent
//    public static void onGuiContainerCreativeMouseInputEvent(GuiScreenEvent.MouseInputEvent.Pre event) throws NoSuchFieldException, IllegalAccessException 
//    {
//    	if (ModConfig.isFunctionEnabledGuiContainerCreative) 
//    	{
//    		GuiScreen guiScreen = event.getGui();
//	     	if (guiScreen instanceof GuiContainerCreative)
//	     	{
//	             GuiTextField guiRepairTextField = (GuiTextField) ReflectionUtil.getPrivateField(GuiContainerCreative.class, GuiContainerCreativeGuiTextFieldName, guiScreen);
//	             /*if GuiTextField is focused, in other word, if in the search page of GuiContainerCreative.*/
//	             if (isGuiContainerCreativeGuiTextFieldFocused && guiRepairTextField.isFocused())
//	             {
//	            	 System.out.println("mouse input");
//	            	 /*if mouse left button down*/
//	            	 if (Mouse.isButtonDown(0))
//	            	 {
//	            		 System.out.println("button down");
//	            		 int mouseX = Mouse.getX();
//	            		 int mouseY = Mouse.getY();
//	            		 System.out.println(mouseX);
//	            		 System.out.println(mouseY);
//	            		 System.out.println(guiRepairTextField.x);
//	            		 System.out.println(guiRepairTextField.y);
//	            		 System.out.println(guiRepairTextField.width);
//	            		 System.out.println(guiRepairTextField.height);
//	            		 /*if mouse clicked in GuiTextField*/
//	            		 if (mouseX >= guiRepairTextField.x && mouseX <= guiRepairTextField.x + guiRepairTextField.width
//	            				 && mouseY >= guiRepairTextField.y && mouseY <= guiRepairTextField.y + guiRepairTextField.height)
//	            		 {
//	            			 IMEChangeManager.switchKL(true);
//	            		 }
//	            	 }
//	             }
//	         }
//    	}
//    }
    
//    @SubscribeEvent
//    public void onControlGUI(GuiOpenEvent event){
//        if (event.getGui() instanceof GuiContainer){
//            NativeUtils.inactiveInputMethod("");
//        }
//    }
    @SubscribeEvent
    public static void onGuiInGameOpenEvent(GuiOpenEvent event)
    {
        if (event.getGui() == null)
        {
        	IMEChangeManager.switchKL(true);
        }
    }
    
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
//    @SubscribeEvent
//    public void onCommandBlock(GuiOpenEvent event){
//        if (event.getGui() instanceof GuiCommandBlock){
//            NativeUtils.activeInputMethod("");
//        }
//    }
//    @SubscribeEvent
//    public void onCreateWorld(GuiOpenEvent event){
//        if (event.getGui() instanceof GuiCreateWorld){
//            NativeUtils.activeInputMethod("");
//        }
//    }
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
//    @SubscribeEvent
//    public void onRenameWorld(GuiOpenEvent event){
//        if (event.getGui() instanceof GuiWorldEdit){
//            NativeUtils.activeInputMethod("");
//        }
//    }
//    @SubscribeEvent
//    public void onAddServer(GuiOpenEvent event){
//        if (event.getGui() instanceof GuiScreenAddServer){
//            NativeUtils.activeInputMethod("");
//        }
//    }
//    @SubscribeEvent
//    public void onDirectJoinServer(GuiOpenEvent event){
//        if (event.getGui() instanceof GuiScreenServerList){
//            NativeUtils.activeInputMethod("");
//        }
//    }
//    @SubscribeEvent
//    public void onMultiPlayer(GuiOpenEvent event){
//        if (event.getGui() instanceof GuiMultiplayer){
//            NativeUtils.inactiveInputMethod("");
//        }
//    }
//    @SubscribeEvent
//    public void onSelectWorld(GuiOpenEvent event){
//        if (event.getGui() instanceof GuiWorldSelection){
//            NativeUtils.inactiveInputMethod("");
//        }
//    }
}
