package com.imeautochange.nativefunction.windows;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;

import com.sun.jna.Function;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;
//import com.sun.jna.platform.win32.Guid.GUID;
//import com.sun.jna.platform.win32.Ole32;

public abstract class COMHelper {
	public static final int REGDB_E_CLASSNOTREG = 0x80040154;
	public static final int CLASS_E_NOAGGREGATION = 0x80040110;
	public static final int E_NOINTERFACE = 0x80004002;
	public static final int E_POINTER = 0x80004003;
	public static final int S_OK = 0;

	public static void initCOM() {
		if (isCOMInitialized) {
			return;
		}
		Ole32.INSTANCE.CoInitialize(0);
		isCOMInitialized = true;
	}

	public static void uninitCOM() {
		if (!isCOMInitialized) {
			return;
		}
		Ole32.INSTANCE.CoUninitialize();
		isCOMInitialized = false;
	}
	
	private static HashMap<Class<?>, Handler> handlerList = new HashMap<Class<?>, Handler>();
	private static boolean isCOMInitialized = false;

	public static COMInterface loadInterface(Class<? extends COMInterface> comClazz, COMInfo comInfo) {
		if(!isCOMInitialized) {
			initCOM();
			isCOMInitialized = true;
		}
		System.out.println("clsidTfInputprocessorprofiles "+comInfo.rclsid);
		System.out.println("iidItfinputprocessorprofiles "+comInfo.riid);
		
		Pointer pv =new Pointer(0);
		PointerByReference ppv = new PointerByReference(pv);
		Ole32.INSTANCE.CoCreateInstance(comInfo.rclsid, null, comInfo.dwClsContext, comInfo.riid, ppv);
		pv = new Pointer(Pointer.nativeValue(ppv.getValue()));
//		System.out.println("hr "+hr);
//		System.out.println("pv "+Pointer.nativeValue(pv));
//		Pointer lpVtbl = pv.getPointer(0);
//		System.out.println("lpVtbl "+Pointer.nativeValue(lpVtbl));
		Handler handler = new Handler(pv, comInfo.offsetTable);
		handlerList.put(comClazz, handler);
		Object proxy = Proxy.newProxyInstance(comClazz.getClassLoader(), new Class[] {comClazz}, handler);
        return comClazz.cast(proxy);
	}

	

	static class Handler implements InvocationHandler {
		private final Pointer pv;
		private final Pointer lpVtbl;
		private final HashMap<String, Integer> offsetTable;
		public Handler(Pointer pv, HashMap<String, Integer> offsetTable) {
			this.pv = pv;
			this.lpVtbl = pv.getPointer(0);
			this.offsetTable = offsetTable;
		}
		public Pointer getCOMlpVtbl() {
			return lpVtbl;
		}
		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//			System.out.println("offsetTable "+offsetTable);
			System.out.println(method.getName());
//			System.out.println("lpVtbl "+lpVtbl);
//			System.out.println("Native.POINTER_SIZE "+Native.POINTER_SIZE);
			Object[] callArgs = new Object[args.length + 1];
			for (int i = 0; i < args.length; i++) {
				callArgs[i + 1] = args[i];
			}
			callArgs[0] = pv;
			Function func = Function.getFunction(
					lpVtbl.getPointer(offsetTable.get(method.getName())*Native.POINTER_SIZE), 
					Function.ALT_CONVENTION);
			return func.invoke(method.getReturnType(), callArgs);
		}

	}
}
