package com.imeautochange.event;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import com.imeautochange.compat.ICompatOverlayAdapter;
import com.imeautochange.config.IMEInfo;

import net.minecraft.client.gui.screen.Screen;

public abstract class ModClientEventsHandler extends ModClientEventsHandlerBase {
	
	public static final int RESULT_CLASS_NOT_FOUND = 0x00000001;
	public static final int RESULT_FIELD_NOT_FOUND = 0x00000002;
	public static final int RESULT_FIELD_REPLACED = 0x00000004;
	public static final int RESULT_CLASS_REPLACED = 0x00000008;
	public static final int RESULT_FIELD_REMOVED = 0x00000010;
	public static final int RESULT_CLASS_REMOVED = 0x00000011;
	
//	protected HashMap<Class<?>, HashMap<Field, IMEInfo>> listenerClasses;
	protected HashMap<Class<?>, String> screenIMETable;
	protected HashMap<ICompatOverlayAdapter, String> overlayIMETable;
	public HashMap<Class<?>, ArrayList<Field>> cachedScreenFieldTable = new HashMap<Class<?>, ArrayList<Field>>();
	public HashMap<ICompatOverlayAdapter, ArrayList<Field>> cachedOverlayFieldList = new HashMap<ICompatOverlayAdapter, ArrayList<Field>>();

	public ModClientEventsHandler() {
		isRegistered = false;
//		listenerClasses = new HashMap<Class<?>, HashMap<Field, IMEInfo>>();
		screenIMETable = new HashMap<Class<?>, String>();
		overlayIMETable = new HashMap<ICompatOverlayAdapter, String>();
	}
	
//	public int addListenerField(Class<?> clazz, Field field, IMEInfo imeInfo) {
//		HashMap<Field, IMEInfo> classMap = listenerClasses.get(clazz);
//		if (classMap == null) {
//			classMap = new HashMap<Field, IMEInfo>();
//			classMap.put(field, imeInfo);
//			return RESULT_CLASS_NOT_FOUND | RESULT_SUCCESS;
//		} else {
//			if (classMap.put(field, imeInfo) == null) {
//				return RESULT_FIELD_NOT_FOUND | RESULT_SUCCESS;
//			} else {
//				return RESULT_FIELD_REPLACED | RESULT_SUCCESS;
//			}
//		}
//	}
//
//	public int removeListenerField(Class<?> clazz, Field field) {
//		HashMap<Field, IMEInfo> classMap = listenerClasses.get(clazz);
//		if (classMap == null) {
//			return RESULT_CLASS_NOT_FOUND | RESULT_FAIL;
//		} else {
//			if (classMap.remove(field) != null) {
//				if (classMap.isEmpty()) {
//					listenerClasses.remove(clazz);
//					return RESULT_CLASS_REMOVED | RESULT_FIELD_REMOVED | RESULT_SUCCESS;
//				} else {
//					return RESULT_FIELD_REMOVED | RESULT_SUCCESS;
//				}
//			} else {
//				return RESULT_FIELD_NOT_FOUND | RESULT_FAIL;
//			}
//		}
//	}
//	
	public int addScreenListenerClass(Class<?> clazz, String imeName) {
		String last;
		last = screenIMETable.put(clazz, imeName);
		if (last != null) {
			if (last.equals(imeName)) {
				return RESULT_SUCCESS;
			} else {
				return RESULT_CLASS_REPLACED | RESULT_SUCCESS;
			}
		}else {
			return RESULT_SUCCESS;
		}
	}
	public int addOverlayListenerClass(ICompatOverlayAdapter overlayAdapter, String imeName) {
		String last;
		last = overlayIMETable.put(overlayAdapter, imeName);
		if (last != null) {
			if (last.equals(imeName)) {
				return RESULT_SUCCESS;
			} else {
				return RESULT_CLASS_REPLACED | RESULT_SUCCESS;
			}
		}else {
			return RESULT_SUCCESS;
		}
	}
	
	public int removeScreenListenerClass(Class<?> clazz) {
		String last;
		last = screenIMETable.remove(clazz);
		if(last == null) {
			return RESULT_CLASS_NOT_FOUND | RESULT_FAIL;
		}else {
			return RESULT_CLASS_REMOVED | RESULT_SUCCESS;
		}
	}
	
	public int removeOverlayListenerClass(ICompatOverlayAdapter overlayAdapter) {
		String last;
		last = overlayIMETable.remove(overlayAdapter);
		if(last == null) {
			return RESULT_CLASS_NOT_FOUND | RESULT_FAIL;
		}else {
			return RESULT_CLASS_REMOVED | RESULT_SUCCESS;
		}
	}
}
