package com.IMEautochange.util;

import java.lang.reflect.Field;

public class ReflectionUtil {
	public static Object getPrivateField(Class classIn,String name,Object object) throws NoSuchFieldException, IllegalAccessException {
        Field field = classIn.getDeclaredField(name);
        field.setAccessible(true);
        Object returnObject = field.get(object);
        return returnObject;
    }

}
