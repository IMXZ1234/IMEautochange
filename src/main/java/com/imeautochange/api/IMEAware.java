package com.imeautochange.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.minecraft.client.gui.widget.TextFieldWidget;

/**
 * Put this before your IME-aware Gui classes to enable IMEautochange functionality.
 * Fields in class with this annotation will be checked 
 * @author IMXZ
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface IMEAware {
	/**
	 * Set this to true if you want IMEautochange to function in this Gui. text fields, either custom or derived from {@link TextFieldWidget}
	 * @return
	 */
	boolean functionEnabled() default true;
	/**
	 * Set this to true if you want IMEautochange to
	 * @return
	 */
	boolean switchOnOpening() default false;
	String displayName() default "";
}
