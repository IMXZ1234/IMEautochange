package com.IMEautochange.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.IllegalFormatException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class HashMapPrettyHexAdapter extends TypeAdapter<HashMap<String, Long>> {
	@Override
	public HashMap<String, Long> read(JsonReader reader) throws IOException {
		HashMap<String, Long> map = new HashMap<String, Long>();
		if (reader.peek() == JsonToken.BEGIN_OBJECT) {
			reader.beginObject();
			while (reader.peek() != JsonToken.END_OBJECT) {
				String key = reader.nextName();
				String idRawStr = reader.nextString();
				Long id;
				try {
					id = Long.parseUnsignedLong(idRawStr.replaceAll("0x0*", ""), 16);
				} catch (NumberFormatException e) {
					e.printStackTrace();
					continue;
				}
				map.put(key, id);
			}
			reader.endObject();
			return map;
		} else {
			reader.skipValue();
			return map;
		}
	}

	@Override
	public void write(JsonWriter writer, HashMap<String, Long> value) throws IOException {
		if (value == null) {
			writer.nullValue();
			return;
		} else {
			writer.beginObject();
			Iterator<Entry<String, Long>> iter = value.entrySet().iterator();
			Map.Entry<String, Long> entry;
			while (iter.hasNext()) {
				entry = (Map.Entry<String, Long>) iter.next();
				writer.name(entry.getKey());
				StringBuilder builder;
				try {
					builder = new StringBuilder(String.format("%#x", entry.getValue().longValue()));
				} catch (IllegalFormatException e) {
					e.printStackTrace();
					continue;
				}
				System.out.println(builder.toString());
				while (builder.length() < 10)
					builder.insert(2, '0');
				System.out.println(builder.toString());
				writer.value(builder.toString());
			}
			writer.endObject();
			return;
		}
	}
}
