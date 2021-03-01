package com.imeautochange.nativefunction.windows;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;

import com.sun.jna.Function;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

public abstract class COMHelper {
	public static final int REGDB_E_CLASSNOTREG = 0x80040154;
	public static final int CLASS_E_NOAGGREGATION = 0x80040110;
	public static final int E_NOINTERFACE = 0x80004002;
	public static final int E_POINTER = 0x80004003;
	public static final int S_OK = 0;
	public static void initCOM(){
		Ole32.INSTANCE.CoInitialize(0);
		isCOMInitialized = true;
	}
	public static void uninitCOM(){
		Ole32.INSTANCE.CoUninitialize();
		isCOMInitialized = false;
	}
	private static HashMap<Class<?>, Handler> handlerList = new HashMap<Class<?>, Handler>();
	private static boolean isCOMInitialized = false;

	public static COMInterface loadInterface(Class<? extends COMInterface> comClazz, COMInfo comInfo) {
		if(!isCOMInitialized) {
			throw new IllegalStateException("COM not initialized");
		}
		System.out.println("clsidTfInputprocessorprofiles "+comInfo.rclsid);
		System.out.println("iidItfinputprocessorprofiles "+comInfo.riid);
		
		Pointer pv =new Pointer(0);
		PointerByReference ppv = new PointerByReference(pv);
		Ole32.INSTANCE.CoCreateInstance(comInfo.rclsid, null, comInfo.dwClsContext, comInfo.riid, ppv);
		pv = new Pointer(Pointer.nativeValue(ppv.getValue()));
		Handler handler = new Handler(pv, comInfo.offsetTable);
		Object proxy = Proxy.newProxyInstance(comClazz.getClassLoader(), new Class[] {comClazz}, handler);
		handlerList.put(proxy.getClass(), handler);
        return comClazz.cast(proxy);
	}

	public static COMInterface loadInterfaceByQuery(Class<? extends COMInterface> comClazz, COMInterface instance, COMInfo comInfo) {
		if(!isCOMInitialized) {
			throw new IllegalStateException("COM not initialized");
		}
		Handler qHandler = handlerList.get(instance.getClass());
		if(qHandler == null) {
			throw new IllegalArgumentException("The COM Interface sending the request is not created.");
		}
		System.out.println("requester lpVtbl "+Pointer.nativeValue(qHandler.getCOMlpVtbl()));
		Pointer qlpVtbl = qHandler.getCOMlpVtbl();
		Pointer qpv = qHandler.getCOMpv();
		Pointer pv =new Pointer(0);
		PointerByReference ppv = new PointerByReference(pv);
		Function QueryInterface = Function.getFunction(
				qlpVtbl.getPointer(0), 
				Function.ALT_CONVENTION);
		Object[] QueryInterfaceInArgList = new Object[3];
		QueryInterfaceInArgList[0] = qpv;
		QueryInterfaceInArgList[1] = comInfo.riid;
		QueryInterfaceInArgList[2] = ppv;
		QueryInterface.invoke(int.class, QueryInterfaceInArgList);
		System.out.println("ppv"+ppv.getValue());
		pv = new Pointer(Pointer.nativeValue(ppv.getValue()));
		Handler handler = new Handler(pv, comInfo.offsetTable);
		Object proxy = Proxy.newProxyInstance(comClazz.getClassLoader(), new Class[] {comClazz}, handler);
		handlerList.put(proxy.getClass(), handler);
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
		public Pointer getCOMpv() {
			return pv;
		}
		public Pointer getCOMlpVtbl() {
			return lpVtbl;
		}
		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//			System.out.println("offsetTable "+offsetTable);
//			System.out.println(method.getName());
//			System.out.println("lpVtbl "+lpVtbl);
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
