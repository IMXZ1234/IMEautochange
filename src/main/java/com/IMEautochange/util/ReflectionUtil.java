package com.IMEautochange.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectionUtil 
{
	public static Object getPrivateField(Class classIn, String name, Object object) throws NoSuchFieldException, IllegalAccessException
	{
        Field field = classIn.getDeclaredField(name);
        field.setAccessible(true);
        Object returnObject = field.get(object);
        return returnObject;
    }
	
	public static Method getPrivateMethod(Class classIn, String name, Object object, Class... parameterTypes) throws NoSuchMethodException, IllegalAccessException
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
