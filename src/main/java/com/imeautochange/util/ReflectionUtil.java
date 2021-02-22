package com.imeautochange.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;

import mezz.jei.gui.overlay.IngredientListOverlay;
import net.minecraft.client.gui.widget.TextFieldWidget;

public class ReflectionUtil 
{
	public static Object getFieldObject(Class<?> classIn, String name, Object object) throws NoSuchFieldException, IllegalAccessException
	{
        Field field = classIn.getDeclaredField(name);
        field.setAccessible(true);
        Object returnObject = field.get(object);
        return returnObject;
    }
	
	public static Field getField(Class<?> classIn, String name) throws NoSuchFieldException, IllegalAccessException
	{
		Field field = classIn.getDeclaredField(name);
		field.setAccessible(true);
		return field;
	}
	
	public static boolean getFieldObjectList(Class<?> classIn, Object object, ArrayList<Object> fieldObjectList)
	{
		fieldObjectList.clear();
		try {
			Field[] fields = classIn.getDeclaredFields();
			for (Field field:fields) {
				field.setAccessible(true);
				fieldObjectList.add(field.get(object));
			}
			return true;
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static <T> boolean getFieldObjectList(Class<?> classIn, Class<T> desiredClass, Object object, ArrayList<T> fieldObjectList)
	{
		fieldObjectList.clear();
		try {
			Field[] fields = classIn.getDeclaredFields();
			for (Field field:fields) {
				field.setAccessible(true);
				if(desiredClass.isAssignableFrom(field.getType())) {
					Object obj = field.get(object);
					if(obj != null) {
						fieldObjectList.add(desiredClass.cast(obj));
					}
				}
			}
			return true;
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean getFieldObjectList(Field[] fields, Object object, ArrayList<Object> fieldObjectList)
	{
		fieldObjectList.clear();
		try {
			for (Field field:fields) {
				field.setAccessible(true);
				fieldObjectList.add(field.get(object));
			}
			return true;
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean getFieldList(Class<?> classIn, ArrayList<Field> classFieldList)
	{
		classFieldList.clear();
		Field[] fields = classIn.getDeclaredFields();
		for (Field field:fields) {
			field.setAccessible(true);
			classFieldList.add(field);
		}
		return true;
	}
	
	public static boolean getFieldList(Class<?> classIn, Class<?> desiredClass, ArrayList<Field> classFieldList)
	{
		classFieldList.clear();
		Field[] fields = classIn.getDeclaredFields();
		for (Field field:fields) {
			field.setAccessible(true);
			if(desiredClass.isAssignableFrom(field.getType())) {
				classFieldList.add(field);
			}
		}
		return true;
	}
	
	public static Method getMethod(Class<?> classIn, String name, Object object, Class<?>... parameterTypes) throws NoSuchMethodException, IllegalAccessException
	{
        Method method = classIn.getDeclaredMethod(name, parameterTypes);
        method.setAccessible(true);
        return method;
    }
	
	public static void printReflectedMethodInfo(Method methodIn)
	{
		System.out.printf("Method: %s\n", methodIn.getName());
		Class<?>[] parametersTypes = methodIn.getParameterTypes();
		for (int i = 0; i < parametersTypes.length; i++)
		{
			System.out.printf("Parameter %d: %s\n", i, parametersTypes[i].getName());
		}
		Class<?> returnType = methodIn.getReturnType();
		System.out.printf("Returns: %s\n", returnType.getName());
	}
}
