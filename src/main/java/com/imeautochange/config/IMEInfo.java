package com.imeautochange.config;

public class IMEInfo {
	public String name;
	public String id;
	public String[] data;
	
	public IMEInfo(String name, String id, String... data) {
		this.name = name;
		this.id = id;
		this.data = new String[data.length];
		for(int i =0;i<data.length;i++) {
			this.data[i] = data[i];
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof IMEInfo)) {
			return false;
		}
		if(this == obj) {
			return true;
		}
		if(!(name.equals(((IMEInfo)obj).name) && id.equals(((IMEInfo)obj).id) && data.length == ((IMEInfo)obj).data.length)) {
			return false;
		}
		for(int i=0;i<data.length;i++) {
			if(!data[i].equals(((IMEInfo)obj).data[i])){
				return false;
			}
		}
		return true;
	}
	
	@Override
	public String toString() {
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append("IME name:\t");
		strBuilder.append(name);
		strBuilder.append("\nInternal ID:\t");
		strBuilder.append(id);
		for(int i =0;i<data.length;i++) {
			strBuilder.append("\nData ");
			strBuilder.append(i);
			strBuilder.append(" :\t");
			strBuilder.append(this.data[i]);
		}
		return strBuilder.toString();
	}
}
