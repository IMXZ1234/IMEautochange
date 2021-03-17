package com.imeautochange.event;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import com.imeautochange.compat.IOverlayAdapter;

public abstract class ModClientEventsHandlerCommon extends ModClientEventsHandlerBase {
	
	public static final int RESULT_CLASS_NOT_FOUND = 0x00000001;
	public static final int RESULT_FIELD_NOT_FOUND = 0x00000002;
	public static final int RESULT_FIELD_REPLACED = 0x00000004;
	public static final int RESULT_CLASS_REPLACED = 0x00000008;
	public static final int RESULT_FIELD_REMOVED = 0x00000010;
	public static final int RESULT_CLASS_REMOVED = 0x00000011;
	
	protected HashMap<Class<?>, String> screenIMETable;
	protected HashMap<IOverlayAdapter, String> overlayIMETable;
	public HashMap<Class<?>, ArrayList<Field>> cachedScreenFieldTable = new HashMap<Class<?>, ArrayList<Field>>();
	public HashMap<IOverlayAdapter, ArrayList<Field>> cachedOverlayFieldList = new HashMap<IOverlayAdapter, ArrayList<Field>>();

	public ModClientEventsHandlerCommon() {
		isRegistered = false;
		screenIMETable = new HashMap<Class<?>, String>();
		overlayIMETable = new HashMap<IOverlayAdapter, String>();
	}
	
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
	public int addOverlayListenerClass(IOverlayAdapter overlayAdapter, String imeName) {
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
	
	public int removeOverlayListenerClass(IOverlayAdapter overlayAdapter) {
		String last;
		System.out.println(this.getClass().getName());
		System.out.println("removing"+overlayAdapter.getClass().getName());
		last = overlayIMETable.remove(overlayAdapter);
		if(last == null) {
			return RESULT_CLASS_NOT_FOUND | RESULT_FAIL;
		}else {
			return RESULT_CLASS_REMOVED | RESULT_SUCCESS;
		}
	}
}
