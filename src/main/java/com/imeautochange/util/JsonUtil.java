package com.imeautochange.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonUtil {
	public static Gson gsonPrettyHex;
	static {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(HashMap.class, new HashMapPrettyHexAdapter());
		gsonBuilder.setPrettyPrinting();
		gsonPrettyHex = gsonBuilder.create();
	}

	public static boolean saveHashMapToFile(HashMap<String, Long> map, File file) {
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			String mapjson = gsonPrettyHex.toJson(map);
			FileOutputStream fop = new FileOutputStream(file);
			OutputStreamWriter writer = new OutputStreamWriter(fop, StandardCharsets.UTF_8);
			writer.write(mapjson);
			writer.close();
			fop.close();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean loadHashMapfromFile(HashMap<String, Long> map, File file) {
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			FileInputStream fip = new FileInputStream(file);
			InputStreamReader reader = new InputStreamReader(fip, StandardCharsets.UTF_8);			
			map.putAll(gsonPrettyHex.fromJson(reader, HashMap.class));
			reader.close();
			fip.close();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
}
